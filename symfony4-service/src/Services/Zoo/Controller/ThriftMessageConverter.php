<?php

namespace Services\Zoo\Controller;

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Thrift\Base\TBase;
use Thrift\Protocol\TJSONProtocol;
use Thrift\Protocol\TProtocol;
use Thrift\Protocol\TSimpleJSONProtocol;
use Thrift\Transport\TMemoryBuffer;

/**
 * A simple service for writing / reading Thrift entities to / from Symfony HTTP requests / responses.
 *
 * @package Services\Zoo\Controller
 */
class ThriftMessageConverter
{
    private const THRIFT_JSON_MEDIA_TYPE = "application/vnd.apache.thrift.json";
    private const THRIFT_JSON_MEDIA_TYPE_LEGACY = "application/x-thrift+json";
    private const SIMPLE_JSON_MEDIA_TYPE = "application/json";
    private const ANY_APPLICATION_MEDIA_TYPE = "application/*";
    private const ANY_MEDIA_TYPE = "*/*";

    /**
     * Reads a Thrift entity from a Symfony HTTP request. Takes the media type in the Content-Type header (if there's one)
     * into consideration when deserializing the request payload.
     *
     * @param Request $request The HTTP request to read the Thrift entity from
     * @param string $thriftType The FQCN of the Thrift entity to construct
     *
     * @return TBase The constructed Thrift entity
     *
     * @throws \InvalidArgumentException If the payload's media type is not supported
     */
    public function read(Request $request, string $thriftType): TBase
    {
        $contentType = $request->headers->get("Content-Type");
        if (false !== $pos = strpos($contentType, ';')) {
            $contentType = substr($contentType, 0, $pos);
        }

        $protocolType = $this->getThriftProtocolTypeForMediaType($contentType)[0];
        if (!$protocolType || $protocolType === TSimpleJSONProtocol::class) {
            throw new \InvalidArgumentException("Unsupported media type {$contentType}");
        }

        $buffer = new TMemoryBuffer($request->getContent());
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
     * Writes a Thrift entity to a Symfony HTTP response. Takes the media types in the Accept header (if there's one)
     * into consideration when serializing the provided entity. It also sets the correct Content-Type header on the response.
     *
     * @param Response $response The HTTP response to write the Thrift entity to
     * @param TBase $thriftEntity The Thrift entity to write to the response
     * @param string[] $acceptableMediaTypes The acceptable media types (in order of preference) as defined in the incoming request's Accept header (if one was provided)
     *
     * @throws \InvalidArgumentException If none of the acceptable media types are supported
     */
    public function write(Response $response, TBase $thriftEntity, array $acceptableMediaTypes): void
    {
        if (empty($acceptableMediaTypes)) {
            $acceptableMediaTypes[] = self::ANY_MEDIA_TYPE;
        }

        $protocolType = null;
        $selectedMediaType = null;
        foreach ($acceptableMediaTypes as $acceptableMediaType) {
            [$protocolType, $selectedMediaType] = $this->getThriftProtocolTypeForMediaType($acceptableMediaType);
            if ($protocolType) {
                break;
            }
        }

        if (!$protocolType) {
            throw new \InvalidArgumentException(
                "Couldn't find supported media type among [".implode(",", $acceptableMediaTypes)."]"
            );
        }

        $buffer = new TMemoryBuffer();
        /** @var TProtocol $protocol */
        $protocol = new $protocolType($buffer);
        try {
            $buffer->open();

            $thriftEntity->write($protocol);

            $response->setContent($buffer->readAll($buffer->available()));
            $response->headers->set("Content-Type", $selectedMediaType);
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
            case self::ANY_APPLICATION_MEDIA_TYPE:
            case self::ANY_MEDIA_TYPE:
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
