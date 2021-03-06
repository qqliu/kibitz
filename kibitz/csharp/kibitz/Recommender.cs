/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;
using Thrift;
using Thrift.Collections;
using System.Runtime.Serialization;
using Thrift.Protocol;
using Thrift.Transport;

namespace kibitz
{

  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class Recommender : TBase
  {
    private string _video;
    private Dictionary<string, string> _itemTypes;
    private List<string> _displayItems;
    private string _ratingsColumn;
    private string _primaryKey;

    public string Username { get; set; }

    public string RecommenderName { get; set; }

    public string ClientKey { get; set; }

    public string Homepage { get; set; }

    public string RepoName { get; set; }

    public string Title { get; set; }

    public string Description { get; set; }

    public string Image { get; set; }

    public string Video
    {
      get
      {
        return _video;
      }
      set
      {
        __isset.video = true;
        this._video = value;
      }
    }

    public Dictionary<string, string> ItemTypes
    {
      get
      {
        return _itemTypes;
      }
      set
      {
        __isset.itemTypes = true;
        this._itemTypes = value;
      }
    }

    public List<string> DisplayItems
    {
      get
      {
        return _displayItems;
      }
      set
      {
        __isset.displayItems = true;
        this._displayItems = value;
      }
    }

    public long NumRecs { get; set; }

    public long MaxRatingVal { get; set; }

    public string RatingsColumn
    {
      get
      {
        return _ratingsColumn;
      }
      set
      {
        __isset.ratingsColumn = true;
        this._ratingsColumn = value;
      }
    }

    public string PrimaryKey
    {
      get
      {
        return _primaryKey;
      }
      set
      {
        __isset.primaryKey = true;
        this._primaryKey = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool video;
      public bool itemTypes;
      public bool displayItems;
      public bool ratingsColumn;
      public bool primaryKey;
    }

    public Recommender() {
    }

    public Recommender(string username, string recommenderName, string clientKey, string homepage, string repoName, string title, string description, string image, long numRecs, long maxRatingVal) : this() {
      this.Username = username;
      this.RecommenderName = recommenderName;
      this.ClientKey = clientKey;
      this.Homepage = homepage;
      this.RepoName = repoName;
      this.Title = title;
      this.Description = description;
      this.Image = image;
      this.NumRecs = numRecs;
      this.MaxRatingVal = maxRatingVal;
    }

    public void Read (TProtocol iprot)
    {
      bool isset_username = false;
      bool isset_recommenderName = false;
      bool isset_clientKey = false;
      bool isset_homepage = false;
      bool isset_repoName = false;
      bool isset_title = false;
      bool isset_description = false;
      bool isset_image = false;
      bool isset_numRecs = false;
      bool isset_maxRatingVal = false;
      TField field;
      iprot.ReadStructBegin();
      while (true)
      {
        field = iprot.ReadFieldBegin();
        if (field.Type == TType.Stop) { 
          break;
        }
        switch (field.ID)
        {
          case 1:
            if (field.Type == TType.String) {
              Username = iprot.ReadString();
              isset_username = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 2:
            if (field.Type == TType.String) {
              RecommenderName = iprot.ReadString();
              isset_recommenderName = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 3:
            if (field.Type == TType.String) {
              ClientKey = iprot.ReadString();
              isset_clientKey = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 4:
            if (field.Type == TType.String) {
              Homepage = iprot.ReadString();
              isset_homepage = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 5:
            if (field.Type == TType.String) {
              RepoName = iprot.ReadString();
              isset_repoName = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 6:
            if (field.Type == TType.String) {
              Title = iprot.ReadString();
              isset_title = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 7:
            if (field.Type == TType.String) {
              Description = iprot.ReadString();
              isset_description = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 8:
            if (field.Type == TType.String) {
              Image = iprot.ReadString();
              isset_image = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 9:
            if (field.Type == TType.String) {
              Video = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 10:
            if (field.Type == TType.Map) {
              {
                ItemTypes = new Dictionary<string, string>();
                TMap _map5 = iprot.ReadMapBegin();
                for( int _i6 = 0; _i6 < _map5.Count; ++_i6)
                {
                  string _key7;
                  string _val8;
                  _key7 = iprot.ReadString();
                  _val8 = iprot.ReadString();
                  ItemTypes[_key7] = _val8;
                }
                iprot.ReadMapEnd();
              }
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 11:
            if (field.Type == TType.List) {
              {
                DisplayItems = new List<string>();
                TList _list9 = iprot.ReadListBegin();
                for( int _i10 = 0; _i10 < _list9.Count; ++_i10)
                {
                  string _elem11;
                  _elem11 = iprot.ReadString();
                  DisplayItems.Add(_elem11);
                }
                iprot.ReadListEnd();
              }
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 12:
            if (field.Type == TType.I64) {
              NumRecs = iprot.ReadI64();
              isset_numRecs = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 13:
            if (field.Type == TType.I64) {
              MaxRatingVal = iprot.ReadI64();
              isset_maxRatingVal = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 14:
            if (field.Type == TType.String) {
              RatingsColumn = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 15:
            if (field.Type == TType.String) {
              PrimaryKey = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          default: 
            TProtocolUtil.Skip(iprot, field.Type);
            break;
        }
        iprot.ReadFieldEnd();
      }
      iprot.ReadStructEnd();
      if (!isset_username)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_recommenderName)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_clientKey)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_homepage)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_repoName)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_title)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_description)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_image)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_numRecs)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_maxRatingVal)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
    }

    public void Write(TProtocol oprot) {
      TStruct struc = new TStruct("Recommender");
      oprot.WriteStructBegin(struc);
      TField field = new TField();
      field.Name = "username";
      field.Type = TType.String;
      field.ID = 1;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(Username);
      oprot.WriteFieldEnd();
      field.Name = "recommenderName";
      field.Type = TType.String;
      field.ID = 2;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(RecommenderName);
      oprot.WriteFieldEnd();
      field.Name = "clientKey";
      field.Type = TType.String;
      field.ID = 3;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(ClientKey);
      oprot.WriteFieldEnd();
      field.Name = "homepage";
      field.Type = TType.String;
      field.ID = 4;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(Homepage);
      oprot.WriteFieldEnd();
      field.Name = "repoName";
      field.Type = TType.String;
      field.ID = 5;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(RepoName);
      oprot.WriteFieldEnd();
      field.Name = "title";
      field.Type = TType.String;
      field.ID = 6;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(Title);
      oprot.WriteFieldEnd();
      field.Name = "description";
      field.Type = TType.String;
      field.ID = 7;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(Description);
      oprot.WriteFieldEnd();
      field.Name = "image";
      field.Type = TType.String;
      field.ID = 8;
      oprot.WriteFieldBegin(field);
      oprot.WriteString(Image);
      oprot.WriteFieldEnd();
      if (Video != null && __isset.video) {
        field.Name = "video";
        field.Type = TType.String;
        field.ID = 9;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(Video);
        oprot.WriteFieldEnd();
      }
      if (ItemTypes != null && __isset.itemTypes) {
        field.Name = "itemTypes";
        field.Type = TType.Map;
        field.ID = 10;
        oprot.WriteFieldBegin(field);
        {
          oprot.WriteMapBegin(new TMap(TType.String, TType.String, ItemTypes.Count));
          foreach (string _iter12 in ItemTypes.Keys)
          {
            oprot.WriteString(_iter12);
            oprot.WriteString(ItemTypes[_iter12]);
          }
          oprot.WriteMapEnd();
        }
        oprot.WriteFieldEnd();
      }
      if (DisplayItems != null && __isset.displayItems) {
        field.Name = "displayItems";
        field.Type = TType.List;
        field.ID = 11;
        oprot.WriteFieldBegin(field);
        {
          oprot.WriteListBegin(new TList(TType.String, DisplayItems.Count));
          foreach (string _iter13 in DisplayItems)
          {
            oprot.WriteString(_iter13);
          }
          oprot.WriteListEnd();
        }
        oprot.WriteFieldEnd();
      }
      field.Name = "numRecs";
      field.Type = TType.I64;
      field.ID = 12;
      oprot.WriteFieldBegin(field);
      oprot.WriteI64(NumRecs);
      oprot.WriteFieldEnd();
      field.Name = "maxRatingVal";
      field.Type = TType.I64;
      field.ID = 13;
      oprot.WriteFieldBegin(field);
      oprot.WriteI64(MaxRatingVal);
      oprot.WriteFieldEnd();
      if (RatingsColumn != null && __isset.ratingsColumn) {
        field.Name = "ratingsColumn";
        field.Type = TType.String;
        field.ID = 14;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(RatingsColumn);
        oprot.WriteFieldEnd();
      }
      if (PrimaryKey != null && __isset.primaryKey) {
        field.Name = "primaryKey";
        field.Type = TType.String;
        field.ID = 15;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(PrimaryKey);
        oprot.WriteFieldEnd();
      }
      oprot.WriteFieldStop();
      oprot.WriteStructEnd();
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("Recommender(");
      __sb.Append(", Username: ");
      __sb.Append(Username);
      __sb.Append(", RecommenderName: ");
      __sb.Append(RecommenderName);
      __sb.Append(", ClientKey: ");
      __sb.Append(ClientKey);
      __sb.Append(", Homepage: ");
      __sb.Append(Homepage);
      __sb.Append(", RepoName: ");
      __sb.Append(RepoName);
      __sb.Append(", Title: ");
      __sb.Append(Title);
      __sb.Append(", Description: ");
      __sb.Append(Description);
      __sb.Append(", Image: ");
      __sb.Append(Image);
      if (Video != null && __isset.video) {
        __sb.Append(", Video: ");
        __sb.Append(Video);
      }
      if (ItemTypes != null && __isset.itemTypes) {
        __sb.Append(", ItemTypes: ");
        __sb.Append(ItemTypes);
      }
      if (DisplayItems != null && __isset.displayItems) {
        __sb.Append(", DisplayItems: ");
        __sb.Append(DisplayItems);
      }
      __sb.Append(", NumRecs: ");
      __sb.Append(NumRecs);
      __sb.Append(", MaxRatingVal: ");
      __sb.Append(MaxRatingVal);
      if (RatingsColumn != null && __isset.ratingsColumn) {
        __sb.Append(", RatingsColumn: ");
        __sb.Append(RatingsColumn);
      }
      if (PrimaryKey != null && __isset.primaryKey) {
        __sb.Append(", PrimaryKey: ");
        __sb.Append(PrimaryKey);
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }

}
