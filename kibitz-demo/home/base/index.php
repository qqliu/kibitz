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
?>

<html>
  <head>
    <title><?php echo $name ?> Recommender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script type="text/javascript" src="rating.js"></script>
    <link rel="stylesheet" type="text/css" href="rating.css" />
    
    <!--Google Fonts-->
    <link href='http://fonts.googleapis.com/css?family=Carrois+Gothic|Alegreya+Sans&subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    
    <!--Bootshape-->
    <link href="snippet.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
    </style>
  </head>
  <body>
    <section class="bootshape">
      <div class="center-block main">
        <div class="head">
          <div class="person">
            <?php echo $name ?> Recommender
          </div>
        </div>
        <div class="navigation text-center">
          <table class="table">
            <tr>
              <td><a class="current" href="#">All Items</a></td>
              <td><a href="my_recommendations.php">My Recommendations</a></td>
	      <td><a href="my_ratings.php">My Ratings</a></td>
            </tr>
          </table>
        </div>

        <form class="search-form">
          <div class="form-group has-feedback">
            <input type="text" class="form-control" id="">
            <span class="glyphicon glyphicon-search form-control-feedback"></span>
          </div>
        </form>
        <table class="table table-striped list">
          <tr></tr>
	  <?php $items = $client -> getItems();
		foreach ($items as $item) {
		    $id = $item[0];
		    $item_name = $item[1];
		    $description = $item[2];
		    $image = $item[3];
		    echo "<tr>";
		    echo "<td>";
		    echo '<div class="relative">';
		    echo '<img src = "' . $image . '" />';
		    echo '<div class="inline-block user-info">';
		    echo "<h2>$item_name</h2>";
		    echo "<div class='icons'>";
		    echo '<ul class="list-inline">';
		    echo "<li> $description </li>";
		    echo "<div id='rate" . "$id" . "' class='rating'>&nbsp;</div><div class='implementation'></div>";
		    echo "</ul></div></div></div></td></tr>";
		}
	  ?>
        </table>
      </div>
    </section>
    <script>
        $(document).ready(function() {
                var ratings = $(".rating");
                ratings.each(function (i, el) {
                    $(el).rating('rate.php', {maxvalue: 10});
                });
        });
      </script>
  </body>
</html>