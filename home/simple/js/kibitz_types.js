//
// Autogenerated by Thrift Compiler (0.9.1)
//
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
//


if (typeof kibitz === 'undefined') {
  kibitz = {};
}
kibitz.Item = function(args) {
  this.attributes = null;
  this.kibitz_generated_id = null;
  if (args) {
    if (args.attributes !== undefined) {
      this.attributes = args.attributes;
    }
    if (args.kibitz_generated_id !== undefined) {
      this.kibitz_generated_id = args.kibitz_generated_id;
    }
  }
};
kibitz.Item.prototype = {};
kibitz.Item.prototype.read = function(input) {
  input.readStructBegin();
  while (true)
  {
    var ret = input.readFieldBegin();
    var fname = ret.fname;
    var ftype = ret.ftype;
    var fid = ret.fid;
    if (ftype == Thrift.Type.STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
      if (ftype == Thrift.Type.MAP) {
        var _size0 = 0;
        var _rtmp34;
        this.attributes = {};
        var _ktype1 = 0;
        var _vtype2 = 0;
        _rtmp34 = input.readMapBegin();
        _ktype1 = _rtmp34.ktype;
        _vtype2 = _rtmp34.vtype;
        _size0 = _rtmp34.size;
        for (var _i5 = 0; _i5 < _size0; ++_i5)
        {
          if (_i5 > 0 ) {
            if (input.rstack.length > input.rpos[input.rpos.length -1] + 1) {
              input.rstack.pop();
            }
          }
          var key6 = null;
          var val7 = null;
          key6 = input.readString().value;
          val7 = input.readString().value;
          this.attributes[key6] = val7;
        }
        input.readMapEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.I64) {
        this.kibitz_generated_id = input.readI64().value;
      } else {
        input.skip(ftype);
      }
      break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.Item.prototype.write = function(output) {
  output.writeStructBegin('Item');
  if (this.attributes !== null && this.attributes !== undefined) {
    output.writeFieldBegin('attributes', Thrift.Type.MAP, 1);
    output.writeMapBegin(Thrift.Type.STRING, Thrift.Type.STRING, Thrift.objectLength(this.attributes));
    for (var kiter8 in this.attributes)
    {
      if (this.attributes.hasOwnProperty(kiter8))
      {
        var viter9 = this.attributes[kiter8];
        output.writeString(kiter8);
        output.writeString(viter9);
      }
    }
    output.writeMapEnd();
    output.writeFieldEnd();
  }
  if (this.kibitz_generated_id !== null && this.kibitz_generated_id !== undefined) {
    output.writeFieldBegin('kibitz_generated_id', Thrift.Type.I64, 2);
    output.writeI64(this.kibitz_generated_id);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.Recommender = function(args) {
  this.username = null;
  this.recommenderName = null;
  this.clientKey = null;
  this.homepage = null;
  this.repoName = null;
  this.title = null;
  this.description = null;
  this.image = null;
  this.video = null;
  this.itemTypes = null;
  this.displayItems = null;
  this.numRecs = null;
  this.maxRatingVal = null;
  if (args) {
    if (args.username !== undefined) {
      this.username = args.username;
    }
    if (args.recommenderName !== undefined) {
      this.recommenderName = args.recommenderName;
    }
    if (args.clientKey !== undefined) {
      this.clientKey = args.clientKey;
    }
    if (args.homepage !== undefined) {
      this.homepage = args.homepage;
    }
    if (args.repoName !== undefined) {
      this.repoName = args.repoName;
    }
    if (args.title !== undefined) {
      this.title = args.title;
    }
    if (args.description !== undefined) {
      this.description = args.description;
    }
    if (args.image !== undefined) {
      this.image = args.image;
    }
    if (args.video !== undefined) {
      this.video = args.video;
    }
    if (args.itemTypes !== undefined) {
      this.itemTypes = args.itemTypes;
    }
    if (args.displayItems !== undefined) {
      this.displayItems = args.displayItems;
    }
    if (args.numRecs !== undefined) {
      this.numRecs = args.numRecs;
    }
    if (args.maxRatingVal !== undefined) {
      this.maxRatingVal = args.maxRatingVal;
    }
  }
};
kibitz.Recommender.prototype = {};
kibitz.Recommender.prototype.read = function(input) {
  input.readStructBegin();
  while (true)
  {
    var ret = input.readFieldBegin();
    var fname = ret.fname;
    var ftype = ret.ftype;
    var fid = ret.fid;
    if (ftype == Thrift.Type.STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
      if (ftype == Thrift.Type.STRING) {
        this.username = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.STRING) {
        this.recommenderName = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 3:
      if (ftype == Thrift.Type.STRING) {
        this.clientKey = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 4:
      if (ftype == Thrift.Type.STRING) {
        this.homepage = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 5:
      if (ftype == Thrift.Type.STRING) {
        this.repoName = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 6:
      if (ftype == Thrift.Type.STRING) {
        this.title = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 7:
      if (ftype == Thrift.Type.STRING) {
        this.description = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 8:
      if (ftype == Thrift.Type.STRING) {
        this.image = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 9:
      if (ftype == Thrift.Type.STRING) {
        this.video = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 10:
      if (ftype == Thrift.Type.MAP) {
        var _size10 = 0;
        var _rtmp314;
        this.itemTypes = {};
        var _ktype11 = 0;
        var _vtype12 = 0;
        _rtmp314 = input.readMapBegin();
        _ktype11 = _rtmp314.ktype;
        _vtype12 = _rtmp314.vtype;
        _size10 = _rtmp314.size;
        for (var _i15 = 0; _i15 < _size10; ++_i15)
        {
          if (_i15 > 0 ) {
            if (input.rstack.length > input.rpos[input.rpos.length -1] + 1) {
              input.rstack.pop();
            }
          }
          var key16 = null;
          var val17 = null;
          key16 = input.readString().value;
          val17 = input.readString().value;
          this.itemTypes[key16] = val17;
        }
        input.readMapEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 11:
      if (ftype == Thrift.Type.LIST) {
        var _size18 = 0;
        var _rtmp322;
        this.displayItems = [];
        var _etype21 = 0;
        _rtmp322 = input.readListBegin();
        _etype21 = _rtmp322.etype;
        _size18 = _rtmp322.size;
        for (var _i23 = 0; _i23 < _size18; ++_i23)
        {
          var elem24 = null;
          elem24 = input.readString().value;
          this.displayItems.push(elem24);
        }
        input.readListEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 12:
      if (ftype == Thrift.Type.I64) {
        this.numRecs = input.readI64().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 13:
      if (ftype == Thrift.Type.I64) {
        this.maxRatingVal = input.readI64().value;
      } else {
        input.skip(ftype);
      }
      break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.Recommender.prototype.write = function(output) {
  output.writeStructBegin('Recommender');
  if (this.username !== null && this.username !== undefined) {
    output.writeFieldBegin('username', Thrift.Type.STRING, 1);
    output.writeString(this.username);
    output.writeFieldEnd();
  }
  if (this.recommenderName !== null && this.recommenderName !== undefined) {
    output.writeFieldBegin('recommenderName', Thrift.Type.STRING, 2);
    output.writeString(this.recommenderName);
    output.writeFieldEnd();
  }
  if (this.clientKey !== null && this.clientKey !== undefined) {
    output.writeFieldBegin('clientKey', Thrift.Type.STRING, 3);
    output.writeString(this.clientKey);
    output.writeFieldEnd();
  }
  if (this.homepage !== null && this.homepage !== undefined) {
    output.writeFieldBegin('homepage', Thrift.Type.STRING, 4);
    output.writeString(this.homepage);
    output.writeFieldEnd();
  }
  if (this.repoName !== null && this.repoName !== undefined) {
    output.writeFieldBegin('repoName', Thrift.Type.STRING, 5);
    output.writeString(this.repoName);
    output.writeFieldEnd();
  }
  if (this.title !== null && this.title !== undefined) {
    output.writeFieldBegin('title', Thrift.Type.STRING, 6);
    output.writeString(this.title);
    output.writeFieldEnd();
  }
  if (this.description !== null && this.description !== undefined) {
    output.writeFieldBegin('description', Thrift.Type.STRING, 7);
    output.writeString(this.description);
    output.writeFieldEnd();
  }
  if (this.image !== null && this.image !== undefined) {
    output.writeFieldBegin('image', Thrift.Type.STRING, 8);
    output.writeString(this.image);
    output.writeFieldEnd();
  }
  if (this.video !== null && this.video !== undefined) {
    output.writeFieldBegin('video', Thrift.Type.STRING, 9);
    output.writeString(this.video);
    output.writeFieldEnd();
  }
  if (this.itemTypes !== null && this.itemTypes !== undefined) {
    output.writeFieldBegin('itemTypes', Thrift.Type.MAP, 10);
    output.writeMapBegin(Thrift.Type.STRING, Thrift.Type.STRING, Thrift.objectLength(this.itemTypes));
    for (var kiter25 in this.itemTypes)
    {
      if (this.itemTypes.hasOwnProperty(kiter25))
      {
        var viter26 = this.itemTypes[kiter25];
        output.writeString(kiter25);
        output.writeString(viter26);
      }
    }
    output.writeMapEnd();
    output.writeFieldEnd();
  }
  if (this.displayItems !== null && this.displayItems !== undefined) {
    output.writeFieldBegin('displayItems', Thrift.Type.LIST, 11);
    output.writeListBegin(Thrift.Type.STRING, this.displayItems.length);
    for (var iter27 in this.displayItems)
    {
      if (this.displayItems.hasOwnProperty(iter27))
      {
        iter27 = this.displayItems[iter27];
        output.writeString(iter27);
      }
    }
    output.writeListEnd();
    output.writeFieldEnd();
  }
  if (this.numRecs !== null && this.numRecs !== undefined) {
    output.writeFieldBegin('numRecs', Thrift.Type.I64, 12);
    output.writeI64(this.numRecs);
    output.writeFieldEnd();
  }
  if (this.maxRatingVal !== null && this.maxRatingVal !== undefined) {
    output.writeFieldBegin('maxRatingVal', Thrift.Type.I64, 13);
    output.writeI64(this.maxRatingVal);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

