// Autogenerated by Thrift Compiler (0.9.2)
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING

package kibitz

import (
	"bytes"
	"fmt"
	"git.apache.org/thrift.git/lib/go/thrift"
)

// (needed to ensure safety because of naive import list construction.)
var _ = thrift.ZERO
var _ = fmt.Printf
var _ = bytes.Equal

var GoUnusedProtection__ int;

type Item struct {
  Attributes map[string]string `thrift:"attributes,1,required" json:"attributes"`
  KibitzGeneratedId int64 `thrift:"kibitz_generated_id,2,required" json:"kibitz_generated_id"`
  Confidence int32 `thrift:"confidence,3,required" json:"confidence"`
  PredictedPreferences float64 `thrift:"predictedPreferences,4,required" json:"predictedPreferences"`
}

func NewItem() *Item {
  return &Item{}
}


func (p *Item) GetAttributes() map[string]string {
return p.Attributes
}

func (p *Item) GetKibitzGeneratedId() int64 {
return p.KibitzGeneratedId
}

func (p *Item) GetConfidence() int32 {
return p.Confidence
}

func (p *Item) GetPredictedPreferences() float64 {
return p.PredictedPreferences
}
func (p *Item) Read(iprot thrift.TProtocol) error {
  if _, err := iprot.ReadStructBegin(); err != nil {
    return fmt.Errorf("%T read error: %s", p, err)
  }
  for {
    _, fieldTypeId, fieldId, err := iprot.ReadFieldBegin()
    if err != nil {
      return fmt.Errorf("%T field %d read error: %s", p, fieldId, err)
    }
    if fieldTypeId == thrift.STOP { break; }
    switch fieldId {
    case 1:
      if err := p.ReadField1(iprot); err != nil {
        return err
      }
    case 2:
      if err := p.ReadField2(iprot); err != nil {
        return err
      }
    case 3:
      if err := p.ReadField3(iprot); err != nil {
        return err
      }
    case 4:
      if err := p.ReadField4(iprot); err != nil {
        return err
      }
    default:
      if err := iprot.Skip(fieldTypeId); err != nil {
        return err
      }
    }
    if err := iprot.ReadFieldEnd(); err != nil {
      return err
    }
  }
  if err := iprot.ReadStructEnd(); err != nil {
    return fmt.Errorf("%T read struct end error: %s", p, err)
  }
  return nil
}

func (p *Item)  ReadField1(iprot thrift.TProtocol) error {
  _, _, size, err := iprot.ReadMapBegin()
  if err != nil {
    return fmt.Errorf("error reading map begin: %s", err)
  }
  tMap := make(map[string]string, size)
  p.Attributes =  tMap
  for i := 0; i < size; i ++ {
var _key0 string
    if v, err := iprot.ReadString(); err != nil {
    return fmt.Errorf("error reading field 0: %s", err)
} else {
    _key0 = v
}
var _val1 string
    if v, err := iprot.ReadString(); err != nil {
    return fmt.Errorf("error reading field 0: %s", err)
} else {
    _val1 = v
}
    p.Attributes[_key0] = _val1
  }
  if err := iprot.ReadMapEnd(); err != nil {
    return fmt.Errorf("error reading map end: %s", err)
  }
  return nil
}

func (p *Item)  ReadField2(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadI64(); err != nil {
  return fmt.Errorf("error reading field 2: %s", err)
} else {
  p.KibitzGeneratedId = v
}
  return nil
}

func (p *Item)  ReadField3(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadI32(); err != nil {
  return fmt.Errorf("error reading field 3: %s", err)
} else {
  p.Confidence = v
}
  return nil
}

func (p *Item)  ReadField4(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadDouble(); err != nil {
  return fmt.Errorf("error reading field 4: %s", err)
} else {
  p.PredictedPreferences = v
}
  return nil
}

func (p *Item) Write(oprot thrift.TProtocol) error {
  if err := oprot.WriteStructBegin("Item"); err != nil {
    return fmt.Errorf("%T write struct begin error: %s", p, err) }
  if err := p.writeField1(oprot); err != nil { return err }
  if err := p.writeField2(oprot); err != nil { return err }
  if err := p.writeField3(oprot); err != nil { return err }
  if err := p.writeField4(oprot); err != nil { return err }
  if err := oprot.WriteFieldStop(); err != nil {
    return fmt.Errorf("write field stop error: %s", err) }
  if err := oprot.WriteStructEnd(); err != nil {
    return fmt.Errorf("write struct stop error: %s", err) }
  return nil
}

