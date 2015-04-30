<?php
ini_set('memory_limit', '500M');

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
    
    include("initiate_model.php");
    
    if( $_REQUEST["rating"] )
    {
       $rating = (int) $_REQUEST['rating'];
       $item = (int) $_REQUEST['item'];
       $client -> recordRatings(999999, $item, $rating);
    }
?>