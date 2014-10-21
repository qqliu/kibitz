/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package datahub;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSet implements org.apache.thrift.TBase<ResultSet, ResultSet._Fields>, java.io.Serializable, Cloneable, Comparable<ResultSet> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ResultSet");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField CON_FIELD_DESC = new org.apache.thrift.protocol.TField("con", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField NUM_TUPLES_FIELD_DESC = new org.apache.thrift.protocol.TField("num_tuples", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField NUM_MORE_TUPLES_FIELD_DESC = new org.apache.thrift.protocol.TField("num_more_tuples", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField TUPLES_FIELD_DESC = new org.apache.thrift.protocol.TField("tuples", org.apache.thrift.protocol.TType.LIST, (short)5);
  private static final org.apache.thrift.protocol.TField FIELD_NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("field_names", org.apache.thrift.protocol.TType.LIST, (short)6);
  private static final org.apache.thrift.protocol.TField FIELD_TYPES_FIELD_DESC = new org.apache.thrift.protocol.TField("field_types", org.apache.thrift.protocol.TType.LIST, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResultSetStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResultSetTupleSchemeFactory());
  }

  public boolean status; // required
  public Connection con; // optional
  public long num_tuples; // optional
  public long num_more_tuples; // optional
  public List<Tuple> tuples; // optional
  public List<String> field_names; // optional
  public List<String> field_types; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    CON((short)2, "con"),
    NUM_TUPLES((short)3, "num_tuples"),
    NUM_MORE_TUPLES((short)4, "num_more_tuples"),
    TUPLES((short)5, "tuples"),
    FIELD_NAMES((short)6, "field_names"),
    FIELD_TYPES((short)7, "field_types");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // STATUS
          return STATUS;
        case 2: // CON
          return CON;
        case 3: // NUM_TUPLES
          return NUM_TUPLES;
        case 4: // NUM_MORE_TUPLES
          return NUM_MORE_TUPLES;
        case 5: // TUPLES
          return TUPLES;
        case 6: // FIELD_NAMES
          return FIELD_NAMES;
        case 7: // FIELD_TYPES
          return FIELD_TYPES;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __STATUS_ISSET_ID = 0;
  private static final int __NUM_TUPLES_ISSET_ID = 1;
  private static final int __NUM_MORE_TUPLES_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.CON,_Fields.NUM_TUPLES,_Fields.NUM_MORE_TUPLES,_Fields.TUPLES,_Fields.FIELD_NAMES,_Fields.FIELD_TYPES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.CON, new org.apache.thrift.meta_data.FieldMetaData("con", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Connection.class)));
    tmpMap.put(_Fields.NUM_TUPLES, new org.apache.thrift.meta_data.FieldMetaData("num_tuples", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.NUM_MORE_TUPLES, new org.apache.thrift.meta_data.FieldMetaData("num_more_tuples", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.TUPLES, new org.apache.thrift.meta_data.FieldMetaData("tuples", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Tuple.class))));
    tmpMap.put(_Fields.FIELD_NAMES, new org.apache.thrift.meta_data.FieldMetaData("field_names", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.FIELD_TYPES, new org.apache.thrift.meta_data.FieldMetaData("field_types", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ResultSet.class, metaDataMap);
  }

  public ResultSet() {
  }

  public ResultSet(
    boolean status)
  {
    this();
    this.status = status;
    setStatusIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ResultSet(ResultSet other) {
    __isset_bitfield = other.__isset_bitfield;
    this.status = other.status;
    if (other.isSetCon()) {
      this.con = new Connection(other.con);
    }
    this.num_tuples = other.num_tuples;
    this.num_more_tuples = other.num_more_tuples;
    if (other.isSetTuples()) {
      List<Tuple> __this__tuples = new ArrayList<Tuple>(other.tuples.size());
      for (Tuple other_element : other.tuples) {
        __this__tuples.add(new Tuple(other_element));
      }
      this.tuples = __this__tuples;
    }
    if (other.isSetField_names()) {
      List<String> __this__field_names = new ArrayList<String>(other.field_names);
      this.field_names = __this__field_names;
    }
    if (other.isSetField_types()) {
      List<String> __this__field_types = new ArrayList<String>(other.field_types);
      this.field_types = __this__field_types;
    }
  }

  public ResultSet deepCopy() {
    return new ResultSet(this);
  }

  @Override
  public void clear() {
    setStatusIsSet(false);
    this.status = false;
    this.con = null;
    setNum_tuplesIsSet(false);
    this.num_tuples = 0;
    setNum_more_tuplesIsSet(false);
    this.num_more_tuples = 0;
    this.tuples = null;
    this.field_names = null;
    this.field_types = null;
  }

  public boolean isStatus() {
    return this.status;
  }

  public ResultSet setStatus(boolean status) {
    this.status = status;
    setStatusIsSet(true);
    return this;
  }

  public void unsetStatus() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return EncodingUtils.testBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  public void setStatusIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATUS_ISSET_ID, value);
  }

  public Connection getCon() {
    return this.con;
  }

  public ResultSet setCon(Connection con) {
    this.con = con;
    return this;
  }

  public void unsetCon() {
    this.con = null;
  }

  /** Returns true if field con is set (has been assigned a value) and false otherwise */
  public boolean isSetCon() {
    return this.con != null;
  }

  public void setConIsSet(boolean value) {
    if (!value) {
      this.con = null;
    }
  }

  public long getNum_tuples() {
    return this.num_tuples;
  }

  public ResultSet setNum_tuples(long num_tuples) {
    this.num_tuples = num_tuples;
    setNum_tuplesIsSet(true);
    return this;
  }

  public void unsetNum_tuples() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __NUM_TUPLES_ISSET_ID);
  }

  /** Returns true if field num_tuples is set (has been assigned a value) and false otherwise */
  public boolean isSetNum_tuples() {
    return EncodingUtils.testBit(__isset_bitfield, __NUM_TUPLES_ISSET_ID);
  }

  public void setNum_tuplesIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __NUM_TUPLES_ISSET_ID, value);
  }

  public long getNum_more_tuples() {
    return this.num_more_tuples;
  }

  public ResultSet setNum_more_tuples(long num_more_tuples) {
    this.num_more_tuples = num_more_tuples;
    setNum_more_tuplesIsSet(true);
    return this;
  }

  public void unsetNum_more_tuples() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __NUM_MORE_TUPLES_ISSET_ID);
  }

  /** Returns true if field num_more_tuples is set (has been assigned a value) and false otherwise */
  public boolean isSetNum_more_tuples() {
    return EncodingUtils.testBit(__isset_bitfield, __NUM_MORE_TUPLES_ISSET_ID);
  }

  public void setNum_more_tuplesIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __NUM_MORE_TUPLES_ISSET_ID, value);
  }

  public int getTuplesSize() {
    return (this.tuples == null) ? 0 : this.tuples.size();
  }

  public java.util.Iterator<Tuple> getTuplesIterator() {
    return (this.tuples == null) ? null : this.tuples.iterator();
  }

  public void addToTuples(Tuple elem) {
    if (this.tuples == null) {
      this.tuples = new ArrayList<Tuple>();
    }
    this.tuples.add(elem);
  }

  public List<Tuple> getTuples() {
    return this.tuples;
  }

  public ResultSet setTuples(List<Tuple> tuples) {
    this.tuples = tuples;
    return this;
  }

  public void unsetTuples() {
    this.tuples = null;
  }

  /** Returns true if field tuples is set (has been assigned a value) and false otherwise */
  public boolean isSetTuples() {
    return this.tuples != null;
  }

  public void setTuplesIsSet(boolean value) {
    if (!value) {
      this.tuples = null;
    }
  }

  public int getField_namesSize() {
    return (this.field_names == null) ? 0 : this.field_names.size();
  }

  public java.util.Iterator<String> getField_namesIterator() {
    return (this.field_names == null) ? null : this.field_names.iterator();
  }

  public void addToField_names(String elem) {
    if (this.field_names == null) {
      this.field_names = new ArrayList<String>();
    }
    this.field_names.add(elem);
  }

  public List<String> getField_names() {
    return this.field_names;
  }

  public ResultSet setField_names(List<String> field_names) {
    this.field_names = field_names;
    return this;
  }

  public void unsetField_names() {
    this.field_names = null;
  }

  /** Returns true if field field_names is set (has been assigned a value) and false otherwise */
  public boolean isSetField_names() {
    return this.field_names != null;
  }

  public void setField_namesIsSet(boolean value) {
    if (!value) {
      this.field_names = null;
    }
  }

  public int getField_typesSize() {
    return (this.field_types == null) ? 0 : this.field_types.size();
  }

  public java.util.Iterator<String> getField_typesIterator() {
    return (this.field_types == null) ? null : this.field_types.iterator();
  }

  public void addToField_types(String elem) {
    if (this.field_types == null) {
      this.field_types = new ArrayList<String>();
    }
    this.field_types.add(elem);
  }

  public List<String> getField_types() {
    return this.field_types;
  }

  public ResultSet setField_types(List<String> field_types) {
    this.field_types = field_types;
    return this;
  }

  public void unsetField_types() {
    this.field_types = null;
  }

  /** Returns true if field field_types is set (has been assigned a value) and false otherwise */
  public boolean isSetField_types() {
    return this.field_types != null;
  }

  public void setField_typesIsSet(boolean value) {
    if (!value) {
      this.field_types = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((Boolean)value);
      }
      break;

    case CON:
      if (value == null) {
        unsetCon();
      } else {
        setCon((Connection)value);
      }
      break;

    case NUM_TUPLES:
      if (value == null) {
        unsetNum_tuples();
      } else {
        setNum_tuples((Long)value);
      }
      break;

    case NUM_MORE_TUPLES:
      if (value == null) {
        unsetNum_more_tuples();
      } else {
        setNum_more_tuples((Long)value);
      }
      break;

    case TUPLES:
      if (value == null) {
        unsetTuples();
      } else {
        setTuples((List<Tuple>)value);
      }
      break;

    case FIELD_NAMES:
      if (value == null) {
        unsetField_names();
      } else {
        setField_names((List<String>)value);
      }
      break;

    case FIELD_TYPES:
      if (value == null) {
        unsetField_types();
      } else {
        setField_types((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return Boolean.valueOf(isStatus());

    case CON:
      return getCon();

    case NUM_TUPLES:
      return Long.valueOf(getNum_tuples());

    case NUM_MORE_TUPLES:
      return Long.valueOf(getNum_more_tuples());

    case TUPLES:
      return getTuples();

    case FIELD_NAMES:
      return getField_names();

    case FIELD_TYPES:
      return getField_types();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case CON:
      return isSetCon();
    case NUM_TUPLES:
      return isSetNum_tuples();
    case NUM_MORE_TUPLES:
      return isSetNum_more_tuples();
    case TUPLES:
      return isSetTuples();
    case FIELD_NAMES:
      return isSetField_names();
    case FIELD_TYPES:
      return isSetField_types();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ResultSet)
      return this.equals((ResultSet)that);
    return false;
  }

  public boolean equals(ResultSet that) {
    if (that == null)
      return false;

    boolean this_present_status = true;
    boolean that_present_status = true;
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (this.status != that.status)
        return false;
    }

    boolean this_present_con = true && this.isSetCon();
    boolean that_present_con = true && that.isSetCon();
    if (this_present_con || that_present_con) {
      if (!(this_present_con && that_present_con))
        return false;
      if (!this.con.equals(that.con))
        return false;
    }

    boolean this_present_num_tuples = true && this.isSetNum_tuples();
    boolean that_present_num_tuples = true && that.isSetNum_tuples();
    if (this_present_num_tuples || that_present_num_tuples) {
      if (!(this_present_num_tuples && that_present_num_tuples))
        return false;
      if (this.num_tuples != that.num_tuples)
        return false;
    }

    boolean this_present_num_more_tuples = true && this.isSetNum_more_tuples();
    boolean that_present_num_more_tuples = true && that.isSetNum_more_tuples();
    if (this_present_num_more_tuples || that_present_num_more_tuples) {
      if (!(this_present_num_more_tuples && that_present_num_more_tuples))
        return false;
      if (this.num_more_tuples != that.num_more_tuples)
        return false;
    }

    boolean this_present_tuples = true && this.isSetTuples();
    boolean that_present_tuples = true && that.isSetTuples();
    if (this_present_tuples || that_present_tuples) {
      if (!(this_present_tuples && that_present_tuples))
        return false;
      if (!this.tuples.equals(that.tuples))
        return false;
    }

    boolean this_present_field_names = true && this.isSetField_names();
    boolean that_present_field_names = true && that.isSetField_names();
    if (this_present_field_names || that_present_field_names) {
      if (!(this_present_field_names && that_present_field_names))
        return false;
      if (!this.field_names.equals(that.field_names))
        return false;
    }

    boolean this_present_field_types = true && this.isSetField_types();
    boolean that_present_field_types = true && that.isSetField_types();
    if (this_present_field_types || that_present_field_types) {
      if (!(this_present_field_types && that_present_field_types))
        return false;
      if (!this.field_types.equals(that.field_types))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(ResultSet other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCon()).compareTo(other.isSetCon());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCon()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.con, other.con);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNum_tuples()).compareTo(other.isSetNum_tuples());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNum_tuples()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.num_tuples, other.num_tuples);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNum_more_tuples()).compareTo(other.isSetNum_more_tuples());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNum_more_tuples()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.num_more_tuples, other.num_more_tuples);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTuples()).compareTo(other.isSetTuples());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTuples()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tuples, other.tuples);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetField_names()).compareTo(other.isSetField_names());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetField_names()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.field_names, other.field_names);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetField_types()).compareTo(other.isSetField_types());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetField_types()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.field_types, other.field_types);
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
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ResultSet(");
    boolean first = true;

    sb.append("status:");
    sb.append(this.status);
    first = false;
    if (isSetCon()) {
      if (!first) sb.append(", ");
      sb.append("con:");
      if (this.con == null) {
        sb.append("null");
      } else {
        sb.append(this.con);
      }
      first = false;
    }
    if (isSetNum_tuples()) {
      if (!first) sb.append(", ");
      sb.append("num_tuples:");
      sb.append(this.num_tuples);
      first = false;
    }
    if (isSetNum_more_tuples()) {
      if (!first) sb.append(", ");
      sb.append("num_more_tuples:");
      sb.append(this.num_more_tuples);
      first = false;
    }
    if (isSetTuples()) {
      if (!first) sb.append(", ");
      sb.append("tuples:");
      if (this.tuples == null) {
        sb.append("null");
      } else {
        sb.append(this.tuples);
      }
      first = false;
    }
    if (isSetField_names()) {
      if (!first) sb.append(", ");
      sb.append("field_names:");
      if (this.field_names == null) {
        sb.append("null");
      } else {
        sb.append(this.field_names);
      }
      first = false;
    }
    if (isSetField_types()) {
      if (!first) sb.append(", ");
      sb.append("field_types:");
      if (this.field_types == null) {
        sb.append("null");
      } else {
        sb.append(this.field_types);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'status' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (con != null) {
      con.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ResultSetStandardSchemeFactory implements SchemeFactory {
    public ResultSetStandardScheme getScheme() {
      return new ResultSetStandardScheme();
    }
  }

  private static class ResultSetStandardScheme extends StandardScheme<ResultSet> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ResultSet struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.status = iprot.readBool();
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CON
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.con = new Connection();
              struct.con.read(iprot);
              struct.setConIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // NUM_TUPLES
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.num_tuples = iprot.readI64();
              struct.setNum_tuplesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // NUM_MORE_TUPLES
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.num_more_tuples = iprot.readI64();
              struct.setNum_more_tuplesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // TUPLES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.tuples = new ArrayList<Tuple>(_list8.size);
                for (int _i9 = 0; _i9 < _list8.size; ++_i9)
                {
                  Tuple _elem10;
                  _elem10 = new Tuple();
                  _elem10.read(iprot);
                  struct.tuples.add(_elem10);
                }
                iprot.readListEnd();
              }
              struct.setTuplesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // FIELD_NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list11 = iprot.readListBegin();
                struct.field_names = new ArrayList<String>(_list11.size);
                for (int _i12 = 0; _i12 < _list11.size; ++_i12)
                {
                  String _elem13;
                  _elem13 = iprot.readString();
                  struct.field_names.add(_elem13);
                }
                iprot.readListEnd();
              }
              struct.setField_namesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // FIELD_TYPES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list14 = iprot.readListBegin();
                struct.field_types = new ArrayList<String>(_list14.size);
                for (int _i15 = 0; _i15 < _list14.size; ++_i15)
                {
                  String _elem16;
                  _elem16 = iprot.readString();
                  struct.field_types.add(_elem16);
                }
                iprot.readListEnd();
              }
              struct.setField_typesIsSet(true);
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
      if (!struct.isSetStatus()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'status' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ResultSet struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATUS_FIELD_DESC);
      oprot.writeBool(struct.status);
      oprot.writeFieldEnd();
      if (struct.con != null) {
        if (struct.isSetCon()) {
          oprot.writeFieldBegin(CON_FIELD_DESC);
          struct.con.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetNum_tuples()) {
        oprot.writeFieldBegin(NUM_TUPLES_FIELD_DESC);
        oprot.writeI64(struct.num_tuples);
        oprot.writeFieldEnd();
      }
      if (struct.isSetNum_more_tuples()) {
        oprot.writeFieldBegin(NUM_MORE_TUPLES_FIELD_DESC);
        oprot.writeI64(struct.num_more_tuples);
        oprot.writeFieldEnd();
      }
      if (struct.tuples != null) {
        if (struct.isSetTuples()) {
          oprot.writeFieldBegin(TUPLES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.tuples.size()));
            for (Tuple _iter17 : struct.tuples)
            {
              _iter17.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.field_names != null) {
        if (struct.isSetField_names()) {
          oprot.writeFieldBegin(FIELD_NAMES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.field_names.size()));
            for (String _iter18 : struct.field_names)
            {
              oprot.writeString(_iter18);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.field_types != null) {
        if (struct.isSetField_types()) {
          oprot.writeFieldBegin(FIELD_TYPES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.field_types.size()));
            for (String _iter19 : struct.field_types)
            {
              oprot.writeString(_iter19);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResultSetTupleSchemeFactory implements SchemeFactory {
    public ResultSetTupleScheme getScheme() {
      return new ResultSetTupleScheme();
    }
  }

  private static class ResultSetTupleScheme extends TupleScheme<ResultSet> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ResultSet struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeBool(struct.status);
      BitSet optionals = new BitSet();
      if (struct.isSetCon()) {
        optionals.set(0);
      }
      if (struct.isSetNum_tuples()) {
        optionals.set(1);
      }
      if (struct.isSetNum_more_tuples()) {
        optionals.set(2);
      }
      if (struct.isSetTuples()) {
        optionals.set(3);
      }
      if (struct.isSetField_names()) {
        optionals.set(4);
      }
      if (struct.isSetField_types()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetCon()) {
        struct.con.write(oprot);
      }
      if (struct.isSetNum_tuples()) {
        oprot.writeI64(struct.num_tuples);
      }
      if (struct.isSetNum_more_tuples()) {
        oprot.writeI64(struct.num_more_tuples);
      }
      if (struct.isSetTuples()) {
        {
          oprot.writeI32(struct.tuples.size());
          for (Tuple _iter20 : struct.tuples)
          {
            _iter20.write(oprot);
          }
        }
      }
      if (struct.isSetField_names()) {
        {
          oprot.writeI32(struct.field_names.size());
          for (String _iter21 : struct.field_names)
          {
            oprot.writeString(_iter21);
          }
        }
      }
      if (struct.isSetField_types()) {
        {
          oprot.writeI32(struct.field_types.size());
          for (String _iter22 : struct.field_types)
          {
            oprot.writeString(_iter22);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ResultSet struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.status = iprot.readBool();
      struct.setStatusIsSet(true);
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.con = new Connection();
        struct.con.read(iprot);
        struct.setConIsSet(true);
      }
      if (incoming.get(1)) {
        struct.num_tuples = iprot.readI64();
        struct.setNum_tuplesIsSet(true);
      }
      if (incoming.get(2)) {
        struct.num_more_tuples = iprot.readI64();
        struct.setNum_more_tuplesIsSet(true);
      }
      if (incoming.get(3)) {
        {
          org.apache.thrift.protocol.TList _list23 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.tuples = new ArrayList<Tuple>(_list23.size);
          for (int _i24 = 0; _i24 < _list23.size; ++_i24)
          {
            Tuple _elem25;
            _elem25 = new Tuple();
            _elem25.read(iprot);
            struct.tuples.add(_elem25);
          }
        }
        struct.setTuplesIsSet(true);
      }
      if (incoming.get(4)) {
        {
          org.apache.thrift.protocol.TList _list26 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.field_names = new ArrayList<String>(_list26.size);
          for (int _i27 = 0; _i27 < _list26.size; ++_i27)
          {
            String _elem28;
            _elem28 = iprot.readString();
            struct.field_names.add(_elem28);
          }
        }
        struct.setField_namesIsSet(true);
      }
      if (incoming.get(5)) {
        {
          org.apache.thrift.protocol.TList _list29 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.field_types = new ArrayList<String>(_list29.size);
          for (int _i30 = 0; _i30 < _list29.size; ++_i30)
          {
            String _elem31;
            _elem31 = iprot.readString();
            struct.field_types.add(_elem31);
          }
        }
        struct.setField_typesIsSet(true);
      }
    }
  }

}

