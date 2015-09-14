#
# Autogenerated by Thrift Compiler (0.9.1)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
require 5.6.0;
use strict;
use warnings;
use Thrift;

package kibitz::Item;
use base qw(Class::Accessor);
kibitz::Item->mk_accessors( qw( attributes kibitz_generated_id ) );

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  $self->{attributes} = undef;
  $self->{kibitz_generated_id} = undef;
  if (UNIVERSAL::isa($vals,'HASH')) {
    if (defined $vals->{attributes}) {
      $self->{attributes} = $vals->{attributes};
    }
    if (defined $vals->{kibitz_generated_id}) {
      $self->{kibitz_generated_id} = $vals->{kibitz_generated_id};
    }
  }
  return bless ($self, $classname);
}

sub getName {
  return 'Item';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1) 
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
      /^1$/ && do{      if ($ftype == TType::MAP) {
        {
          my $_size0 = 0;
          $self->{attributes} = {};
          my $_ktype1 = 0;
          my $_vtype2 = 0;
          $xfer += $input->readMapBegin(\$_ktype1, \$_vtype2, \$_size0);
          for (my $_i4 = 0; $_i4 < $_size0; ++$_i4)
          {
            my $key5 = '';
            my $val6 = '';
            $xfer += $input->readString(\$key5);
            $xfer += $input->readString(\$val6);
            $self->{attributes}->{$key5} = $val6;
          }
          $xfer += $input->readMapEnd();
        }
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^2$/ && do{      if ($ftype == TType::I64) {
        $xfer += $input->readI64(\$self->{kibitz_generated_id});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Item');
  if (defined $self->{attributes}) {
    $xfer += $output->writeFieldBegin('attributes', TType::MAP, 1);
    {
      $xfer += $output->writeMapBegin(TType::STRING, TType::STRING, scalar(keys %{$self->{attributes}}));
      {
        while( my ($kiter7,$viter8) = each %{$self->{attributes}}) 
        {
          $xfer += $output->writeString($kiter7);
          $xfer += $output->writeString($viter8);
        }
      }
      $xfer += $output->writeMapEnd();
    }
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{kibitz_generated_id}) {
    $xfer += $output->writeFieldBegin('kibitz_generated_id', TType::I64, 2);
    $xfer += $output->writeI64($self->{kibitz_generated_id});
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package kibitz::Recommender;
use base qw(Class::Accessor);
kibitz::Recommender->mk_accessors( qw( username recommenderName clientKey homepage repoName title description image video itemTypes displayItems numRecs maxRatingVal ratingsColumn primaryKey ) );

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  $self->{username} = undef;
  $self->{recommenderName} = undef;
  $self->{clientKey} = undef;
  $self->{homepage} = undef;
  $self->{repoName} = undef;
  $self->{title} = undef;
  $self->{description} = undef;
  $self->{image} = undef;
  $self->{video} = undef;
  $self->{itemTypes} = undef;
  $self->{displayItems} = undef;
  $self->{numRecs} = undef;
  $self->{maxRatingVal} = undef;
  $self->{ratingsColumn} = undef;
  $self->{primaryKey} = undef;
  if (UNIVERSAL::isa($vals,'HASH')) {
    if (defined $vals->{username}) {
      $self->{username} = $vals->{username};
    }
    if (defined $vals->{recommenderName}) {
      $self->{recommenderName} = $vals->{recommenderName};
    }
    if (defined $vals->{clientKey}) {
      $self->{clientKey} = $vals->{clientKey};
    }
    if (defined $vals->{homepage}) {
      $self->{homepage} = $vals->{homepage};
    }
    if (defined $vals->{repoName}) {
      $self->{repoName} = $vals->{repoName};
    }
    if (defined $vals->{title}) {
      $self->{title} = $vals->{title};
    }
    if (defined $vals->{description}) {
      $self->{description} = $vals->{description};
    }
    if (defined $vals->{image}) {
      $self->{image} = $vals->{image};
    }
    if (defined $vals->{video}) {
      $self->{video} = $vals->{video};
    }
    if (defined $vals->{itemTypes}) {
      $self->{itemTypes} = $vals->{itemTypes};
    }
    if (defined $vals->{displayItems}) {
      $self->{displayItems} = $vals->{displayItems};
    }
    if (defined $vals->{numRecs}) {
      $self->{numRecs} = $vals->{numRecs};
    }
    if (defined $vals->{maxRatingVal}) {
      $self->{maxRatingVal} = $vals->{maxRatingVal};
    }
    if (defined $vals->{ratingsColumn}) {
      $self->{ratingsColumn} = $vals->{ratingsColumn};
    }
    if (defined $vals->{primaryKey}) {
      $self->{primaryKey} = $vals->{primaryKey};
    }
  }
  return bless ($self, $classname);
}

sub getName {
  return 'Recommender';
}

sub read {
  my ($self, $input) = @_;
  my $xfer  = 0;
  my $fname;
  my $ftype = 0;
  my $fid   = 0;
  $xfer += $input->readStructBegin(\$fname);
  while (1) 
  {
    $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
    if ($ftype == TType::STOP) {
      last;
    }
    SWITCH: for($fid)
    {
      /^1$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{username});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^2$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{recommenderName});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^3$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{clientKey});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^4$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{homepage});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^5$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{repoName});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^6$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{title});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^7$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{description});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^8$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{image});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^9$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{video});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^10$/ && do{      if ($ftype == TType::MAP) {
        {
          my $_size9 = 0;
          $self->{itemTypes} = {};
          my $_ktype10 = 0;
          my $_vtype11 = 0;
          $xfer += $input->readMapBegin(\$_ktype10, \$_vtype11, \$_size9);
          for (my $_i13 = 0; $_i13 < $_size9; ++$_i13)
          {
            my $key14 = '';
            my $val15 = '';
            $xfer += $input->readString(\$key14);
            $xfer += $input->readString(\$val15);
            $self->{itemTypes}->{$key14} = $val15;
          }
          $xfer += $input->readMapEnd();
        }
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^11$/ && do{      if ($ftype == TType::LIST) {
        {
          my $_size16 = 0;
          $self->{displayItems} = [];
          my $_etype19 = 0;
          $xfer += $input->readListBegin(\$_etype19, \$_size16);
          for (my $_i20 = 0; $_i20 < $_size16; ++$_i20)
          {
            my $elem21 = undef;
            $xfer += $input->readString(\$elem21);
            push(@{$self->{displayItems}},$elem21);
          }
          $xfer += $input->readListEnd();
        }
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^12$/ && do{      if ($ftype == TType::I64) {
        $xfer += $input->readI64(\$self->{numRecs});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^13$/ && do{      if ($ftype == TType::I64) {
        $xfer += $input->readI64(\$self->{maxRatingVal});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^14$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{ratingsColumn});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
      /^15$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{primaryKey});
      } else {
        $xfer += $input->skip($ftype);
      }
      last; };
        $xfer += $input->skip($ftype);
    }
    $xfer += $input->readFieldEnd();
  }
  $xfer += $input->readStructEnd();
  return $xfer;
}

sub write {
  my ($self, $output) = @_;
  my $xfer   = 0;
  $xfer += $output->writeStructBegin('Recommender');
  if (defined $self->{username}) {
    $xfer += $output->writeFieldBegin('username', TType::STRING, 1);
    $xfer += $output->writeString($self->{username});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{recommenderName}) {
    $xfer += $output->writeFieldBegin('recommenderName', TType::STRING, 2);
    $xfer += $output->writeString($self->{recommenderName});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{clientKey}) {
    $xfer += $output->writeFieldBegin('clientKey', TType::STRING, 3);
    $xfer += $output->writeString($self->{clientKey});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{homepage}) {
    $xfer += $output->writeFieldBegin('homepage', TType::STRING, 4);
    $xfer += $output->writeString($self->{homepage});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{repoName}) {
    $xfer += $output->writeFieldBegin('repoName', TType::STRING, 5);
    $xfer += $output->writeString($self->{repoName});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{title}) {
    $xfer += $output->writeFieldBegin('title', TType::STRING, 6);
    $xfer += $output->writeString($self->{title});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{description}) {
    $xfer += $output->writeFieldBegin('description', TType::STRING, 7);
    $xfer += $output->writeString($self->{description});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{image}) {
    $xfer += $output->writeFieldBegin('image', TType::STRING, 8);
    $xfer += $output->writeString($self->{image});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{video}) {
    $xfer += $output->writeFieldBegin('video', TType::STRING, 9);
    $xfer += $output->writeString($self->{video});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{itemTypes}) {
    $xfer += $output->writeFieldBegin('itemTypes', TType::MAP, 10);
    {
      $xfer += $output->writeMapBegin(TType::STRING, TType::STRING, scalar(keys %{$self->{itemTypes}}));
      {
        while( my ($kiter22,$viter23) = each %{$self->{itemTypes}}) 
        {
          $xfer += $output->writeString($kiter22);
          $xfer += $output->writeString($viter23);
        }
      }
      $xfer += $output->writeMapEnd();
    }
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{displayItems}) {
    $xfer += $output->writeFieldBegin('displayItems', TType::LIST, 11);
    {
      $xfer += $output->writeListBegin(TType::STRING, scalar(@{$self->{displayItems}}));
      {
        foreach my $iter24 (@{$self->{displayItems}}) 
        {
          $xfer += $output->writeString($iter24);
        }
      }
      $xfer += $output->writeListEnd();
    }
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{numRecs}) {
    $xfer += $output->writeFieldBegin('numRecs', TType::I64, 12);
    $xfer += $output->writeI64($self->{numRecs});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{maxRatingVal}) {
    $xfer += $output->writeFieldBegin('maxRatingVal', TType::I64, 13);
    $xfer += $output->writeI64($self->{maxRatingVal});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{ratingsColumn}) {
    $xfer += $output->writeFieldBegin('ratingsColumn', TType::STRING, 14);
    $xfer += $output->writeString($self->{ratingsColumn});
    $xfer += $output->writeFieldEnd();
  }
  if (defined $self->{primaryKey}) {
    $xfer += $output->writeFieldBegin('primaryKey', TType::STRING, 15);
    $xfer += $output->writeString($self->{primaryKey});
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

1;