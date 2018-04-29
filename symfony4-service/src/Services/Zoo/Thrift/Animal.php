<?php
namespace Services\Zoo\Thrift;

/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
use Thrift\Base\TBase;
use Thrift\Type\TType;
use Thrift\Type\TMessageType;
use Thrift\Exception\TException;
use Thrift\Exception\TProtocolException;
use Thrift\Protocol\TProtocol;
use Thrift\Protocol\TBinaryProtocolAccelerated;
use Thrift\Exception\TApplicationException;
use JsonSerializable;
use stdClass;


class Animal extends TBase implements JsonSerializable {
  static $isValidate = false;

  static $_TSPEC = array(
    1 => array(
      'var' => 'name',
      'isRequired' => false,
      'type' => TType::STRING,
      ),
    2 => array(
      'var' => 'type',
      'isRequired' => false,
      'type' => TType::STRING,
      ),
    3 => array(
      'var' => 'vaccinated',
      'isRequired' => false,
      'type' => TType::BOOL,
      ),
    );

  /**
   * @var string
   */
  public $name = null;
  /**
   * @var string
   */
  public $type = null;
  /**
   * @var bool
   */
  public $vaccinated = null;

  public function __construct($vals=null) {
    if (is_array($vals)) {
      parent::__construct(self::$_TSPEC, $vals);
    }
  }

  public function getName() {
    return 'Animal';
  }

  public function read($input)
  {
    return $this->_read('Animal', self::$_TSPEC, $input);
  }

  public function write($output) {
    return $this->_write('Animal', self::$_TSPEC, $output);
  }

  public function jsonSerialize() {
    $json = new stdClass;
    if ($this->name !== null) {
      $json->name = (string)$this->name;
    }
    if ($this->type !== null) {
      $json->type = (string)$this->type;
    }
    if ($this->vaccinated !== null) {
      $json->vaccinated = (bool)$this->vaccinated;
    }
    return $json;
  }

}

