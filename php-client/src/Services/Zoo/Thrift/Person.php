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


class Person extends TBase implements JsonSerializable {
  static $isValidate = false;

  static $_TSPEC = array(
    1 => array(
      'var' => 'firstName',
      'isRequired' => false,
      'type' => TType::STRING,
      ),
    2 => array(
      'var' => 'lastName',
      'isRequired' => false,
      'type' => TType::STRING,
      ),
    3 => array(
      'var' => 'governmentId',
      'isRequired' => false,
      'type' => TType::STRING,
      ),
    4 => array(
      'var' => 'yearsOfExperience',
      'isRequired' => false,
      'type' => TType::I32,
      ),
    );

  /**
   * @var string
   */
  public $firstName = null;
  /**
   * @var string
   */
  public $lastName = null;
  /**
   * @var string
   */
  public $governmentId = null;
  /**
   * @var int
   */
  public $yearsOfExperience = null;

  public function __construct($vals=null) {
    if (is_array($vals)) {
      parent::__construct(self::$_TSPEC, $vals);
    }
  }

  public function getName() {
    return 'Person';
  }

  public function read($input)
  {
    return $this->_read('Person', self::$_TSPEC, $input);
  }

  public function write($output) {
    return $this->_write('Person', self::$_TSPEC, $output);
  }

  public function jsonSerialize() {
    $json = new stdClass;
    if ($this->firstName !== null) {
      $json->firstName = (string)$this->firstName;
    }
    if ($this->lastName !== null) {
      $json->lastName = (string)$this->lastName;
    }
    if ($this->governmentId !== null) {
      $json->governmentId = (string)$this->governmentId;
    }
    if ($this->yearsOfExperience !== null) {
      $json->yearsOfExperience = (int)$this->yearsOfExperience;
    }
    return $json;
  }

}

