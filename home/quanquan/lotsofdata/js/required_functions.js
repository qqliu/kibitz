var setCookie = function (cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
};

var getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)===' ') c = c.substring(1);
        if (c.indexOf(name) !== -1) return c.substring(name.length,c.length);
    }
    return "";
};

var eraseCookie = function (name) {
    setCookie(name,"");
}

var process_login = function(username) {
    swap("login_message", "login_panel");
    client.createNewUser(client_key, username);
    userId = client.retrieveUserId(client_key, username);
    setCookie("userId", userId);
    sessionStorage.setItem("userId", userId);
    document.location = ".";
};

var swap = function swap(one, two) {
    document.getElementById(one).style.display = 'block';
    document.getElementById(two).style.display = 'none';
};

var logout = function() {
    debugger;
    swap("login_panel", "login_message");
    eraseCookie("username");
    eraseCookie("userId");

    sessionStorage.setItem("username", "");
    sessionStorage.setItem("userId", "");
    document.location = ".";
};

var imageExists = function testImage(url, callback, id, prefix) {
    var timeout = 800;
    var timedOut = false, timer;
    var img = new Image();
    img.onerror = img.onabort = function() {
        if (!timedOut) {
            clearTimeout(timer);
            callback(id, prefix);
	    return;
        }
    };
    img.onload = function() {
        if (!timedOut) {
            clearTimeout(timer);
        } else {
	    return;   
	}
    };
    img.src = url;
    timer = setTimeout(function() {
        timedOut = true;
        callback(id, prefix);
	return;
    }, timeout); 
};

var processPage = function(page) {
    window.location.hash = page;
    document.getElementById('listofitems').innerHTML = "";
    
    var items = client.getPageItems(client_key, page, 10, display_items);
    display_database_items(items);
};

var processNextPages = function(start) {
   var lastPages;
   var pages;
   var numPages = parseInt(client.getItemCount(client_key));
   
   if (start > 0) {
        lastPages = start - 90;
        pages = "<li><a href='#" + lastPages/10 + "' onclick = 'processNextPages(" + lastPages + ")'>&#10094;</a></li>";
   } else {
        pages = "";
   }
   for (var i = Math.max(parseInt(start) - 10, 0); i < Math.min(numPages, start+100); i += 10) {
        var displayPages = i/10 + 1;
        pages += "<li><a href='#" + i/10 + "' onclick='processPage(" + i/10 + ")'>" + displayPages + "</a></li>";
   }

   var nextPages = i - 10;
   pages += "<li><a href='#" + nextPages/10 + "' onclick='processNextPages(" + nextPages + ")'>&#10095;</a></li>";
   processPage(start/10);
   
   $("#pages").empty();
   $("#pages").append(pages);
};


//var transport = new Thrift.Transport("http://kibitz.csail.mit.edu:9888/kibitz/");
var transport = new Thrift.Transport("http://localhost:9889/kibitz/");
var protocol = new Thrift.Protocol(transport);
var client = new kibitz.RecommenderServiceClient(protocol)

var show_signup_form = function() {
    var popup = window.open('http://datahub.csail.mit.edu/account/login?redirect_url=http://localhost/kibitz-demo/home/' + homepage,'newwindow', config='height=600,width=600,' +
			'toolbar=no, menubar=no, scrollbars=no, resizable=no,' + 'location=no, directories=no, status=no');

    //Create a trigger for location changes
    var intIntervalTime = 100;
    var curPage = this;

    // This will be the method that we use to check
    // changes in the window location.
    var fnCheckLocation = function(){
	// Check to see if the location has changed.
	if (popup.location.href.indexOf("auth_user") > -1) {
	    var href = popup.location.href;
	    sessionStorage.setItem("username", href.split("=")[1]);
	    setCookie("username", href.split("=")[1]);
	    popup.close();
	    process_login(href.split("=")[1]);
	}
    }
    var id = setInterval(fnCheckLocation, intIntervalTime);
};

