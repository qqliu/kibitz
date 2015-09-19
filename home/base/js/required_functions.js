var homepage = window.location.href, home_location = window.location.href;

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
    swap("login_panel", "login_message");
    eraseCookie("username");
    eraseCookie("userId");
    deleteLoginInfo();

    sessionStorage.setItem("username", "");
    sessionStorage.setItem("userId", "");
    document.location = ".";
};


function deleteLoginInfo() {
    var popup = window.open('http://datahub.csail.mit.edu/account/logout','newwindow', config='height=600,width=600,' +
            'toolbar=no, menubar=no, scrollbars=no, resizable=no,' + 'location=no, directories=no, status=no');
    popup.close();
}

var imageExists = function testImage(url, callback, id, prefix) {
    var timeout = 2000;
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


var transport = new Thrift.Transport("http://kibitz.csail.mit.edu:9889/kibitz/");
//var transport = new Thrift.Transport("http://localhost:9889/kibitz/");
var protocol = new Thrift.Protocol(transport);
var client = new kibitz.RecommenderServiceClient(protocol);

var show_signup_form = function() {
    var popup = window.open('http://datahub.csail.mit.edu/account/login?redirect_url=' + homepage,'newwindow', config='height=600,width=600,' +
			'toolbar=no, menubar=no, scrollbars=no, resizable=no,' + 'location=no, directories=no, status=no');

    //Create a trigger for location changes
    var intIntervalTime = 100;
    var curPage = this;

    // This will be the method that we use to check
    // changes in the window location.
    var fnCheckLocation = function(){
    if (popup.location === null) {
        clearInterval(id);
    }
	// Check to see if the location has changed.
	if (popup.location.href.indexOf("auth_user") > -1) {
	    var href = popup.location.href;
	    sessionStorage.setItem("username", href.split("=")[1]);
	    setCookie("username", href.split("=")[1]);
	    popup.close();
        clearInterval(id);
	    process_login(href.split("=")[1]);
	}
    }
    var id = setInterval(fnCheckLocation, intIntervalTime);
};

var display_database_items = function(items, is_rating_page) {
    var itemslist ="";
    items.sort(comparePreference);

    if (is_rating_page) {
        items.sort(compareTitle);
    }

    for (var i =0; i < items.length; i++) {
      var item = items[i];
      if (item.kibitz_generated_id !== null) {
	currItem = '<tr><tr><div class="relative">';
	if (item.attributes[image] !== null && item.attributes[image] !== undefined && item.attributes[image].indexOf("http") > -1) {
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

	if (item.attributes[title] !== null && item.attributes[title] !== undefined) {
	    currItem += '<div class="inline-block user-info"><h2 class="title">' + item.attributes[title] + '</h2>';
	}

	if (item.attributes[description] !== null && item.attributes[description] !== undefined){
	  currItem += '<div class="icons"><ul class="list-inline"><li class="description">' + item.attributes[description] + '</li>';
	}

	var object_keys = Object.keys(item_types);
	for (var j in object_keys) {
	    if (object_keys[j] !== title && object_keys[j] !== description && object_keys[j] !== image && object_keys[j] !== video) {
		var type = item_types[object_keys[j]];
		if (item.attributes[object_keys[j]] !== undefined && item.attributes[object_keys[j]] !== null && item.attributes[object_keys[j]] !== "undefined" && item.attributes[object_keys[j]] !== "null") {
			if (type === "text") {
			    currItem += "<div class='" + object_keys[j] + "'>" + item.attributes[object_keys[j]] + "</div>";
			} else if (type === "image") {
			    currItem += "<div><object class = '" + object_keys[j] + "' id='"+ object_keys[j] + item.kibitz_generated_id + "' data='" + item.attributes[object_keys[j]] + "'></object></div>";
			    var extra_imageUrl = item.attributes[object_keys[j]];
			    imageExists(extra_imageUrl, function(id, prefix) {
				    //Delete the object
				    $("#" + prefix + id).remove();
			    }, item.kibitz_generated_id, object_keys[j]);
			} else if (type === "video") {
			    currItem += "<div><object class = '" + object_keys[j] + "' id='"+ object_keys[j] + item.kibitz_generated_id + "' width='300px' height='300px' data='" + item.attributes[object_keys[j]] + "'></object></div>";
			} else if (type === "html") {
			    currItem += "<div class = '" + object_keys[j] + "'>" + item.attributes[object_keys[j]] + "</div>";
			} else if (type === "number") {
			    currItem += "<div class = '" + object_keys[j] + "'>" + item.attributes[object_keys[j]] + "</div>";
			}
		}
	    }
	}
	currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating">&nbsp;</div><div class="implementation"></div>';
	if (item.attributes[description] !== null && item.attributes[description] !== undefined) {
	  currItem += '</ul></div>';
	}

    if (((item.confidence !== 0 || item.predictedPreferences !== 0) && (item.confidence !== null && item.predictedPreferences !== null)) ||
                    (item.confidence === 0 && item.predictedPreferences > 0)) {
        if (item.confidence === 0)
            currItem += "<div class='preference-indication'>Others have rated this item highly. We hope you like it too!</div>";
        else {
            currItem += "<div class='preference-indication'>We think it is <confidence>";
            if (item.confidence === 1)
                currItem += "somewhat";
            else if (item.confidence === 2)
                currItem += "fairly";
            else
                currItem += "very";
            if (item.predictedPreferences > 5)
                currItem += "</confidence> likely you will rate this item <predicted-g>" + parseInt(item.predictedPreferences) + "</predicted-g> stars.";
            else if (item.predictedPreferences > 1)
                currItem += "</confidence> likely you will rate this item <predicted-o>" + parseInt(item.predictedPreferences) + "</predicted-o> stars.";
            else
                currItem += "</confidence> likely you will rate this item <predicted-r>1</predicted-r> star.";
        }
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
};

var popup_register_page = function() {
    var popup_sign_up = window.open('http://datahub.csail.mit.edu/account/register?redirect_url=' + home_location,'newwindow', config='height=600,width=600,' +
			'toolbar=no, menubar=no, scrollbars=no, resizable=no,' + 'location=no, directories=no, status=no');

    //Create a trigger for location changes
    var intIntervalTime = 100;
    var curPage = this;

    // This will be the method that we use to check
    // changes in the window location.
    var fnCheckLocation = function(){
	    // Check to see if the location has changed.
        if (popup_sign_up === null) {
            clearInterval(id);
            $("#creating_table_instructions").hide();
        }

        if (popup_sign_up === null || popup_sign_up.location === null) {
            popup_sign_up.close();
            popup_sign_up = null;
            clearInterval(id);
        }

        if (popup_sign_up.location.href.indexOf("auth_user") > -1) {
	        var href = popup_sign_up.location.href;
	        var splits = href.split("=");
            var userName = href.split("=")[splits.length - 1];
	        sessionStorage.setItem("username", userName);
	        setCookie("username", userName);
            clearInterval(id);
	        popup_sign_up.close();
            clearInterval(id);
	        process_login(userName);
	    }
    };
    var id = setInterval( fnCheckLocation, intIntervalTime );
};

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

function comparePreference(a,b) {
    if (a.predictedPreferences > b.predictedPreferences)
        return -1;
    if (a.predictedPreferences < b.predictedPreferences)
        return 1;
    return 0;
}

function compareTitle(a, b) {
    if (a.attributes[title] < b.attributes[title])
        return -1;
    if (a.attributes[title] > b.attributes[title])
        return 1;
    return 0;
}
