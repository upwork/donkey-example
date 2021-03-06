/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.example.services.zoo.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-04-27")
public class AnimalList implements org.apache.thrift.TBase<AnimalList, AnimalList._Fields>, java.io.Serializable, Cloneable, Comparable<AnimalList> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("AnimalList");

  private static final org.apache.thrift.protocol.TField ANIMALS_FIELD_DESC = new org.apache.thrift.protocol.TField("animals", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new AnimalListStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new AnimalListTupleSchemeFactory();

  public java.util.List<Animal> animals; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ANIMALS((short)1, "animals");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ANIMALS
          return ANIMALS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ANIMALS, new org.apache.thrift.meta_data.FieldMetaData("animals", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Animal.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(AnimalList.class, metaDataMap);
  }

  public AnimalList() {
  }

  public AnimalList(
    java.util.List<Animal> animals)
  {
    this();
    this.animals = animals;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public AnimalList(AnimalList other) {
    if (other.isSetAnimals()) {
      java.util.List<Animal> __this__animals = new java.util.ArrayList<Animal>(other.animals.size());
      for (Animal other_element : other.animals) {
        __this__animals.add(new Animal(other_element));
      }
      this.animals = __this__animals;
    }
  }

  public AnimalList deepCopy() {
    return new AnimalList(this);
  }

  @Override
  public void clear() {
    this.animals = null;
  }

  public int getAnimalsSize() {
    return (this.animals == null) ? 0 : this.animals.size();
  }

  public java.util.Iterator<Animal> getAnimalsIterator() {
    return (this.animals == null) ? null : this.animals.iterator();
  }

  public void addToAnimals(Animal elem) {
    if (this.animals == null) {
      this.animals = new java.util.ArrayList<Animal>();
    }
    this.animals.add(elem);
  }

  public java.util.List<Animal> getAnimals() {
    return this.animals;
  }

  public AnimalList setAnimals(java.util.List<Animal> animals) {
    this.animals = animals;
    return this;
  }

  public void unsetAnimals() {
    this.animals = null;
  }

  /** Returns true if field animals is set (has been assigned a value) and false otherwise */
  public boolean isSetAnimals() {
    return this.animals != null;
  }

  public void setAnimalsIsSet(boolean value) {
    if (!value) {
      this.animals = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case ANIMALS:
      if (value == null) {
        unsetAnimals();
      } else {
        setAnimals((java.util.List<Animal>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case ANIMALS:
      return getAnimals();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case ANIMALS:
      return isSetAnimals();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof AnimalList)
      return this.equals((AnimalList)that);
    return false;
  }

  public boolean equals(AnimalList that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_animals = true && this.isSetAnimals();
    boolean that_present_animals = true && that.isSetAnimals();
    if (this_present_animals || that_present_animals) {
      if (!(this_present_animals && that_present_animals))
        return false;
      if (!this.animals.equals(that.animals))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetAnimals()) ? 131071 : 524287);
    if (isSetAnimals())
      hashCode = hashCode * 8191 + animals.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(AnimalList other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetAnimals()).compareTo(other.isSetAnimals());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAnimals()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.animals, other.animals);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("AnimalList(");
    boolean first = true;

    sb.append("animals:");
    if (this.animals == null) {
      sb.append("null");
    } else {
      sb.append(this.animals);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class AnimalListStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public AnimalListStandardScheme getScheme() {
      return new AnimalListStandardScheme();
    }
  }

  private static class AnimalListStandardScheme extends org.apache.thrift.scheme.StandardScheme<AnimalList> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, AnimalList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ANIMALS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.animals = new java.util.ArrayList<Animal>(_list8.size);
                Animal _elem9;
                for (int _i10 = 0; _i10 < _list8.size; ++_i10)
                {
                  _elem9 = new Animal();
                  _elem9.read(iprot);
                  struct.animals.add(_elem9);
                }
                iprot.readListEnd();
              }
              struct.setAnimalsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, AnimalList struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.animals != null) {
        oprot.writeFieldBegin(ANIMALS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.animals.size()));
          for (Animal _iter11 : struct.animals)
          {
            _iter11.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class AnimalListTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public AnimalListTupleScheme getScheme() {
      return new AnimalListTupleScheme();
    }
  }

  private static class AnimalListTupleScheme extends org.apache.thrift.scheme.TupleScheme<AnimalList> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, AnimalList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetAnimals()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetAnimals()) {
        {
          oprot.writeI32(struct.animals.size());
          for (Animal _iter12 : struct.animals)
          {
            _iter12.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, AnimalList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.animals = new java.util.ArrayList<Animal>(_list13.size);
          Animal _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new Animal();
            _elem14.read(iprot);
            struct.animals.add(_elem14);
          }
        }
        struct.setAnimalsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