func (p *Item) writeField1(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("attributes", thrift.MAP, 1); err != nil {
    return fmt.Errorf("%T write field begin error 1:attributes: %s", p, err); }
  if err := oprot.WriteMapBegin(thrift.STRING, thrift.STRING, len(p.Attributes)); err != nil {
    return fmt.Errorf("error writing map begin: %s", err)
  }
  for k, v := range p.Attributes {
    if err := oprot.WriteString(string(k)); err != nil {
    return fmt.Errorf("%T. (0) field write error: %s", p, err) }
    if err := oprot.WriteString(string(v)); err != nil {
    return fmt.Errorf("%T. (0) field write error: %s", p, err) }
  }
  if err := oprot.WriteMapEnd(); err != nil {
    return fmt.Errorf("error writing map end: %s", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 1:attributes: %s", p, err); }
  return err
}

func (p *Item) writeField2(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("kibitz_generated_id", thrift.I64, 2); err != nil {
    return fmt.Errorf("%T write field begin error 2:kibitz_generated_id: %s", p, err); }
  if err := oprot.WriteI64(int64(p.KibitzGeneratedId)); err != nil {
  return fmt.Errorf("%T.kibitz_generated_id (2) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 2:kibitz_generated_id: %s", p, err); }
  return err
}

func (p *Item) writeField3(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("confidence", thrift.I32, 3); err != nil {
    return fmt.Errorf("%T write field begin error 3:confidence: %s", p, err); }
  if err := oprot.WriteI32(int32(p.Confidence)); err != nil {
  return fmt.Errorf("%T.confidence (3) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 3:confidence: %s", p, err); }
  return err
}

func (p *Item) writeField4(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("predictedPreferences", thrift.DOUBLE, 4); err != nil {
    return fmt.Errorf("%T write field begin error 4:predictedPreferences: %s", p, err); }
  if err := oprot.WriteDouble(float64(p.PredictedPreferences)); err != nil {
  return fmt.Errorf("%T.predictedPreferences (4) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 4:predictedPreferences: %s", p, err); }
  return err
}

func (p *Item) String() string {
  if p == nil {
    return "<nil>"
  }
  return fmt.Sprintf("Item(%+v)", *p)
}

type Recommender struct {
  Username string `thrift:"username,1,required" json:"username"`
  RecommenderName string `thrift:"recommenderName,2,required" json:"recommenderName"`
  ClientKey string `thrift:"clientKey,3,required" json:"clientKey"`
  Homepage string `thrift:"homepage,4,required" json:"homepage"`
  RepoName string `thrift:"repoName,5,required" json:"repoName"`
  Title string `thrift:"title,6,required" json:"title"`
  Description string `thrift:"description,7,required" json:"description"`
  Image string `thrift:"image,8,required" json:"image"`
  Video string `thrift:"video,9" json:"video"`
  ItemTypes map[string]string `thrift:"itemTypes,10" json:"itemTypes"`
  DisplayItems []string `thrift:"displayItems,11" json:"displayItems"`
  NumRecs int64 `thrift:"numRecs,12,required" json:"numRecs"`
  MaxRatingVal int64 `thrift:"maxRatingVal,13,required" json:"maxRatingVal"`
  RatingsColumn string `thrift:"ratingsColumn,14" json:"ratingsColumn"`
  PrimaryKey string `thrift:"primaryKey,15" json:"primaryKey"`
}

func NewRecommender() *Recommender {
  return &Recommender{}
}


func (p *Recommender) GetUsername() string {
return p.Username
}

func (p *Recommender) GetRecommenderName() string {
return p.RecommenderName
}

func (p *Recommender) GetClientKey() string {
return p.ClientKey
}

func (p *Recommender) GetHomepage() string {
return p.Homepage
}

func (p *Recommender) GetRepoName() string {
return p.RepoName
}

func (p *Recommender) GetTitle() string {
return p.Title
}

func (p *Recommender) GetDescription() string {
return p.Description
}

func (p *Recommender) GetImage() string {
return p.Image
}

func (p *Recommender) GetVideo() string {
return p.Video
}

func (p *Recommender) GetItemTypes() map[string]string {
return p.ItemTypes
}

func (p *Recommender) GetDisplayItems() []string {
return p.DisplayItems
}

func (p *Recommender) GetNumRecs() int64 {
return p.NumRecs
}

func (p *Recommender) GetMaxRatingVal() int64 {
return p.MaxRatingVal
}

func (p *Recommender) GetRatingsColumn() string {
return p.RatingsColumn
}

func (p *Recommender) GetPrimaryKey() string {
return p.PrimaryKey
}
func (p *Recommender) Read(iprot thrift.TProtocol) error {
  if _, err := iprot.ReadStructBegin(); err != nil {
    return fmt.Errorf("%T read error: %s", p, err)
  }
  for {
    _, fieldTypeId, fieldId, err := iprot.ReadFieldBegin()
    if err != nil {
      return fmt.Errorf("%T field %d read error: %s", p, fieldId, err)
    }
    if fieldTypeId == thrift.STOP { break; }
    switch fieldId {
    case 1:
      if err := p.ReadField1(iprot); err != nil {
        return err
      }
    case 2:
      if err := p.ReadField2(iprot); err != nil {
        return err
      }
    case 3:
      if err := p.ReadField3(iprot); err != nil {
        return err
      }
    case 4:
      if err := p.ReadField4(iprot); err != nil {
        return err
      }
    case 5:
      if err := p.ReadField5(iprot); err != nil {
        return err
      }
    case 6:
      if err := p.ReadField6(iprot); err != nil {
        return err
      }
    case 7:
      if err := p.ReadField7(iprot); err != nil {
        return err
      }
    case 8:
      if err := p.ReadField8(iprot); err != nil {
        return err
      }
    case 9:
      if err := p.ReadField9(iprot); err != nil {
        return err
      }
    case 10:
      if err := p.ReadField10(iprot); err != nil {
        return err
      }
    case 11:
      if err := p.ReadField11(iprot); err != nil {
        return err
      }
    case 12:
      if err := p.ReadField12(iprot); err != nil {
        return err
      }
    case 13:
      if err := p.ReadField13(iprot); err != nil {
        return err
      }
    case 14:
      if err := p.ReadField14(iprot); err != nil {
        return err
      }
    case 15:
      if err := p.ReadField15(iprot); err != nil {
        return err
      }
    default:
      if err := iprot.Skip(fieldTypeId); err != nil {
        return err
      }
    }
    if err := iprot.ReadFieldEnd(); err != nil {
      return err
    }
  }
  if err := iprot.ReadStructEnd(); err != nil {
    return fmt.Errorf("%T read struct end error: %s", p, err)
  }
  return nil
}

func (p *Recommender)  ReadField1(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 1: %s", err)
} else {
  p.Username = v
}
  return nil
}

func (p *Recommender)  ReadField2(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 2: %s", err)
} else {
  p.RecommenderName = v
}
  return nil
}

