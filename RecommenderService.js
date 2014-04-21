//
// Autogenerated by Thrift Compiler (0.9.1)
//
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
//


//HELPER FUNCTIONS AND STRUCTURES

kibitz.RecommenderService_makeRecommendation_args = function(args) {
  this.userId = null;
  this.numRecs = null;
  if (args) {
    if (args.userId !== undefined) {
      this.userId = args.userId;
    }
    if (args.numRecs !== undefined) {
      this.numRecs = args.numRecs;
    }
  }
};
kibitz.RecommenderService_makeRecommendation_args.prototype = {};
kibitz.RecommenderService_makeRecommendation_args.prototype.read = function(input) {
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
      if (ftype == Thrift.Type.I32) {
        this.userId = input.readI32().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.I32) {
        this.numRecs = input.readI32().value;
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

kibitz.RecommenderService_makeRecommendation_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_makeRecommendation_args');
  if (this.userId !== null && this.userId !== undefined) {
    output.writeFieldBegin('userId', Thrift.Type.I32, 1);
    output.writeI32(this.userId);
    output.writeFieldEnd();
  }
  if (this.numRecs !== null && this.numRecs !== undefined) {
    output.writeFieldBegin('numRecs', Thrift.Type.I32, 2);
    output.writeI32(this.numRecs);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_makeRecommendation_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_makeRecommendation_result.prototype = {};
kibitz.RecommenderService_makeRecommendation_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.LIST) {
        var _size0 = 0;
        var _rtmp34;
        this.success = [];
        var _etype3 = 0;
        _rtmp34 = input.readListBegin();
        _etype3 = _rtmp34.etype;
        _size0 = _rtmp34.size;
        for (var _i5 = 0; _i5 < _size0; ++_i5)
        {
          var elem6 = null;
          var _size7 = 0;
          var _rtmp311;
          elem6 = [];
          var _etype10 = 0;
          _rtmp311 = input.readListBegin();
          _etype10 = _rtmp311.etype;
          _size7 = _rtmp311.size;
          for (var _i12 = 0; _i12 < _size7; ++_i12)
          {
            var elem13 = null;
            elem13 = input.readString().value;
            elem6.push(elem13);
          }
          input.readListEnd();
          this.success.push(elem6);
        }
        input.readListEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_makeRecommendation_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_makeRecommendation_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.LIST, 0);
    output.writeListBegin(Thrift.Type.LIST, this.success.length);
    for (var iter14 in this.success)
    {
      if (this.success.hasOwnProperty(iter14))
      {
        iter14 = this.success[iter14];
        output.writeListBegin(Thrift.Type.STRING, iter14.length);
        for (var iter15 in iter14)
        {
          if (iter14.hasOwnProperty(iter15))
          {
            iter15 = iter14[iter15];
            output.writeString(iter15);
          }
        }
        output.writeListEnd();
      }
    }
    output.writeListEnd();
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_getItems_args = function(args) {
};
kibitz.RecommenderService_getItems_args.prototype = {};
kibitz.RecommenderService_getItems_args.prototype.read = function(input) {
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
    input.skip(ftype);
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_getItems_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_getItems_args');
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_getItems_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_getItems_result.prototype = {};
kibitz.RecommenderService_getItems_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.LIST) {
        var _size16 = 0;
        var _rtmp320;
        this.success = [];
        var _etype19 = 0;
        _rtmp320 = input.readListBegin();
        _etype19 = _rtmp320.etype;
        _size16 = _rtmp320.size;
        for (var _i21 = 0; _i21 < _size16; ++_i21)
        {
          var elem22 = null;
          var _size23 = 0;
          var _rtmp327;
          elem22 = [];
          var _etype26 = 0;
          _rtmp327 = input.readListBegin();
          _etype26 = _rtmp327.etype;
          _size23 = _rtmp327.size;
          for (var _i28 = 0; _i28 < _size23; ++_i28)
          {
            var elem29 = null;
            elem29 = input.readString().value;
            elem22.push(elem29);
          }
          input.readListEnd();
          this.success.push(elem22);
        }
        input.readListEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_getItems_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_getItems_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.LIST, 0);
    output.writeListBegin(Thrift.Type.LIST, this.success.length);
    for (var iter30 in this.success)
    {
      if (this.success.hasOwnProperty(iter30))
      {
        iter30 = this.success[iter30];
        output.writeListBegin(Thrift.Type.STRING, iter30.length);
        for (var iter31 in iter30)
        {
          if (iter30.hasOwnProperty(iter31))
          {
            iter31 = iter30[iter31];
            output.writeString(iter31);
          }
        }
        output.writeListEnd();
      }
    }
    output.writeListEnd();
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_recordRatings_args = function(args) {
  this.userId = null;
  this.itemId = null;
  this.rating = null;
  if (args) {
    if (args.userId !== undefined) {
      this.userId = args.userId;
    }
    if (args.itemId !== undefined) {
      this.itemId = args.itemId;
    }
    if (args.rating !== undefined) {
      this.rating = args.rating;
    }
  }
};
kibitz.RecommenderService_recordRatings_args.prototype = {};
kibitz.RecommenderService_recordRatings_args.prototype.read = function(input) {
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
      if (ftype == Thrift.Type.I32) {
        this.userId = input.readI32().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.I32) {
        this.itemId = input.readI32().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 3:
      if (ftype == Thrift.Type.I32) {
        this.rating = input.readI32().value;
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

kibitz.RecommenderService_recordRatings_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_recordRatings_args');
  if (this.userId !== null && this.userId !== undefined) {
    output.writeFieldBegin('userId', Thrift.Type.I32, 1);
    output.writeI32(this.userId);
    output.writeFieldEnd();
  }
  if (this.itemId !== null && this.itemId !== undefined) {
    output.writeFieldBegin('itemId', Thrift.Type.I32, 2);
    output.writeI32(this.itemId);
    output.writeFieldEnd();
  }
  if (this.rating !== null && this.rating !== undefined) {
    output.writeFieldBegin('rating', Thrift.Type.I32, 3);
    output.writeI32(this.rating);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_recordRatings_result = function(args) {
};
kibitz.RecommenderService_recordRatings_result.prototype = {};
kibitz.RecommenderService_recordRatings_result.prototype.read = function(input) {
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
    input.skip(ftype);
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_recordRatings_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_recordRatings_result');
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_deleteRatings_args = function(args) {
  this.userId = null;
  this.itemId = null;
  if (args) {
    if (args.userId !== undefined) {
      this.userId = args.userId;
    }
    if (args.itemId !== undefined) {
      this.itemId = args.itemId;
    }
  }
};
kibitz.RecommenderService_deleteRatings_args.prototype = {};
kibitz.RecommenderService_deleteRatings_args.prototype.read = function(input) {
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
      if (ftype == Thrift.Type.I32) {
        this.userId = input.readI32().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.I32) {
        this.itemId = input.readI32().value;
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

kibitz.RecommenderService_deleteRatings_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_deleteRatings_args');
  if (this.userId !== null && this.userId !== undefined) {
    output.writeFieldBegin('userId', Thrift.Type.I32, 1);
    output.writeI32(this.userId);
    output.writeFieldEnd();
  }
  if (this.itemId !== null && this.itemId !== undefined) {
    output.writeFieldBegin('itemId', Thrift.Type.I32, 2);
    output.writeI32(this.itemId);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_deleteRatings_result = function(args) {
};
kibitz.RecommenderService_deleteRatings_result.prototype = {};
kibitz.RecommenderService_deleteRatings_result.prototype.read = function(input) {
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
    input.skip(ftype);
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_deleteRatings_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_deleteRatings_result');
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_createNewUser_args = function(args) {
  this.username = null;
  this.email = null;
  this.password = null;
  if (args) {
    if (args.username !== undefined) {
      this.username = args.username;
    }
    if (args.email !== undefined) {
      this.email = args.email;
    }
    if (args.password !== undefined) {
      this.password = args.password;
    }
  }
};
kibitz.RecommenderService_createNewUser_args.prototype = {};
kibitz.RecommenderService_createNewUser_args.prototype.read = function(input) {
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
        this.email = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 3:
      if (ftype == Thrift.Type.STRING) {
        this.password = input.readString().value;
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

kibitz.RecommenderService_createNewUser_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_createNewUser_args');
  if (this.username !== null && this.username !== undefined) {
    output.writeFieldBegin('username', Thrift.Type.STRING, 1);
    output.writeString(this.username);
    output.writeFieldEnd();
  }
  if (this.email !== null && this.email !== undefined) {
    output.writeFieldBegin('email', Thrift.Type.STRING, 2);
    output.writeString(this.email);
    output.writeFieldEnd();
  }
  if (this.password !== null && this.password !== undefined) {
    output.writeFieldBegin('password', Thrift.Type.STRING, 3);
    output.writeString(this.password);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_createNewUser_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_createNewUser_result.prototype = {};
kibitz.RecommenderService_createNewUser_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.STRING) {
        this.success = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_createNewUser_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_createNewUser_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.STRING, 0);
    output.writeString(this.success);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_checkUsername_args = function(args) {
  this.username = null;
  if (args) {
    if (args.username !== undefined) {
      this.username = args.username;
    }
  }
};
kibitz.RecommenderService_checkUsername_args.prototype = {};
kibitz.RecommenderService_checkUsername_args.prototype.read = function(input) {
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
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_checkUsername_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_checkUsername_args');
  if (this.username !== null && this.username !== undefined) {
    output.writeFieldBegin('username', Thrift.Type.STRING, 1);
    output.writeString(this.username);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_checkUsername_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_checkUsername_result.prototype = {};
kibitz.RecommenderService_checkUsername_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.BOOL) {
        this.success = input.readBool().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_checkUsername_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_checkUsername_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.BOOL, 0);
    output.writeBool(this.success);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_checkLogin_args = function(args) {
  this.username = null;
  this.password = null;
  if (args) {
    if (args.username !== undefined) {
      this.username = args.username;
    }
    if (args.password !== undefined) {
      this.password = args.password;
    }
  }
};
kibitz.RecommenderService_checkLogin_args.prototype = {};
kibitz.RecommenderService_checkLogin_args.prototype.read = function(input) {
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
        this.password = input.readString().value;
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

kibitz.RecommenderService_checkLogin_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_checkLogin_args');
  if (this.username !== null && this.username !== undefined) {
    output.writeFieldBegin('username', Thrift.Type.STRING, 1);
    output.writeString(this.username);
    output.writeFieldEnd();
  }
  if (this.password !== null && this.password !== undefined) {
    output.writeFieldBegin('password', Thrift.Type.STRING, 2);
    output.writeString(this.password);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_checkLogin_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_checkLogin_result.prototype = {};
kibitz.RecommenderService_checkLogin_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.BOOL) {
        this.success = input.readBool().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_checkLogin_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_checkLogin_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.BOOL, 0);
    output.writeBool(this.success);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_getUserRatedItems_args = function(args) {
  this.userId = null;
  if (args) {
    if (args.userId !== undefined) {
      this.userId = args.userId;
    }
  }
};
kibitz.RecommenderService_getUserRatedItems_args.prototype = {};
kibitz.RecommenderService_getUserRatedItems_args.prototype.read = function(input) {
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
      if (ftype == Thrift.Type.I32) {
        this.userId = input.readI32().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_getUserRatedItems_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_getUserRatedItems_args');
  if (this.userId !== null && this.userId !== undefined) {
    output.writeFieldBegin('userId', Thrift.Type.I32, 1);
    output.writeI32(this.userId);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_getUserRatedItems_result = function(args) {
  this.success = null;
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
  }
};
kibitz.RecommenderService_getUserRatedItems_result.prototype = {};
kibitz.RecommenderService_getUserRatedItems_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.LIST) {
        var _size32 = 0;
        var _rtmp336;
        this.success = [];
        var _etype35 = 0;
        _rtmp336 = input.readListBegin();
        _etype35 = _rtmp336.etype;
        _size32 = _rtmp336.size;
        for (var _i37 = 0; _i37 < _size32; ++_i37)
        {
          var elem38 = null;
          var _size39 = 0;
          var _rtmp343;
          elem38 = [];
          var _etype42 = 0;
          _rtmp343 = input.readListBegin();
          _etype42 = _rtmp343.etype;
          _size39 = _rtmp343.size;
          for (var _i44 = 0; _i44 < _size39; ++_i44)
          {
            var elem45 = null;
            elem45 = input.readString().value;
            elem38.push(elem45);
          }
          input.readListEnd();
          this.success.push(elem38);
        }
        input.readListEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_getUserRatedItems_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_getUserRatedItems_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.LIST, 0);
    output.writeListBegin(Thrift.Type.LIST, this.success.length);
    for (var iter46 in this.success)
    {
      if (this.success.hasOwnProperty(iter46))
      {
        iter46 = this.success[iter46];
        output.writeListBegin(Thrift.Type.STRING, iter46.length);
        for (var iter47 in iter46)
        {
          if (iter46.hasOwnProperty(iter47))
          {
            iter47 = iter46[iter47];
            output.writeString(iter47);
          }
        }
        output.writeListEnd();
      }
    }
    output.writeListEnd();
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_initiateModel_args = function(args) {
  this.table = null;
  if (args) {
    if (args.table !== undefined) {
      this.table = args.table;
    }
  }
};
kibitz.RecommenderService_initiateModel_args.prototype = {};
kibitz.RecommenderService_initiateModel_args.prototype.read = function(input) {
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
        this.table = input.readString().value;
      } else {
        input.skip(ftype);
      }
      break;
      case 0:
        input.skip(ftype);
        break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_initiateModel_args.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_initiateModel_args');
  if (this.table !== null && this.table !== undefined) {
    output.writeFieldBegin('table', Thrift.Type.STRING, 1);
    output.writeString(this.table);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderService_initiateModel_result = function(args) {
};
kibitz.RecommenderService_initiateModel_result.prototype = {};
kibitz.RecommenderService_initiateModel_result.prototype.read = function(input) {
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
    input.skip(ftype);
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

kibitz.RecommenderService_initiateModel_result.prototype.write = function(output) {
  output.writeStructBegin('RecommenderService_initiateModel_result');
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

kibitz.RecommenderServiceClient = function(input, output) {
    this.input = input;
    this.output = (!output) ? input : output;
    this.seqid = 0;
};
kibitz.RecommenderServiceClient.prototype = {};
kibitz.RecommenderServiceClient.prototype.makeRecommendation = function(userId, numRecs) {
  this.send_makeRecommendation(userId, numRecs);
  return this.recv_makeRecommendation();
};

kibitz.RecommenderServiceClient.prototype.send_makeRecommendation = function(userId, numRecs) {
  this.output.writeMessageBegin('makeRecommendation', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_makeRecommendation_args();
  args.userId = userId;
  args.numRecs = numRecs;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_makeRecommendation = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_makeRecommendation_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'makeRecommendation failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.getItems = function() {
  this.send_getItems();
  return this.recv_getItems();
};

kibitz.RecommenderServiceClient.prototype.send_getItems = function() {
  this.output.writeMessageBegin('getItems', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_getItems_args();
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_getItems = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_getItems_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'getItems failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.recordRatings = function(userId, itemId, rating) {
  this.send_recordRatings(userId, itemId, rating);
  this.recv_recordRatings();
};

kibitz.RecommenderServiceClient.prototype.send_recordRatings = function(userId, itemId, rating) {
  this.output.writeMessageBegin('recordRatings', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_recordRatings_args();
  args.userId = userId;
  args.itemId = itemId;
  args.rating = rating;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_recordRatings = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_recordRatings_result();
  result.read(this.input);
  this.input.readMessageEnd();

  return;
};
kibitz.RecommenderServiceClient.prototype.deleteRatings = function(userId, itemId) {
  this.send_deleteRatings(userId, itemId);
  this.recv_deleteRatings();
};

kibitz.RecommenderServiceClient.prototype.send_deleteRatings = function(userId, itemId) {
  this.output.writeMessageBegin('deleteRatings', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_deleteRatings_args();
  args.userId = userId;
  args.itemId = itemId;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_deleteRatings = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_deleteRatings_result();
  result.read(this.input);
  this.input.readMessageEnd();

  return;
};
kibitz.RecommenderServiceClient.prototype.createNewUser = function(username, email, password) {
  this.send_createNewUser(username, email, password);
  return this.recv_createNewUser();
};

kibitz.RecommenderServiceClient.prototype.send_createNewUser = function(username, email, password) {
  this.output.writeMessageBegin('createNewUser', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_createNewUser_args();
  args.username = username;
  args.email = email;
  args.password = password;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_createNewUser = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_createNewUser_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'createNewUser failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.checkUsername = function(username) {
  this.send_checkUsername(username);
  return this.recv_checkUsername();
};

kibitz.RecommenderServiceClient.prototype.send_checkUsername = function(username) {
  this.output.writeMessageBegin('checkUsername', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_checkUsername_args();
  args.username = username;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_checkUsername = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_checkUsername_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'checkUsername failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.checkLogin = function(username, password) {
  this.send_checkLogin(username, password);
  return this.recv_checkLogin();
};

kibitz.RecommenderServiceClient.prototype.send_checkLogin = function(username, password) {
  this.output.writeMessageBegin('checkLogin', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_checkLogin_args();
  args.username = username;
  args.password = password;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_checkLogin = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_checkLogin_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'checkLogin failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.getUserRatedItems = function(userId) {
  this.send_getUserRatedItems(userId);
  return this.recv_getUserRatedItems();
};

kibitz.RecommenderServiceClient.prototype.send_getUserRatedItems = function(userId) {
  this.output.writeMessageBegin('getUserRatedItems', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_getUserRatedItems_args();
  args.userId = userId;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_getUserRatedItems = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_getUserRatedItems_result();
  result.read(this.input);
  this.input.readMessageEnd();

  if (null !== result.success) {
    return result.success;
  }
  throw 'getUserRatedItems failed: unknown result';
};
kibitz.RecommenderServiceClient.prototype.initiateModel = function(table) {
  this.send_initiateModel(table);
  this.recv_initiateModel();
};

kibitz.RecommenderServiceClient.prototype.send_initiateModel = function(table) {
  this.output.writeMessageBegin('initiateModel', Thrift.MessageType.CALL, this.seqid);
  var args = new kibitz.RecommenderService_initiateModel_args();
  args.table = table;
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

kibitz.RecommenderServiceClient.prototype.recv_initiateModel = function() {
  var ret = this.input.readMessageBegin();
  var fname = ret.fname;
  var mtype = ret.mtype;
  var rseqid = ret.rseqid;
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(this.input);
    this.input.readMessageEnd();
    throw x;
  }
  var result = new kibitz.RecommenderService_initiateModel_result();
  result.read(this.input);
  this.input.readMessageEnd();

  return;
};
