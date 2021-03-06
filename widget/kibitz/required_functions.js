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
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
    }
    return "";
};

var eraseCookie = function (name) {
    setCookie(name,"",-1);
}

var process_login = function() {
    username=document.getElementById('username').value;
    password = document.getElementById('password').value;

    var correct_login = client.checkLogin(client_key, username, password, false);
    swap("kibitz-login-message", "kibitz-login-panel");
    if (correct_login) {
       setCookie("username", username);
       userId = client.retrieveUserId(client_key, username, password);
       setCookie("userId", userId);
       document.location = ".";
    } else {
       document.getElementById('login_message').innerHTML = "<p>Wrong login info. <a href='.'>Try again.</a></p>";
    }
};

var show_register = function() {
    document.getElementById("kibitz-login-panel").innerHTML = '<p>Username: </p> <input style="color:black" id = "username" name="username" type="text" />'
	      + '<p>Email: </p> <input style="color:black" id = "email" name="email" type="text" />'
	      + '<p>Password: </p><input style="color:black" id = "password" name="password" type="password" />'
	      + '<p>Retype Password</p><input style="color:black" id = "retype_password" name="retype_password" type="password" /><br>'
	      + '<button style="color:black" type="button" onclick="process_new_user();" value="Register">Register</button><br />';
};

var show_login = function() {
    document.getElementById("kibitz-login-panel").innerHTML = '<p>Username: </p> <input style="color:black" id = "username" name="username" type="text" />'
        + '<p>Password: </p><input style="color:black" id = "password" name="password" type="password" /><br>'
        + '<button style="color:black" type="button" onclick="process_login();" value="Login">Login</button><br />'
        + '<a href="javascript:show_register();">Register New User</a>';
}

var process_new_user = function() {
    var username=document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var retype =document.getElementById('retype_password').value;
    var email = document.getElementById('email').value;

    if (retype == password) {
        client.createNewUser(client_key, username, email, password, false);
        window.location.href = ".";
    }
};

var swap = function swap(one, two) {
    document.getElementById(one).style.display = 'block';
    document.getElementById(two).style.display = 'none';
};

var logout = function() {
    swap("kibitz-login-panel", "kibitz-login-message");
    eraseCookie("username");
    eraseCookie("userId");

    document.location = ".";
};

var processPage = function(page) {
    window.location.hash = page;
    document.getElementById('kibitz-listofitems').innerHTML = "";

    var items = client.getPageItems(client_key, page, 10);
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
            $(el).rating('', {maxvalue: 10, curvalue: rating});
        } else {
            $(el).rating('', {maxvalue: 10});
        }
    });
};

var processNextPages = function(start) {
   var lastPages;
   var pages;
   if (start > 0) {
        lastPages = start - 90;
        pages = "<a href='#" + lastPages/10 + "' onclick = 'processNextPages(" + lastPages + ")'>&#10094;&nbsp;&nbsp;</a>";
   } else {
        pages = "";
   }
   for (var i = Math.max(parseInt(start) - 10, 0); i < Math.min(numPages, start+100); i += 10) {
        var displayPages = i/10 + 1;
        pages += "<a href='#" + i/10 + "' onclick='processPage(" + i/10 + ")'>" + displayPages + "&nbsp;&nbsp;</a>";
   }

   var nextPages = i - 10;
   pages += "<a href='#" + nextPages/10 + "' onclick='processNextPages(" + nextPages + ")'>&#10095;&nbsp;&nbsp;</a>";
   processPage(start/10);
   document.getElementById("kibitz-pages").innerHTML = pages;
};

var transport = new Thrift.Transport("http://kibitz.csail.mit.edu:9888/kibitz/");
//var transport = new Thrift.Transport("http://localhost:9888/kibitz/");
var protocol = new Thrift.Protocol(transport);
var client = new kibitz.RecommenderServiceClient(protocol);

$(document).ready(function() {
    transport.open();
    client.createNewIndividualServer(client_key);
    client.initiateModel(client_key, datahub_table, datahub_username, datahub_password, datahub_repo);
    if ($("#title").length != 0)
        document.getElementById("title").innerHTML = title + " Recommender";
    if($("#search").length != 0) {
        $("#search").keyup(function(ev) {
            if (ev.which === 13) {
                var items = client.getSearchItems(client_key, $("#search").val());
    
                document.getElementById('kibitz-listofitems').innerHTML = "";
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
                                                                                        $(el).rating('', {maxvalue: 10, curvalue: rating});
                                                                                                } else {
                                                                                                                    $(el).rating('', {maxvalue: 10});
                                                                                                                            }
            });
            }
        });
    }
});
