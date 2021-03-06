#
# Autogenerated by Thrift Compiler (0.9.2)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TException, TApplicationException

from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol, TProtocol
try:
  from thrift.protocol import fastbinary
except:
  fastbinary = None



class Item:
  """
  Attributes:
   - attributes
   - kibitz_generated_id
   - confidence
   - predictedPreferences
  """

  thrift_spec = (
    None, # 0
    (1, TType.MAP, 'attributes', (TType.STRING,None,TType.STRING,None), None, ), # 1
    (2, TType.I64, 'kibitz_generated_id', None, None, ), # 2
    (3, TType.I32, 'confidence', None, None, ), # 3
    (4, TType.DOUBLE, 'predictedPreferences', None, None, ), # 4
  )

  def __init__(self, attributes=None, kibitz_generated_id=None, confidence=None, predictedPreferences=None,):
    self.attributes = attributes
    self.kibitz_generated_id = kibitz_generated_id
    self.confidence = confidence
    self.predictedPreferences = predictedPreferences

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.MAP:
          self.attributes = {}
          (_ktype1, _vtype2, _size0 ) = iprot.readMapBegin()
          for _i4 in xrange(_size0):
            _key5 = iprot.readString();
            _val6 = iprot.readString();
            self.attributes[_key5] = _val6
          iprot.readMapEnd()
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.I64:
          self.kibitz_generated_id = iprot.readI64();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.I32:
          self.confidence = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.DOUBLE:
          self.predictedPreferences = iprot.readDouble();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Item')
    if self.attributes is not None:
      oprot.writeFieldBegin('attributes', TType.MAP, 1)
      oprot.writeMapBegin(TType.STRING, TType.STRING, len(self.attributes))
      for kiter7,viter8 in self.attributes.items():
        oprot.writeString(kiter7)
        oprot.writeString(viter8)
      oprot.writeMapEnd()
      oprot.writeFieldEnd()
    if self.kibitz_generated_id is not None:
      oprot.writeFieldBegin('kibitz_generated_id', TType.I64, 2)
      oprot.writeI64(self.kibitz_generated_id)
      oprot.writeFieldEnd()
    if self.confidence is not None:
      oprot.writeFieldBegin('confidence', TType.I32, 3)
      oprot.writeI32(self.confidence)
      oprot.writeFieldEnd()
    if self.predictedPreferences is not None:
      oprot.writeFieldBegin('predictedPreferences', TType.DOUBLE, 4)
      oprot.writeDouble(self.predictedPreferences)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    if self.attributes is None:
      raise TProtocol.TProtocolException(message='Required field attributes is unset!')
    if self.kibitz_generated_id is None:
      raise TProtocol.TProtocolException(message='Required field kibitz_generated_id is unset!')
    if self.confidence is None:
      raise TProtocol.TProtocolException(message='Required field confidence is unset!')
    if self.predictedPreferences is None:
      raise TProtocol.TProtocolException(message='Required field predictedPreferences is unset!')
    return


  def __hash__(self):
    value = 17
    value = (value * 31) ^ hash(self.attributes)
    value = (value * 31) ^ hash(self.kibitz_generated_id)
    value = (value * 31) ^ hash(self.confidence)
    value = (value * 31) ^ hash(self.predictedPreferences)
    return value

  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)