func (p *Recommender)  ReadField3(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 3: %s", err)
} else {
  p.ClientKey = v
}
  return nil
}

func (p *Recommender)  ReadField4(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 4: %s", err)
} else {
  p.Homepage = v
}
  return nil
}

func (p *Recommender)  ReadField5(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 5: %s", err)
} else {
  p.RepoName = v
}
  return nil
}

func (p *Recommender)  ReadField6(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 6: %s", err)
} else {
  p.Title = v
}
  return nil
}

func (p *Recommender)  ReadField7(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 7: %s", err)
} else {
  p.Description = v
}
  return nil
}

func (p *Recommender)  ReadField8(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 8: %s", err)
} else {
  p.Image = v
}
  return nil
}

func (p *Recommender)  ReadField9(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 9: %s", err)
} else {
  p.Video = v
}
  return nil
}

func (p *Recommender)  ReadField10(iprot thrift.TProtocol) error {
  _, _, size, err := iprot.ReadMapBegin()
  if err != nil {
    return fmt.Errorf("error reading map begin: %s", err)
  }
  tMap := make(map[string]string, size)
  p.ItemTypes =  tMap
  for i := 0; i < size; i ++ {
var _key2 string
    if v, err := iprot.ReadString(); err != nil {
    return fmt.Errorf("error reading field 0: %s", err)
} else {
    _key2 = v
}
var _val3 string
    if v, err := iprot.ReadString(); err != nil {
    return fmt.Errorf("error reading field 0: %s", err)
} else {
    _val3 = v
}
    p.ItemTypes[_key2] = _val3
  }
  if err := iprot.ReadMapEnd(); err != nil {
    return fmt.Errorf("error reading map end: %s", err)
  }
  return nil
}

func (p *Recommender)  ReadField11(iprot thrift.TProtocol) error {
  _, size, err := iprot.ReadListBegin()
  if err != nil {
    return fmt.Errorf("error reading list begin: %s", err)
  }
  tSlice := make([]string, 0, size)
  p.DisplayItems =  tSlice
  for i := 0; i < size; i ++ {
var _elem4 string
    if v, err := iprot.ReadString(); err != nil {
    return fmt.Errorf("error reading field 0: %s", err)
} else {
    _elem4 = v
}
    p.DisplayItems = append(p.DisplayItems, _elem4)
  }
  if err := iprot.ReadListEnd(); err != nil {
    return fmt.Errorf("error reading list end: %s", err)
  }
  return nil
}

