<?php

namespace Services\Zoo\Service;

use Psr\SimpleCache\CacheInterface;
use Symfony\Component\Cache\Simple\FilesystemCache;

trait DataStore
{
    /**
     * @var CacheInterface
     */
    private $dataStore;

    protected function initDataStore()
    {
        $this->dataStore = new FilesystemCache("app.cache");
    }
}