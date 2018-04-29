<?php

namespace Services\Zoo\Client;

use function GuzzleHttp\Psr7\stream_for;
use Psr\Http\Message\RequestInterface;
use Psr\Http\Message\ResponseInterface;
use Thrift\Base\TBase;
use Thrift\Protocol\TJSONProtocol;
use Thrift\Protocol\TProtocol;
use Thrift\Protocol\TSimpleJSONProtocol;
use Thrift\Transport\TMemoryBuffer;

/**
 * A simple service for writing / reading Thrift entities to / from PSR-7 HTTP requests / responses.
 *
 * @package Services\Zoo\Client
 */
class ThriftMessageConverter
{
    private const THRIFT_JSON_MEDIA_TYPE = "application/vnd.apache.thrift.json";
    private const THRIFT_JSON_MEDIA_TYPE_LEGACY = "application/x-thrift+json";
    private const SIMPLE_JSON_MEDIA_TYPE = "application/json";

    /**
     * Reads a Thrift entity from a PSR-7 HTTP response. Takes the media type in the Content-Type header (if there's one)
     * into consideration when deserializing the request payload.
     *
     * @param ResponseInterface $response The HTTP response to read the Thrift entity from
     * @param string $thriftType The FQCN of the Thrift entity to construct
     *
     * @return TBase The constructed Thrift entity
     *
     * @throws \InvalidArgumentException If the payload's media type is not supported
     */
    public function read(ResponseInterface $response, string $thriftType): TBase
    {
        $contentType = $response->getHeader("Content-Type");
        $contentType = empty($contentType) ? null : $contentType[0];
        if (false !== $pos = strpos($contentType, ';')) {
            $contentType = substr($contentType, 0, $pos);
        }

        $protocolType = $this->getThriftProtocolTypeForMediaType($contentType)[0];
        if (!$protocolType || $protocolType === TSimpleJSONProtocol::class) {
            throw new \InvalidArgumentException("Unsupported media type $contentType");
        }

        $buffer = new TMemoryBuffer($response->getBody()->getContents());
        /** @var TProtocol $protocol */
        $protocol = new $protocolType($buffer);
        try {
            $buffer->open();

            /** @var TBase $entity */
            $entity = new $thriftType();
            $entity->read($protocol);

            return $entity;
        } finally {
            if ($buffer->isOpen()) {
                $buffer->close();
            }
        }
    }

    /**
     * Writes a Thrift entity to a PSR-7 HTTP request. Takes the media type in the Content-Type header (if there's one)
     * into consideration when serializing the provided entity. It also sets the correct Accept & Content-Type headers
     * on the request.
     * Due to the immutable nature of RequestInterface, this method will return the modified request to the caller.
     *
     * @param RequestInterface $request The HTTP request to write the Thrift entity to
     * @param TBase $thriftEntity The Thrift entity to write to the request
     *
     * @return RequestInterface The request containing the serialized Thrift entity
     *
     * @throws \InvalidArgumentException If none of the acceptable media types are supported
     */
    public function write(RequestInterface $request, TBase $thriftEntity): RequestInterface
    {
        $contentType = $request->getHeader("Content-Type");
        $contentType = empty($contentType) ? null : $contentType[0];
        if (false !== $pos = strpos($contentType, ';')) {
            $contentType = substr($contentType, 0, $pos);
        }

        [$protocolType, $selectedMediaType] = $this->getThriftProtocolTypeForMediaType($contentType);
        if (!$protocolType) {
            throw new \InvalidArgumentException("Unsupported media type $contentType");
        }

        $buffer = new TMemoryBuffer();
        /** @var TProtocol $protocol */
        $protocol = new $protocolType($buffer);
        try {
            $buffer->open();

            $thriftEntity->write($protocol);

            return $request
                ->withBody(stream_for($buffer->readAll($buffer->available())))
                ->withHeader("Content-Type", $selectedMediaType)
                ->withHeader(
                    "Accept",
                    [
                        self::THRIFT_JSON_MEDIA_TYPE,
                        self::THRIFT_JSON_MEDIA_TYPE_LEGACY,
                        self::SIMPLE_JSON_MEDIA_TYPE
                    ]
                );
        } finally {
            if ($buffer->isOpen()) {
                $buffer->close();
            }
        }
    }

    /**
     * Returns the Thrift protocol type that's appropriate for the provided media type.
     *
     * @param string $mediaType The media type
     *
     * @return string[] The appropriate Thrift protocol's FQCN and the primary media type associated with that protocol
     */
    private function getThriftProtocolTypeForMediaType(?string $mediaType): array
    {
        switch ($mediaType) {
            case self::THRIFT_JSON_MEDIA_TYPE:
            case self::THRIFT_JSON_MEDIA_TYPE_LEGACY:
            case "":
            case null:
                $protocolType = TJSONProtocol::class;
                $primaryMediaType = self::THRIFT_JSON_MEDIA_TYPE;
                break;
            case self::SIMPLE_JSON_MEDIA_TYPE:
                $protocolType = TSimpleJSONProtocol::class;
                $primaryMediaType = self::SIMPLE_JSON_MEDIA_TYPE;
                break;
            default:
                $protocolType = null;
                $primaryMediaType = null;
        }

        return [$protocolType, $primaryMediaType];
    }
}
