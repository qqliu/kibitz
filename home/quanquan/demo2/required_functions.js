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
    swap("login_message", "login_panel");
    if (correct_login) {
       setCookie("username", username);
       userId = client.retrieveUserId(client_key, username, password);
       setCookie("userId", userId);
       document.location = ".";
    } else {
       document.getElementById('login_message').innerHTML = "<p>Wrong login info. <a href='.'>Try again.</a></p>";
    }
};

var swap = function swap(one, two) {
    document.getElementById(one).style.display = 'block';
    document.getElementById(two).style.display = 'none';
};

var logout = function() {
    swap("login_panel", "login_message");
    eraseCookie("username");
    eraseCookie("userId");

    document.location = ".";
};

var processPage = function(page) {
    window.location.hash = page;
    document.getElementById('listofitems').innerHTML = "";
    
    var items = client.getPageItems(client_key, window.location.hash, 10);
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
    document.getElementById('listofitems').innerHTML = itemslist;
};

var transport = new Thrift.Transport("http://kibitz.csail.mit.edu:9888/kibitz/");
//var transport = new Thrift.Transport("http://localhost:9888/kibitz/");
var protocol = new Thrift.Protocol(transport);
var client = new kibitz.RecommenderServiceClient(protocol);var title = 'blah';
var client_key = 'wBroLswQKnijYo3Rt1SlFxPb4';

$(document).ready(function() {
    transport.open();
    client.createNewIndividualServer(client_key);
    client.initiateModel('wBroLswQKnijYo3Rt1SlFxPb4', 'blah', 'quanquan', 'hof9924ne@!', 'demo2');
    document.getElementById("title").innerHTML = title + ' Recommender';
    $('#search').keyup(function(ev) {
        if (ev.which === 13) {
            var items = client.getSearchItems(client_key, $('#search').val());

            document.getElementById('listofitems').innerHTML = "";
            var itemslist ="";
            for (var i =0; i < items.length; i++) {
                var item = items[i];
                if (item.id != null && item.title != null) {
                    currItem = '<tr><tr><div class="relative">';
                    if (item.image.indexOf('http') > -1) {
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
                            document.getElementById('listofitems').innerHTML = itemslist;

                                if(userId != null) {
                                                var my_rated_items = client.getUserRatedItems(client_key, userId);
                                                        for (i = 0; i < my_rated_items.length; i++) {
                                                                            item = my_rated_items[i];
                                                                                        var r = document.getElementById('rate' + item.id);
                                                                                                    if (r != null) {
                                                                                                                            r.setAttribute('value', item.rating ? item.rating: -1);
                                                                                                                                        }
                                                                                                            }
                                                            }
                                    var ratings = $('.rating');
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
});