var display_database_items = function(items) {
    var itemslist ="";
    for (var i =0; i < items.length; i++) {
      var item = items[i];
      if (item.attributes[title] !== null && item.attributes[title] !== undefined && item.kibitz_generated_id !== null) {
	currItem = '<tr><tr><div class="relative">';
	if (item.attributes[image] !== null && item.attributes[image] !== undefined && item.attributes.image.indexOf("http") > -1) {
		currItem += '<object id="profile_image' + item.kibitz_generated_id + '" data="' + item.attributes[image] + '"></object>';
		var imageUrl = item.attributes[image];
      
		imageExists(imageUrl, function(id, prefix) {
			//Delete the object
			$("#" + prefix + id).remove();
		}, item.kibitz_generated_id, "profile_image");
	}
	
	if (video && item.attributes[video] !== null && item.attributes[video] !== undefined) {
		currItem += '<object id="profile_image' + item.kibitz_generated_id + '" width="300px" height="300px" data="' + item.attributes[video] + '"></object>';
	}
	currItem += '<div class="inline-block user-info"><h2>' + item.attributes[title] + '</h2>';
	if (item.attributes[description] !== null && item.attributes[description] !== undefined){
	  currItem += '<div class="icons"><ul class="list-inline"><li>' + item.attributes[description] + '</li>';
	}

	var object_keys = Object.keys(item_types);
	for (var j in object_keys) {
	    if (object_keys[j] !== title && object_keys[j] !== description && object_keys[j] !== image && object_keys[j] !== video) {
		var type = item_types[object_keys[j]];
		if (item.attributes[object_keys[j]] !== undefined && item.attributes[object_keys[j]] !== null && item.attributes[object_keys[j]] !== "undefined" && item.attributes[object_keys[j]] !== "null") {
			if (type === "text") {
				currItem += "<div>" + item.attributes[object_keys[j]] + "</div>";
			} else if (type === "image") {
				currItem += "<div><object id='"+ object_keys[j] + item.kibitz_generated_id + "' data='" + item.attributes[object_keys[j]] + "'></object></div>";
				var extra_imageUrl = item.attributes[object_keys[j]];
				imageExists(extra_imageUrl, function(id, prefix) {
					//Delete the object
					$("#" + prefix + id).remove();
				}, item.kibitz_generated_id, object_keys[j]);
			} else if (type === "video") {
				currItem += "<div><object id='"+ object_keys[j] + item.kibitz_generated_id + "' width='300px' height='300px' data='" + item.attributes[object_keys[j]] + "'></object></div>";
			} else if (type === "html") {
				currItem += "<div>" + item.attributes[object_keys[j]] + "</div>";
			}
		}
	    }
	}
	currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating">&nbsp;</div><div class="implementation"></div>';
	if (item.attributes.description !== null) {
	  currItem += '</ul></div>';
	}
	currItem += '</div></div></td></tr>';
	itemslist += currItem;
      }
    }
    document.getElementById('listofitems').innerHTML = itemslist;

    if(userId !== null) {
      var my_rated_items = client.getUserRatedItems(client_key, userId, display_items);
      for (i = 0; i < my_rated_items.length; i++) {
	item = my_rated_items[i];
	var r = document.getElementById("rate" + item.kibitz_generated_id);
	if (r !== null) {
	    r.setAttribute("value", item.attributes.rating ? item.attributes.rating: -1);
	}
      }
    }

    var ratings = $(".rating");
    ratings.each(function (i, el) {
	var rating = parseInt($(el).attr('value'));
	if (rating > -1) {
	  $(el).rating('', {maxvalue: parseInt(maxRatingVal), curvalue: rating});
	} else {
	  $(el).rating('', {maxvalue: parseInt(maxRatingVal)});
	}
    });
}

$(document).ready(function() {
    
    transport.open();
    client.createNewIndividualServer(client_key);
    client.initiateModel(client_key, recommender_name, creator_name, repo_name);
    document.getElementById("title").innerHTML = recommender_name + ' Recommender';
    
    var new_display_items = [];
    var keys = Object.keys(item_types);
    for (var i in keys) {
        if ((item_types[keys[i]] === "text" || item_types[keys[i]] === "html") && keys[i] !== "no_kibitz_title"
	    && keys[i] !== "no_kibitz_description" && keys[i] !== "no_kibitz_image")
            new_display_items.push(keys[i]);
    }
    
    if (title != "no_kibitz_title")
	new_display_items.push(title.toString());
    
    if (description != "no_kibitz_description" )
	new_display_items.push(description.toString());
    
    $('#search').keyup(function(ev) {
        if (ev.which === 13) {
            var items = client.getSearchItems(client_key, $('#search').val(), new_display_items, display_items);

            document.getElementById('listofitems').innerHTML = "";
            display_database_items(items);
        }
    });
});