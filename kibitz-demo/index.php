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
?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8"> 
        <title>Kibitz</title>
        <meta name="generator" content="Bootply" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css" rel="stylesheet">
        
        <!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css" type="text/css" rel="stylesheet">









        <!-- CSS code from Bootply.com editor -->
        
        <style type="text/css">
            html,body {
  height:100%;
}

h1 {
  font-family: Arial,sans-serif
  font-size:80px;
  color:#DDCCEE;
}

.lead {
	color:#DDCCEE;
}


/* Custom container */
.container-full {
  margin: 0 auto;
  width: 100%;
  min-height:100%;
  background-color:#110022;
  color:#eee;
  overflow:hidden;
}

.container-full a {
  color:#efefef;
  text-decoration:none;
}

.v-center {
  margin-top:7%;
}
  
        </style>
    </head>
    
    <!-- HTML code from Bootply.com editor -->
    
    <body  >
        
        <div class="container-full">

      <div class="row">
       
        <div class="col-lg-12 text-center v-center">
          
          <h1>Kibitz</h1>
          <p class="lead">Upload a file to <a href="\\datahub.csail.mit.edu">DataHub</a> and start recommending.</p>
          
          <br><br>
          
          <form class="col-lg-12" enctype="multipart/form-data" action="create_directory.php" method=""><br>
            <h3>DataHub Username: </h3> <input style="margin-left:0%; margin-top:30px; margin-bottom:20px;" name="username" type="text" />
            <h3>DataHub Password: </h3><input style="margin-top:30px; margin-bottom:30px;" name="password" type="password" /><br>
            <h3>DataHub Repository: </h3><input style="margin-top:30px; margin-bottom:30px;" name="repo" type="text" /><br>
            <h3>DataHub Table Name: </h3><input style="margin-top:30px; margin-bottom:30px;" name="tablename" type="text" /><br>
            <input type="submit" value="Create" />
          </form>
        </div>
        
      </div> <!-- /row -->
  
  	  <div class="row">
       
        <div class="col-lg-12 text-center v-center" style="font-size:39pt;">
          <a href="https://github.com/qqliu/kibitz"><i class="icon-github"></i>
        </div>
      
      </div>
  
  	<br><br><br><br><br>

</div> <!-- /container full -->


        
        <script type='text/javascript' src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


        <script type='text/javascript' src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
        
    </body>
</html>