class Recommender:
  """
  Attributes:
   - username
   - recommenderName
   - clientKey
   - homepage
   - repoName
   - title
   - description
   - image
   - video
   - itemTypes
   - displayItems
   - numRecs
   - maxRatingVal
   - ratingsColumn
   - primaryKey
  """

  thrift_spec = (
    None, # 0
    (1, TType.STRING, 'username', None, None, ), # 1
    (2, TType.STRING, 'recommenderName', None, None, ), # 2
    (3, TType.STRING, 'clientKey', None, None, ), # 3
    (4, TType.STRING, 'homepage', None, None, ), # 4
    (5, TType.STRING, 'repoName', None, None, ), # 5
    (6, TType.STRING, 'title', None, None, ), # 6
    (7, TType.STRING, 'description', None, None, ), # 7
    (8, TType.STRING, 'image', None, None, ), # 8
    (9, TType.STRING, 'video', None, None, ), # 9
    (10, TType.MAP, 'itemTypes', (TType.STRING,None,TType.STRING,None), None, ), # 10
    (11, TType.LIST, 'displayItems', (TType.STRING,None), None, ), # 11
    (12, TType.I64, 'numRecs', None, None, ), # 12
    (13, TType.I64, 'maxRatingVal', None, None, ), # 13
    (14, TType.STRING, 'ratingsColumn', None, None, ), # 14
    (15, TType.STRING, 'primaryKey', None, None, ), # 15
  )

  def __init__(self, username=None, recommenderName=None, clientKey=None, homepage=None, repoName=None, title=None, description=None, image=None, video=None, itemTypes=None, displayItems=None, numRecs=None, maxRatingVal=None, ratingsColumn=None, primaryKey=None,):
    self.username = username
    self.recommenderName = recommenderName
    self.clientKey = clientKey
    self.homepage = homepage
    self.repoName = repoName
    self.title = title
    self.description = description
    self.image = image
    self.video = video
    self.itemTypes = itemTypes
    self.displayItems = displayItems
    self.numRecs = numRecs
    self.maxRatingVal = maxRatingVal
    self.ratingsColumn = ratingsColumn
    self.primaryKey = primaryKey

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.STRING:
          self.username = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.recommenderName = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.STRING:
          self.clientKey = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.STRING:
          self.homepage = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 5:
        if ftype == TType.STRING:
          self.repoName = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 6:
        if ftype == TType.STRING:
          self.title = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 7:
        if ftype == TType.STRING:
          self.description = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 8:
        if ftype == TType.STRING:
          self.image = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 9:
        if ftype == TType.STRING:
          self.video = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 10:
        if ftype == TType.MAP:
          self.itemTypes = {}
          (_ktype10, _vtype11, _size9 ) = iprot.readMapBegin()
          for _i13 in xrange(_size9):
            _key14 = iprot.readString();
            _val15 = iprot.readString();
            self.itemTypes[_key14] = _val15
          iprot.readMapEnd()
        else:
          iprot.skip(ftype)
      elif fid == 11:
        if ftype == TType.LIST:
          self.displayItems = []
          (_etype19, _size16) = iprot.readListBegin()
          for _i20 in xrange(_size16):
            _elem21 = iprot.readString();
            self.displayItems.append(_elem21)
          iprot.readListEnd()
        else:
          iprot.skip(ftype)
      elif fid == 12:
        if ftype == TType.I64:
          self.numRecs = iprot.readI64();
        else:
          iprot.skip(ftype)
      elif fid == 13:
        if ftype == TType.I64:
          self.maxRatingVal = iprot.readI64();
        else:
          iprot.skip(ftype)
      elif fid == 14:
        if ftype == TType.STRING:
          self.ratingsColumn = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 15:
        if ftype == TType.STRING:
          self.primaryKey = iprot.readString();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Recommender')
    if self.username is not None:
      oprot.writeFieldBegin('username', TType.STRING, 1)
      oprot.writeString(self.username)
      oprot.writeFieldEnd()
    if self.recommenderName is not None:
      oprot.writeFieldBegin('recommenderName', TType.STRING, 2)
      oprot.writeString(self.recommenderName)
      oprot.writeFieldEnd()
    if self.clientKey is not None:
      oprot.writeFieldBegin('clientKey', TType.STRING, 3)
      oprot.writeString(self.clientKey)
      oprot.writeFieldEnd()
    if self.homepage is not None:
      oprot.writeFieldBegin('homepage', TType.STRING, 4)
      oprot.writeString(self.homepage)
      oprot.writeFieldEnd()
    if self.repoName is not None:
      oprot.writeFieldBegin('repoName', TType.STRING, 5)
      oprot.writeString(self.repoName)
      oprot.writeFieldEnd()
    if self.title is not None:
      oprot.writeFieldBegin('title', TType.STRING, 6)
      oprot.writeString(self.title)
      oprot.writeFieldEnd()
    if self.description is not None:
      oprot.writeFieldBegin('description', TType.STRING, 7)
      oprot.writeString(self.description)
      oprot.writeFieldEnd()
    if self.image is not None:
      oprot.writeFieldBegin('image', TType.STRING, 8)
      oprot.writeString(self.image)
      oprot.writeFieldEnd()
    if self.video is not None:
      oprot.writeFieldBegin('video', TType.STRING, 9)
      oprot.writeString(self.video)
      oprot.writeFieldEnd()
    if self.itemTypes is not None:
      oprot.writeFieldBegin('itemTypes', TType.MAP, 10)
      oprot.writeMapBegin(TType.STRING, TType.STRING, len(self.itemTypes))
      for kiter22,viter23 in self.itemTypes.items():
        oprot.writeString(kiter22)
        oprot.writeString(viter23)
      oprot.writeMapEnd()
      oprot.writeFieldEnd()
    if self.displayItems is not None:
      oprot.writeFieldBegin('displayItems', TType.LIST, 11)
      oprot.writeListBegin(TType.STRING, len(self.displayItems))
      for iter24 in self.displayItems:
        oprot.writeString(iter24)
      oprot.writeListEnd()
      oprot.writeFieldEnd()
    if self.numRecs is not None:
      oprot.writeFieldBegin('numRecs', TType.I64, 12)
      oprot.writeI64(self.numRecs)
      oprot.writeFieldEnd()
    if self.maxRatingVal is not None:
      oprot.writeFieldBegin('maxRatingVal', TType.I64, 13)
      oprot.writeI64(self.maxRatingVal)
      oprot.writeFieldEnd()
    if self.ratingsColumn is not None:
      oprot.writeFieldBegin('ratingsColumn', TType.STRING, 14)
      oprot.writeString(self.ratingsColumn)
      oprot.writeFieldEnd()
    if self.primaryKey is not None:
      oprot.writeFieldBegin('primaryKey', TType.STRING, 15)
      oprot.writeString(self.primaryKey)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    if self.username is None:
      raise TProtocol.TProtocolException(message='Required field username is unset!')
    if self.recommenderName is None:
      raise TProtocol.TProtocolException(message='Required field recommenderName is unset!')
    if self.clientKey is None:
      raise TProtocol.TProtocolException(message='Required field clientKey is unset!')
    if self.homepage is None:
      raise TProtocol.TProtocolException(message='Required field homepage is unset!')
    if self.repoName is None:
      raise TProtocol.TProtocolException(message='Required field repoName is unset!')
    if self.title is None:
      raise TProtocol.TProtocolException(message='Required field title is unset!')
    if self.description is None:
      raise TProtocol.TProtocolException(message='Required field description is unset!')
    if self.image is None:
      raise TProtocol.TProtocolException(message='Required field image is unset!')
    if self.numRecs is None:
      raise TProtocol.TProtocolException(message='Required field numRecs is unset!')
    if self.maxRatingVal is None:
      raise TProtocol.TProtocolException(message='Required field maxRatingVal is unset!')
    return


  def __hash__(self):
    value = 17
    value = (value * 31) ^ hash(self.username)
    value = (value * 31) ^ hash(self.recommenderName)
    value = (value * 31) ^ hash(self.clientKey)
    value = (value * 31) ^ hash(self.homepage)
    value = (value * 31) ^ hash(self.repoName)
    value = (value * 31) ^ hash(self.title)
    value = (value * 31) ^ hash(self.description)
    value = (value * 31) ^ hash(self.image)
    value = (value * 31) ^ hash(self.video)
    value = (value * 31) ^ hash(self.itemTypes)
    value = (value * 31) ^ hash(self.displayItems)
    value = (value * 31) ^ hash(self.numRecs)
    value = (value * 31) ^ hash(self.maxRatingVal)
    value = (value * 31) ^ hash(self.ratingsColumn)
    value = (value * 31) ^ hash(self.primaryKey)
    return value

  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)
