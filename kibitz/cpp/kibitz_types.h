/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef kibitz_TYPES_H
#define kibitz_TYPES_H

#include <iosfwd>

#include <thrift/Thrift.h>
#include <thrift/TApplicationException.h>
#include <thrift/protocol/TProtocol.h>
#include <thrift/transport/TTransport.h>

#include <thrift/cxxfunctional.h>


namespace kibitz {

class Item;

class Recommender;


class Item {
 public:

  static const char* ascii_fingerprint; // = "235731DDD465749C937990D982C869C8";
  static const uint8_t binary_fingerprint[16]; // = {0x23,0x57,0x31,0xDD,0xD4,0x65,0x74,0x9C,0x93,0x79,0x90,0xD9,0x82,0xC8,0x69,0xC8};

  Item(const Item&);
  Item& operator=(const Item&);
  Item() : kibitz_generated_id(0), confidence(0), predictedPreferences(0) {
  }

  virtual ~Item() throw();
  std::map<std::string, std::string>  attributes;
  int64_t kibitz_generated_id;
  int32_t confidence;
  double predictedPreferences;

  void __set_attributes(const std::map<std::string, std::string> & val);

  void __set_kibitz_generated_id(const int64_t val);

  void __set_confidence(const int32_t val);

  void __set_predictedPreferences(const double val);

  bool operator == (const Item & rhs) const
  {
    if (!(attributes == rhs.attributes))
      return false;
    if (!(kibitz_generated_id == rhs.kibitz_generated_id))
      return false;
    if (!(confidence == rhs.confidence))
      return false;
    if (!(predictedPreferences == rhs.predictedPreferences))
      return false;
    return true;
  }
  bool operator != (const Item &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const Item & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  friend std::ostream& operator<<(std::ostream& out, const Item& obj);
};

void swap(Item &a, Item &b);

typedef struct _Recommender__isset {
  _Recommender__isset() : video(false), itemTypes(false), displayItems(false), ratingsColumn(false), primaryKey(false) {}
  bool video :1;
  bool itemTypes :1;
  bool displayItems :1;
  bool ratingsColumn :1;
  bool primaryKey :1;
} _Recommender__isset;

class Recommender {
 public:

  static const char* ascii_fingerprint; // = "3F18D3B132EB5B730FF079FBDEE92FD0";
  static const uint8_t binary_fingerprint[16]; // = {0x3F,0x18,0xD3,0xB1,0x32,0xEB,0x5B,0x73,0x0F,0xF0,0x79,0xFB,0xDE,0xE9,0x2F,0xD0};

  Recommender(const Recommender&);
  Recommender& operator=(const Recommender&);
  Recommender() : username(), recommenderName(), clientKey(), homepage(), repoName(), title(), description(), image(), video(), numRecs(0), maxRatingVal(0), ratingsColumn(), primaryKey() {
  }

  virtual ~Recommender() throw();
  std::string username;
  std::string recommenderName;
  std::string clientKey;
  std::string homepage;
  std::string repoName;
  std::string title;
  std::string description;
  std::string image;
  std::string video;
  std::map<std::string, std::string>  itemTypes;
  std::vector<std::string>  displayItems;
  int64_t numRecs;
  int64_t maxRatingVal;
  std::string ratingsColumn;
  std::string primaryKey;

  _Recommender__isset __isset;

  void __set_username(const std::string& val);

  void __set_recommenderName(const std::string& val);

  void __set_clientKey(const std::string& val);

  void __set_homepage(const std::string& val);

  void __set_repoName(const std::string& val);

  void __set_title(const std::string& val);

  void __set_description(const std::string& val);

  void __set_image(const std::string& val);

  void __set_video(const std::string& val);

  void __set_itemTypes(const std::map<std::string, std::string> & val);

  void __set_displayItems(const std::vector<std::string> & val);

  void __set_numRecs(const int64_t val);

  void __set_maxRatingVal(const int64_t val);

  void __set_ratingsColumn(const std::string& val);

  void __set_primaryKey(const std::string& val);

  bool operator == (const Recommender & rhs) const
  {
    if (!(username == rhs.username))
      return false;
    if (!(recommenderName == rhs.recommenderName))
      return false;
    if (!(clientKey == rhs.clientKey))
      return false;
    if (!(homepage == rhs.homepage))
      return false;
    if (!(repoName == rhs.repoName))
      return false;
    if (!(title == rhs.title))
      return false;
    if (!(description == rhs.description))
      return false;
    if (!(image == rhs.image))
      return false;
    if (!(video == rhs.video))
      return false;
    if (!(itemTypes == rhs.itemTypes))
      return false;
    if (!(displayItems == rhs.displayItems))
      return false;
    if (!(numRecs == rhs.numRecs))
      return false;
    if (!(maxRatingVal == rhs.maxRatingVal))
      return false;
    if (!(ratingsColumn == rhs.ratingsColumn))
      return false;
    if (!(primaryKey == rhs.primaryKey))
      return false;
    return true;
  }
  bool operator != (const Recommender &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const Recommender & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

  friend std::ostream& operator<<(std::ostream& out, const Recommender& obj);
};

void swap(Recommender &a, Recommender &b);

} // namespace

#endif
