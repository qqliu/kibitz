<?php
    $GLOBALS['THRIFT_ROOT'] = 'thrift';
    // Load up all the thrift stuff
    require_once($GLOBALS['THRIFT_ROOT'].'/Thrift.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/exception/TException.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/exception/TTransportException.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/factory/TStringFuncFactory.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/protocol/TBinaryProtocol.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/transport/TSocket.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/transport/TBufferedTransport.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/stringFunc/TStringFunc.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/stringFunc/Core.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/type/TType.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/type/TMessageType.php');
    require_once($GLOBALS['THRIFT_ROOT'].'/RecommenderService.php');
    
    // Create a thrift connection
    //$socket = new Thrift\Transport\TSocket('kibitz.csail.mit.edu', '9888');
    $socket = new Thrift\Transport\TSocket('localhost', '9888');
    $transport = new Thrift\Transport\TBufferedTransport($socket);
    $protocol = new Thrift\Protocol\TBinaryProtocol($transport);
    // Create a reposearch service client
    $client = new kibitz\RecommenderServiceClient($protocol);
    
    // Open up the connection
    $transport->open();
    $client -> initiateModel('f6afe418118814ae1c62aeae803ab049');
    
    $recommendations = $client->makeRecommendation(999999, 10);
    foreach($recommendations as $rec) {
      echo implode(", ", $rec);
    }
    
    /*$allitems = $client -> getItems();
    foreach($allitems as $item) {
      echo implode(", ", $item);
    }*/
    
    $client -> createNewUser("quanquan", "quanquan@mit.edu", "HelloThere123!");
    $user = $client -> recordRatings(999996, 1000, 10);
    echo $user;
    $login = $client -> checkLogin("quanquan", "HelloThere123!");
    echo $login;
    $login2 = $client -> checkLogin("quanquan", "HelloThere123");
    echo $login2;
    $myratings = $client -> getUserRatedItems(999999);
    foreach($myratings as $rating) {
      echo implode(", ", $rating);
    }
?>

<html> 
<body>
  <form enctype="multipart/form-data" action="upload_file.php" method="post">
    Choose a file to upload: <input name="uploaded_file" type="file" />
    <input type="submit" value="Upload" />
  </form> 
</body> 
</html>

<!--<html>
  <body>
  
    <form action="upload_file.php" method="post"
    enctype="multipart/form-data">
      <label for="file">Filename:</label>
      <input type="file" name="file" id="file"><br>
      <input type="submit" name="submit" value="Submit">
    </form>
  
  </body>
</html>-->