<?php

namespace Services\Zoo\Exception;

/**
 * @package Services\Zoo\Exception
 *
 * Generated by com.upwork.donkey.example.php.GeneratorAwareTemplateGroupFactory vdonkey-example:1.0-SNAPSHOT
 */
abstract class ZooException extends \Exception
{
    /**
     * @var int
     */
    private $statusCode;

    /**
     * @param int $statusCode
     * @param int $errorCode
     * @param string|null $message
     * @param \Throwable|null $previous
     */
    public function __construct(int $statusCode, int $errorCode, ?string $message, ?\Throwable $previous)
    {
        parent::__construct($message, $errorCode, $previous);

        $this->statusCode = $statusCode;
    }

    /**
     * @return int
     */
    public function getStatusCode(): int
    {
        return $this->statusCode;
    }

    /**
     * @return int
     */
    public function getErrorCode(): int
    {
        return $this->getCode();
    }
}