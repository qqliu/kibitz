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
  public partial class Item : TBase
  {

    public Dictionary<string, string> Attributes { get; set; }

    public long Kibitz_generated_id { get; set; }

    public int Confidence { get; set; }

    public double PredictedPreferences { get; set; }

    public Item() {
    }

    public Item(Dictionary<string, string> attributes, long kibitz_generated_id, int confidence, double predictedPreferences) : this() {
      this.Attributes = attributes;
      this.Kibitz_generated_id = kibitz_generated_id;
      this.Confidence = confidence;
      this.PredictedPreferences = predictedPreferences;
    }

    public void Read (TProtocol iprot)
    {
      bool isset_attributes = false;
      bool isset_kibitz_generated_id = false;
      bool isset_confidence = false;
      bool isset_predictedPreferences = false;
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
            if (field.Type == TType.Map) {
              {
                Attributes = new Dictionary<string, string>();
                TMap _map0 = iprot.ReadMapBegin();
                for( int _i1 = 0; _i1 < _map0.Count; ++_i1)
                {
                  string _key2;
                  string _val3;
                  _key2 = iprot.ReadString();
                  _val3 = iprot.ReadString();
                  Attributes[_key2] = _val3;
                }
                iprot.ReadMapEnd();
              }
              isset_attributes = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 2:
            if (field.Type == TType.I64) {
              Kibitz_generated_id = iprot.ReadI64();
              isset_kibitz_generated_id = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 3:
            if (field.Type == TType.I32) {
              Confidence = iprot.ReadI32();
              isset_confidence = true;
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 4:
            if (field.Type == TType.Double) {
              PredictedPreferences = iprot.ReadDouble();
              isset_predictedPreferences = true;
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
      if (!isset_attributes)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_kibitz_generated_id)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_confidence)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
      if (!isset_predictedPreferences)
        throw new TProtocolException(TProtocolException.INVALID_DATA);
    }

    public void Write(TProtocol oprot) {
      TStruct struc = new TStruct("Item");
      oprot.WriteStructBegin(struc);
      TField field = new TField();
      field.Name = "attributes";
      field.Type = TType.Map;
      field.ID = 1;
      oprot.WriteFieldBegin(field);
      {
        oprot.WriteMapBegin(new TMap(TType.String, TType.String, Attributes.Count));
        foreach (string _iter4 in Attributes.Keys)
        {
          oprot.WriteString(_iter4);
          oprot.WriteString(Attributes[_iter4]);
        }
        oprot.WriteMapEnd();
      }
      oprot.WriteFieldEnd();
      field.Name = "kibitz_generated_id";
      field.Type = TType.I64;
      field.ID = 2;
      oprot.WriteFieldBegin(field);
      oprot.WriteI64(Kibitz_generated_id);
      oprot.WriteFieldEnd();
      field.Name = "confidence";
      field.Type = TType.I32;
      field.ID = 3;
      oprot.WriteFieldBegin(field);
      oprot.WriteI32(Confidence);
      oprot.WriteFieldEnd();
      field.Name = "predictedPreferences";
      field.Type = TType.Double;
      field.ID = 4;
      oprot.WriteFieldBegin(field);
      oprot.WriteDouble(PredictedPreferences);
      oprot.WriteFieldEnd();
      oprot.WriteFieldStop();
      oprot.WriteStructEnd();
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("Item(");
      __sb.Append(", Attributes: ");
      __sb.Append(Attributes);
      __sb.Append(", Kibitz_generated_id: ");
      __sb.Append(Kibitz_generated_id);
      __sb.Append(", Confidence: ");
      __sb.Append(Confidence);
      __sb.Append(", PredictedPreferences: ");
      __sb.Append(PredictedPreferences);
      __sb.Append(")");
      return __sb.ToString();
    }

  }

}
