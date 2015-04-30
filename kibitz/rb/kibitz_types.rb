#
# Autogenerated by Thrift Compiler (0.9.1)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#

require 'thrift'

class Item
  include ::Thrift::Struct, ::Thrift::Struct_Union
  ATTRIBUTES = 1
  KIBITZ_GENERATED_ID = 2

  FIELDS = {
    ATTRIBUTES => {:type => ::Thrift::Types::MAP, :name => 'attributes', :key => {:type => ::Thrift::Types::STRING}, :value => {:type => ::Thrift::Types::STRING}},
    KIBITZ_GENERATED_ID => {:type => ::Thrift::Types::I64, :name => 'kibitz_generated_id'}
  }

  def struct_fields; FIELDS; end

  def validate
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field attributes is unset!') unless @attributes
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field kibitz_generated_id is unset!') unless @kibitz_generated_id
  end

  ::Thrift::Struct.generate_accessors self
end

class Recommender
  include ::Thrift::Struct, ::Thrift::Struct_Union
  USERNAME = 1
  RECOMMENDERNAME = 2
  CLIENTKEY = 3
  HOMEPAGE = 4
  REPONAME = 5
  TITLE = 6
  DESCRIPTION = 7
  IMAGE = 8
  VIDEO = 9
  ITEMTYPES = 10
  DISPLAYITEMS = 11
  NUMRECS = 12
  MAXRATINGVAL = 13
  RATINGSCOLUMN = 14
  PRIMARYKEY = 15

  FIELDS = {
    USERNAME => {:type => ::Thrift::Types::STRING, :name => 'username'},
    RECOMMENDERNAME => {:type => ::Thrift::Types::STRING, :name => 'recommenderName'},
    CLIENTKEY => {:type => ::Thrift::Types::STRING, :name => 'clientKey'},
    HOMEPAGE => {:type => ::Thrift::Types::STRING, :name => 'homepage'},
    REPONAME => {:type => ::Thrift::Types::STRING, :name => 'repoName'},
    TITLE => {:type => ::Thrift::Types::STRING, :name => 'title'},
    DESCRIPTION => {:type => ::Thrift::Types::STRING, :name => 'description'},
    IMAGE => {:type => ::Thrift::Types::STRING, :name => 'image'},
    VIDEO => {:type => ::Thrift::Types::STRING, :name => 'video'},
    ITEMTYPES => {:type => ::Thrift::Types::MAP, :name => 'itemTypes', :key => {:type => ::Thrift::Types::STRING}, :value => {:type => ::Thrift::Types::STRING}},
    DISPLAYITEMS => {:type => ::Thrift::Types::LIST, :name => 'displayItems', :element => {:type => ::Thrift::Types::STRING}},
    NUMRECS => {:type => ::Thrift::Types::I64, :name => 'numRecs'},
    MAXRATINGVAL => {:type => ::Thrift::Types::I64, :name => 'maxRatingVal'},
    RATINGSCOLUMN => {:type => ::Thrift::Types::STRING, :name => 'ratingsColumn'},
    PRIMARYKEY => {:type => ::Thrift::Types::STRING, :name => 'primaryKey'}
  }

  def struct_fields; FIELDS; end

  def validate
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field username is unset!') unless @username
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field recommenderName is unset!') unless @recommenderName
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field clientKey is unset!') unless @clientKey
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field homepage is unset!') unless @homepage
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field repoName is unset!') unless @repoName
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field title is unset!') unless @title
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field description is unset!') unless @description
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field image is unset!') unless @image
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field numRecs is unset!') unless @numRecs
    raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Required field maxRatingVal is unset!') unless @maxRatingVal
  end

  ::Thrift::Struct.generate_accessors self
end