func (p *Recommender)  ReadField12(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadI64(); err != nil {
  return fmt.Errorf("error reading field 12: %s", err)
} else {
  p.NumRecs = v
}
  return nil
}

func (p *Recommender)  ReadField13(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadI64(); err != nil {
  return fmt.Errorf("error reading field 13: %s", err)
} else {
  p.MaxRatingVal = v
}
  return nil
}

func (p *Recommender)  ReadField14(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 14: %s", err)
} else {
  p.RatingsColumn = v
}
  return nil
}

func (p *Recommender)  ReadField15(iprot thrift.TProtocol) error {
  if v, err := iprot.ReadString(); err != nil {
  return fmt.Errorf("error reading field 15: %s", err)
} else {
  p.PrimaryKey = v
}
  return nil
}

func (p *Recommender) Write(oprot thrift.TProtocol) error {
  if err := oprot.WriteStructBegin("Recommender"); err != nil {
    return fmt.Errorf("%T write struct begin error: %s", p, err) }
  if err := p.writeField1(oprot); err != nil { return err }
  if err := p.writeField2(oprot); err != nil { return err }
  if err := p.writeField3(oprot); err != nil { return err }
  if err := p.writeField4(oprot); err != nil { return err }
  if err := p.writeField5(oprot); err != nil { return err }
  if err := p.writeField6(oprot); err != nil { return err }
  if err := p.writeField7(oprot); err != nil { return err }
  if err := p.writeField8(oprot); err != nil { return err }
  if err := p.writeField9(oprot); err != nil { return err }
  if err := p.writeField10(oprot); err != nil { return err }
  if err := p.writeField11(oprot); err != nil { return err }
  if err := p.writeField12(oprot); err != nil { return err }
  if err := p.writeField13(oprot); err != nil { return err }
  if err := p.writeField14(oprot); err != nil { return err }
  if err := p.writeField15(oprot); err != nil { return err }
  if err := oprot.WriteFieldStop(); err != nil {
    return fmt.Errorf("write field stop error: %s", err) }
  if err := oprot.WriteStructEnd(); err != nil {
    return fmt.Errorf("write struct stop error: %s", err) }
  return nil
}

func (p *Recommender) writeField1(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("username", thrift.STRING, 1); err != nil {
    return fmt.Errorf("%T write field begin error 1:username: %s", p, err); }
  if err := oprot.WriteString(string(p.Username)); err != nil {
  return fmt.Errorf("%T.username (1) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 1:username: %s", p, err); }
  return err
}

func (p *Recommender) writeField2(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("recommenderName", thrift.STRING, 2); err != nil {
    return fmt.Errorf("%T write field begin error 2:recommenderName: %s", p, err); }
  if err := oprot.WriteString(string(p.RecommenderName)); err != nil {
  return fmt.Errorf("%T.recommenderName (2) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 2:recommenderName: %s", p, err); }
  return err
}

func (p *Recommender) writeField3(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("clientKey", thrift.STRING, 3); err != nil {
    return fmt.Errorf("%T write field begin error 3:clientKey: %s", p, err); }
  if err := oprot.WriteString(string(p.ClientKey)); err != nil {
  return fmt.Errorf("%T.clientKey (3) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 3:clientKey: %s", p, err); }
  return err
}

func (p *Recommender) writeField4(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("homepage", thrift.STRING, 4); err != nil {
    return fmt.Errorf("%T write field begin error 4:homepage: %s", p, err); }
  if err := oprot.WriteString(string(p.Homepage)); err != nil {
  return fmt.Errorf("%T.homepage (4) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 4:homepage: %s", p, err); }
  return err
}

func (p *Recommender) writeField5(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("repoName", thrift.STRING, 5); err != nil {
    return fmt.Errorf("%T write field begin error 5:repoName: %s", p, err); }
  if err := oprot.WriteString(string(p.RepoName)); err != nil {
  return fmt.Errorf("%T.repoName (5) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 5:repoName: %s", p, err); }
  return err
}

func (p *Recommender) writeField6(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("title", thrift.STRING, 6); err != nil {
    return fmt.Errorf("%T write field begin error 6:title: %s", p, err); }
  if err := oprot.WriteString(string(p.Title)); err != nil {
  return fmt.Errorf("%T.title (6) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 6:title: %s", p, err); }
  return err
}

