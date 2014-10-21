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

public class Connection implements org.apache.thrift.TBase<Connection, Connection._Fields>, java.io.Serializable, Cloneable, Comparable<Connection> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Connection");

  private static final org.apache.thrift.protocol.TField CLIENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("client_id", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField SEQ_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("seq_id", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField USER_FIELD_DESC = new org.apache.thrift.protocol.TField("user", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField REPO_BASE_FIELD_DESC = new org.apache.thrift.protocol.TField("repo_base", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField CURSOR_FIELD_DESC = new org.apache.thrift.protocol.TField("cursor", org.apache.thrift.protocol.TType.I64, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ConnectionStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ConnectionTupleSchemeFactory());
  }

  public String client_id; // optional
  public String seq_id; // optional
  public String user; // optional
  public String repo_base; // optional
  public long cursor; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CLIENT_ID((short)1, "client_id"),
    SEQ_ID((short)2, "seq_id"),
    USER((short)3, "user"),
    REPO_BASE((short)4, "repo_base"),
    CURSOR((short)5, "cursor");

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
        case 1: // CLIENT_ID
          return CLIENT_ID;
        case 2: // SEQ_ID
          return SEQ_ID;
        case 3: // USER
          return USER;
        case 4: // REPO_BASE
          return REPO_BASE;
        case 5: // CURSOR
          return CURSOR;
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
  private static final int __CURSOR_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.CLIENT_ID,_Fields.SEQ_ID,_Fields.USER,_Fields.REPO_BASE,_Fields.CURSOR};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CLIENT_ID, new org.apache.thrift.meta_data.FieldMetaData("client_id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SEQ_ID, new org.apache.thrift.meta_data.FieldMetaData("seq_id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.USER, new org.apache.thrift.meta_data.FieldMetaData("user", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.REPO_BASE, new org.apache.thrift.meta_data.FieldMetaData("repo_base", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CURSOR, new org.apache.thrift.meta_data.FieldMetaData("cursor", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Connection.class, metaDataMap);
  }

  public Connection() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Connection(Connection other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetClient_id()) {
      this.client_id = other.client_id;
    }
    if (other.isSetSeq_id()) {
      this.seq_id = other.seq_id;
    }
    if (other.isSetUser()) {
      this.user = other.user;
    }
    if (other.isSetRepo_base()) {
      this.repo_base = other.repo_base;
    }
    this.cursor = other.cursor;
  }

  public Connection deepCopy() {
    return new Connection(this);
  }

  @Override
  public void clear() {
    this.client_id = null;
    this.seq_id = null;
    this.user = null;
    this.repo_base = null;
    setCursorIsSet(false);
    this.cursor = 0;
  }

  public String getClient_id() {
    return this.client_id;
  }

  public Connection setClient_id(String client_id) {
    this.client_id = client_id;
    return this;
  }

  public void unsetClient_id() {
    this.client_id = null;
  }

  /** Returns true if field client_id is set (has been assigned a value) and false otherwise */
  public boolean isSetClient_id() {
    return this.client_id != null;
  }

  public void setClient_idIsSet(boolean value) {
    if (!value) {
      this.client_id = null;
    }
  }

  public String getSeq_id() {
    return this.seq_id;
  }

  public Connection setSeq_id(String seq_id) {
    this.seq_id = seq_id;
    return this;
  }

  public void unsetSeq_id() {
    this.seq_id = null;
  }

  /** Returns true if field seq_id is set (has been assigned a value) and false otherwise */
  public boolean isSetSeq_id() {
    return this.seq_id != null;
  }

  public void setSeq_idIsSet(boolean value) {
    if (!value) {
      this.seq_id = null;
    }
  }

  public String getUser() {
    return this.user;
  }

  public Connection setUser(String user) {
    this.user = user;
    return this;
  }

  public void unsetUser() {
    this.user = null;
  }

  /** Returns true if field user is set (has been assigned a value) and false otherwise */
  public boolean isSetUser() {
    return this.user != null;
  }

  public void setUserIsSet(boolean value) {
    if (!value) {
      this.user = null;
    }
  }

  public String getRepo_base() {
    return this.repo_base;
  }

  public Connection setRepo_base(String repo_base) {
    this.repo_base = repo_base;
    return this;
  }

  public void unsetRepo_base() {
    this.repo_base = null;
  }

  /** Returns true if field repo_base is set (has been assigned a value) and false otherwise */
  public boolean isSetRepo_base() {
    return this.repo_base != null;
  }

  public void setRepo_baseIsSet(boolean value) {
    if (!value) {
      this.repo_base = null;
    }
  }

  public long getCursor() {
    return this.cursor;
  }

  public Connection setCursor(long cursor) {
    this.cursor = cursor;
    setCursorIsSet(true);
    return this;
  }

  public void unsetCursor() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CURSOR_ISSET_ID);
  }

  /** Returns true if field cursor is set (has been assigned a value) and false otherwise */
  public boolean isSetCursor() {
    return EncodingUtils.testBit(__isset_bitfield, __CURSOR_ISSET_ID);
  }

  public void setCursorIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CURSOR_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CLIENT_ID:
      if (value == null) {
        unsetClient_id();
      } else {
        setClient_id((String)value);
      }
      break;

    case SEQ_ID:
      if (value == null) {
        unsetSeq_id();
      } else {
        setSeq_id((String)value);
      }
      break;

    case USER:
      if (value == null) {
        unsetUser();
      } else {
        setUser((String)value);
      }
      break;

    case REPO_BASE:
      if (value == null) {
        unsetRepo_base();
      } else {
        setRepo_base((String)value);
      }
      break;

    case CURSOR:
      if (value == null) {
        unsetCursor();
      } else {
        setCursor((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CLIENT_ID:
      return getClient_id();

    case SEQ_ID:
      return getSeq_id();

    case USER:
      return getUser();

    case REPO_BASE:
      return getRepo_base();

    case CURSOR:
      return Long.valueOf(getCursor());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CLIENT_ID:
      return isSetClient_id();
    case SEQ_ID:
      return isSetSeq_id();
    case USER:
      return isSetUser();
    case REPO_BASE:
      return isSetRepo_base();
    case CURSOR:
      return isSetCursor();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Connection)
      return this.equals((Connection)that);
    return false;
  }

  public boolean equals(Connection that) {
    if (that == null)
      return false;

    boolean this_present_client_id = true && this.isSetClient_id();
    boolean that_present_client_id = true && that.isSetClient_id();
    if (this_present_client_id || that_present_client_id) {
      if (!(this_present_client_id && that_present_client_id))
        return false;
      if (!this.client_id.equals(that.client_id))
        return false;
    }

    boolean this_present_seq_id = true && this.isSetSeq_id();
    boolean that_present_seq_id = true && that.isSetSeq_id();
    if (this_present_seq_id || that_present_seq_id) {
      if (!(this_present_seq_id && that_present_seq_id))
        return false;
      if (!this.seq_id.equals(that.seq_id))
        return false;
    }

    boolean this_present_user = true && this.isSetUser();
    boolean that_present_user = true && that.isSetUser();
    if (this_present_user || that_present_user) {
      if (!(this_present_user && that_present_user))
        return false;
      if (!this.user.equals(that.user))
        return false;
    }

    boolean this_present_repo_base = true && this.isSetRepo_base();
    boolean that_present_repo_base = true && that.isSetRepo_base();
    if (this_present_repo_base || that_present_repo_base) {
      if (!(this_present_repo_base && that_present_repo_base))
        return false;
      if (!this.repo_base.equals(that.repo_base))
        return false;
    }

    boolean this_present_cursor = true && this.isSetCursor();
    boolean that_present_cursor = true && that.isSetCursor();
    if (this_present_cursor || that_present_cursor) {
      if (!(this_present_cursor && that_present_cursor))
        return false;
      if (this.cursor != that.cursor)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(Connection other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetClient_id()).compareTo(other.isSetClient_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClient_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.client_id, other.client_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSeq_id()).compareTo(other.isSetSeq_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSeq_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.seq_id, other.seq_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUser()).compareTo(other.isSetUser());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUser()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.user, other.user);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRepo_base()).compareTo(other.isSetRepo_base());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRepo_base()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.repo_base, other.repo_base);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCursor()).compareTo(other.isSetCursor());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCursor()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cursor, other.cursor);
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
    StringBuilder sb = new StringBuilder("Connection(");
    boolean first = true;

    if (isSetClient_id()) {
      sb.append("client_id:");
      if (this.client_id == null) {
        sb.append("null");
      } else {
        sb.append(this.client_id);
      }
      first = false;
    }
    if (isSetSeq_id()) {
      if (!first) sb.append(", ");
      sb.append("seq_id:");
      if (this.seq_id == null) {
        sb.append("null");
      } else {
        sb.append(this.seq_id);
      }
      first = false;
    }
    if (isSetUser()) {
      if (!first) sb.append(", ");
      sb.append("user:");
      if (this.user == null) {
        sb.append("null");
      } else {
        sb.append(this.user);
      }
      first = false;
    }
    if (isSetRepo_base()) {
      if (!first) sb.append(", ");
      sb.append("repo_base:");
      if (this.repo_base == null) {
        sb.append("null");
      } else {
        sb.append(this.repo_base);
      }
      first = false;
    }
    if (isSetCursor()) {
      if (!first) sb.append(", ");
      sb.append("cursor:");
      sb.append(this.cursor);
      first = false;
    }
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

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ConnectionStandardSchemeFactory implements SchemeFactory {
    public ConnectionStandardScheme getScheme() {
      return new ConnectionStandardScheme();
    }
  }

  private static class ConnectionStandardScheme extends StandardScheme<Connection> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Connection struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CLIENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.client_id = iprot.readString();
              struct.setClient_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SEQ_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.seq_id = iprot.readString();
              struct.setSeq_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.user = iprot.readString();
              struct.setUserIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // REPO_BASE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.repo_base = iprot.readString();
              struct.setRepo_baseIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CURSOR
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.cursor = iprot.readI64();
              struct.setCursorIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Connection struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.client_id != null) {
        if (struct.isSetClient_id()) {
          oprot.writeFieldBegin(CLIENT_ID_FIELD_DESC);
          oprot.writeString(struct.client_id);
          oprot.writeFieldEnd();
        }
      }
      if (struct.seq_id != null) {
        if (struct.isSetSeq_id()) {
          oprot.writeFieldBegin(SEQ_ID_FIELD_DESC);
          oprot.writeString(struct.seq_id);
          oprot.writeFieldEnd();
        }
      }
      if (struct.user != null) {
        if (struct.isSetUser()) {
          oprot.writeFieldBegin(USER_FIELD_DESC);
          oprot.writeString(struct.user);
          oprot.writeFieldEnd();
        }
      }
      if (struct.repo_base != null) {
        if (struct.isSetRepo_base()) {
          oprot.writeFieldBegin(REPO_BASE_FIELD_DESC);
          oprot.writeString(struct.repo_base);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetCursor()) {
        oprot.writeFieldBegin(CURSOR_FIELD_DESC);
        oprot.writeI64(struct.cursor);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ConnectionTupleSchemeFactory implements SchemeFactory {
    public ConnectionTupleScheme getScheme() {
      return new ConnectionTupleScheme();
    }
  }

  private static class ConnectionTupleScheme extends TupleScheme<Connection> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Connection struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetClient_id()) {
        optionals.set(0);
      }
      if (struct.isSetSeq_id()) {
        optionals.set(1);
      }
      if (struct.isSetUser()) {
        optionals.set(2);
      }
      if (struct.isSetRepo_base()) {
        optionals.set(3);
      }
      if (struct.isSetCursor()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetClient_id()) {
        oprot.writeString(struct.client_id);
      }
      if (struct.isSetSeq_id()) {
        oprot.writeString(struct.seq_id);
      }
      if (struct.isSetUser()) {
        oprot.writeString(struct.user);
      }
      if (struct.isSetRepo_base()) {
        oprot.writeString(struct.repo_base);
      }
      if (struct.isSetCursor()) {
        oprot.writeI64(struct.cursor);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Connection struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.client_id = iprot.readString();
        struct.setClient_idIsSet(true);
      }
      if (incoming.get(1)) {
        struct.seq_id = iprot.readString();
        struct.setSeq_idIsSet(true);
      }
      if (incoming.get(2)) {
        struct.user = iprot.readString();
        struct.setUserIsSet(true);
      }
      if (incoming.get(3)) {
        struct.repo_base = iprot.readString();
        struct.setRepo_baseIsSet(true);
      }
      if (incoming.get(4)) {
        struct.cursor = iprot.readI64();
        struct.setCursorIsSet(true);
      }
    }
  }

}

