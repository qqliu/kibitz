var username = getCookie("username");
var userId = getCookie("userId");
var stars = $("#kibitz-listofitems").attr("max-rating");
var numPages;
$(document).ready(function() {
  if (username != "" && userId != "" && username != undefined && userId != undefined && username != null && userId != null) {
    stars = $("#kibitz-listofitems").attr("max-rating");
    $("body").append("<div id='kibitz-pages'></div>");
    
    if($("#kibitz-login-message").length != 0 && $("#kibitz-login-panel").length != 0) {
      swap("kibitz-login-message", "kibitz-login-panel");
      document.getElementById('kibitz-login-message').innerHTML = "<p>You are now logged in as " + username + ". <a href='javascript:logout();'>Logout</a></p>";
    }

    if (window.location.hash == "") {
      window.location.hash = 0;
    }

    var items = client.getPageItems(client_key, window.location.hash.split("#")[1], 10);
    var itemslist ="";
    for (var i =0; i < items.length; i++) {
      var item = items[i];
      if (item.id != null && item.title != null) {
	currItem = '<tr><tr><div class="relative">';
	if (item.image.indexOf("http") > -1) {
	  currItem += '<img src = "' + item.image + '" />';
	}
	currItem += '<div class="inline-block user-info"><h2>' + item.title + '</h2>';
	if (item.description != null){
	  currItem += '<div class="icons"><ul class="list-inline"><li>' + item.description + '</li>';
	}
	currItem += '<div id="rate' + item.id + '" class="rating">&nbsp;</div><div class="implementation"></div>';
	if (item.description != null) {
	  currItem += '</ul></div>';
	}
	currItem += '</div></div></td></tr>';
	itemslist += currItem;
      }
    }
    document.getElementById('kibitz-listofitems').innerHTML = itemslist;

    if(userId != null) {
      var my_rated_items = client.getUserRatedItems(client_key, userId);
      for (i = 0; i < my_rated_items.length; i++) {
	item = my_rated_items[i];
	var r = document.getElementById("rate" + item.id);
	if (r != null) {
	    r.setAttribute("value", item.rating ? item.rating: -1);
	}
      }
    }

    var ratings = $(".rating");
    ratings.each(function (i, el) {
	var rating = parseInt($(el).attr('value'));
	if (rating > -1) {
	  $(el).rating('', {maxvalue: parseInt(stars), curvalue: rating});
	} else {
	  $(el).rating('', {maxvalue: parseInt(stars)});
	}
    });

    numPages = parseInt(client.getItemCount(client_key));
    var pages = "";
for (var i = 0; i < Math.min(numPages, 100); i += 10) {
    var displayPages = i/10 + 1;
	    pages += "<a href='#" + i/10 + "' onclick='processPage(" + i/10 + ")'>" + displayPages + "&nbsp;&nbsp;</a>";
}
var nextPages = i - 10;
pages += "<a href='#" + nextPages/10 + "' onclick='processNextPages(" + nextPages + ")'>&#10095;&nbsp;&nbsp;</a>";
    document.getElementById("kibitz-pages").innerHTML = pages;
  } else {
    if($("#kibitz-login-panel") != 0) {
      show_login();
      document.getElementById('kibitz-listofitems').innerHTML = "<p>Please <a href='javascript:show_login();'>login</a> or <a href='javascript:show_register();'>signup</a> to see and rate items.</p>";
    } 
  }
});