func (p *Recommender) writeField7(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("description", thrift.STRING, 7); err != nil {
    return fmt.Errorf("%T write field begin error 7:description: %s", p, err); }
  if err := oprot.WriteString(string(p.Description)); err != nil {
  return fmt.Errorf("%T.description (7) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 7:description: %s", p, err); }
  return err
}

func (p *Recommender) writeField8(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("image", thrift.STRING, 8); err != nil {
    return fmt.Errorf("%T write field begin error 8:image: %s", p, err); }
  if err := oprot.WriteString(string(p.Image)); err != nil {
  return fmt.Errorf("%T.image (8) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 8:image: %s", p, err); }
  return err
}

func (p *Recommender) writeField9(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("video", thrift.STRING, 9); err != nil {
    return fmt.Errorf("%T write field begin error 9:video: %s", p, err); }
  if err := oprot.WriteString(string(p.Video)); err != nil {
  return fmt.Errorf("%T.video (9) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 9:video: %s", p, err); }
  return err
}

func (p *Recommender) writeField10(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("itemTypes", thrift.MAP, 10); err != nil {
    return fmt.Errorf("%T write field begin error 10:itemTypes: %s", p, err); }
  if err := oprot.WriteMapBegin(thrift.STRING, thrift.STRING, len(p.ItemTypes)); err != nil {
    return fmt.Errorf("error writing map begin: %s", err)
  }
  for k, v := range p.ItemTypes {
    if err := oprot.WriteString(string(k)); err != nil {
    return fmt.Errorf("%T. (0) field write error: %s", p, err) }
    if err := oprot.WriteString(string(v)); err != nil {
    return fmt.Errorf("%T. (0) field write error: %s", p, err) }
  }
  if err := oprot.WriteMapEnd(); err != nil {
    return fmt.Errorf("error writing map end: %s", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 10:itemTypes: %s", p, err); }
  return err
}

func (p *Recommender) writeField11(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("displayItems", thrift.LIST, 11); err != nil {
    return fmt.Errorf("%T write field begin error 11:displayItems: %s", p, err); }
  if err := oprot.WriteListBegin(thrift.STRING, len(p.DisplayItems)); err != nil {
    return fmt.Errorf("error writing list begin: %s", err)
  }
  for _, v := range p.DisplayItems {
    if err := oprot.WriteString(string(v)); err != nil {
    return fmt.Errorf("%T. (0) field write error: %s", p, err) }
  }
  if err := oprot.WriteListEnd(); err != nil {
    return fmt.Errorf("error writing list end: %s", err)
  }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 11:displayItems: %s", p, err); }
  return err
}

func (p *Recommender) writeField12(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("numRecs", thrift.I64, 12); err != nil {
    return fmt.Errorf("%T write field begin error 12:numRecs: %s", p, err); }
  if err := oprot.WriteI64(int64(p.NumRecs)); err != nil {
  return fmt.Errorf("%T.numRecs (12) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 12:numRecs: %s", p, err); }
  return err
}

func (p *Recommender) writeField13(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("maxRatingVal", thrift.I64, 13); err != nil {
    return fmt.Errorf("%T write field begin error 13:maxRatingVal: %s", p, err); }
  if err := oprot.WriteI64(int64(p.MaxRatingVal)); err != nil {
  return fmt.Errorf("%T.maxRatingVal (13) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 13:maxRatingVal: %s", p, err); }
  return err
}

func (p *Recommender) writeField14(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("ratingsColumn", thrift.STRING, 14); err != nil {
    return fmt.Errorf("%T write field begin error 14:ratingsColumn: %s", p, err); }
  if err := oprot.WriteString(string(p.RatingsColumn)); err != nil {
  return fmt.Errorf("%T.ratingsColumn (14) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 14:ratingsColumn: %s", p, err); }
  return err
}

func (p *Recommender) writeField15(oprot thrift.TProtocol) (err error) {
  if err := oprot.WriteFieldBegin("primaryKey", thrift.STRING, 15); err != nil {
    return fmt.Errorf("%T write field begin error 15:primaryKey: %s", p, err); }
  if err := oprot.WriteString(string(p.PrimaryKey)); err != nil {
  return fmt.Errorf("%T.primaryKey (15) field write error: %s", p, err) }
  if err := oprot.WriteFieldEnd(); err != nil {
    return fmt.Errorf("%T write field end error 15:primaryKey: %s", p, err); }
  return err
}

func (p *Recommender) String() string {
  if p == nil {
    return "<nil>"
  }
  return fmt.Sprintf("Recommender(%+v)", *p)
}

