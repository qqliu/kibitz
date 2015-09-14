/*
Orginal Page: http://thecodeplayer.com/walkthrough/jquery-multi-step-form-with-progress-bar

*/
var home_location = window.location.href;

//jQuery time
var current_fs, next_fs, previous_fs, last_info_page, option_1 = false, client_id = null; //fieldsets

//Kibitz fieldsets
var transport, protocol, client;
transport = new Thrift.Transport("//localhost:9889/kibitz/");
protocol = new Thrift.Protocol(transport);
client = new kibitz.RecommenderServiceClient(protocol);

var left, opacity, scale; //fieldset properties which we will animate
var animating; //flag to prevent quick multi-click glitches

var popup_sign_up = null; // managing popups

function animateNext(cur, next_fs) {
    if(animating) return false;
	animating = true;

    current_fs = $(cur).parent();
    next_fs.show();

    current_fs.animate({opacity: 0}, {
            step: function(now, mx) {
                    //as the opacity of current_fs reduces to 0 - stored in "now"
                    //1. scale current_fs down to 80%
                    scale = 1 - (1 - now) * 0.2;
                    //2. bring next_fs from the right(50%)
                    left = (now * 50)+"%";
                    //3. increase opacity of next_fs to 1 as it moves in
                    opacity = 1 - now;
                    current_fs.css({'transform': 'scale('+scale+')'});
                    next_fs.css({'left': left, 'opacity': opacity});
            },
            duration: 800,
            complete: function(){
                    current_fs.hide();
                    animating = false;
            },
            //this comes from the custom easing plugin
            easing: 'easeInOutBack'
    });
}

function animatePrevious(cur, previous_fs) {
    if(animating) return false;
	animating = true;

    current_fs = $(cur).parent();
    //show the previous fieldset
    previous_fs.show();
    //hide the current fieldset with style
    current_fs.animate({opacity: 0}, {
            step: function(now, mx) {
                    //as the opacity of current_fs reduces to 0 - stored in "now"
                    //1. scale previous_fs from 80% to 100%
                    scale = 0.8 + (1 - now) * 0.2;
                    //2. take current_fs to the right(50%) - from 0%
                    left = ((1-now) * 50)+"%";
                    //3. increase opacity of previous_fs to 1 as it moves in
                    opacity = 1 - now;
                    current_fs.css({'left': left});
                    previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
            },
            duration: 800,
            complete: function(){
                    current_fs.hide();
                    animating = false;
            },
            //this comes from the custom easing plugin
            easing: 'easeInOutBack'
    });
}

function show_signup_form(){
    $("#sign-up-form").show();
}

function show_login_form() {
    var popup = window.open('http://datahub.csail.mit.edu/account/login?redirect_url=' + home_location,'newwindow', config='height=600,width=600,' +
			'toolbar=no, menubar=no, scrollbars=no, resizable=no,' + 'location=no, directories=no, status=no');

    //Create a trigger for location changes
    var intIntervalTime = 100;
    var curPage = this;

    // This will be the method that we use to check
    // changes in the window location.
    var fnCheckLocation = function(){
	    // Check to see if the location has changed.
        if (popup.location === null) {
            popup.close();
            clearInterval(id);
        }

        if (popup.location.href.indexOf("auth_user") > -1) {
	        var href = popup.location.href;
	        var splits = href.split("=");
	        sessionStorage.setItem("username", href.split("=")[splits.length - 1]);
	        setCookie("username", href.split("=")[splits.length - 1]);
	        popup.close();
            clearInterval(id);
	        document.location.href = "account";
	    }
    }
    var id = setInterval( fnCheckLocation, intIntervalTime );
}

$(".previous").click(function() {
    //de-activate current step on progressbar
    current_fs = $(this).parent();
    $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
    animatePrevious(this, $(this).parent().prev());
});

$("#option-1").click(function() {
    if ($("#option-1").hasClass("button-active"))
        animateNext(this, $("#option-1-form-1"));
});

$("#option-2").click(function() {
    if ($("#option-2").hasClass("button-active")) {
	$("#progressbar li").eq(3).addClass("active");
        animateNext(this, $("#option-2-form"));
    }
});

$(".previous-opt-1").click(function(){
    animatePrevious(this, $("#option-1-form-1"));
});

$(".previous-opt-info").click(function(){
    animatePrevious(this, $("#" + last_info_page));
});

$(".previous-opt-2").click(function(){
    $("#progressbar li").eq(2).removeClass("active");
    animatePrevious(this, $("#customization-form"));
});

$(".next-opt-1").click(function(){
    animateNext(this, $("#option-1-form-3"));
});

$(".next-opt-1-1").click(function(){
    if (validFields(['rating-column-1', 'item-based-col-name-1'])) {
        animateNext(this, $("#submit-info"));
    }
});

$(".next-opt-1-2").click(function(){
    var verify_table_column = true, repo, table;

    username = sessionStorage.getItem("username");

    if (username === null || username === undefined || username === "" || username === "null" || username === "undefined")
	username = getCookie("username")

    repo = $("#repo-name").val().trim();
    table = $("#dh_table_name_chosen").find(".chosen-single").find("span").html().trim();

    if ($("#rating_column_2_chosen").find(".chosen-single").find("span").html().trim() !== "Ratings Column")
	verify_table_column = client.checkRatingsColumn(username, repo, table, $("#rating_column_2_chosen").find(".chosen-single").find("span").html().trim());

    if (verify_table_column) {
	$("#rating_column_error").hide();
	animateNext(this, $("#submit-info"));
    } else {
	$("#rating_column_error").show();
    }
});

$(".next-opt-1-3").click(function(){
   if (validFields(['item-based-col-name-2']))
       animateNext(this, $("#submit-info"));
});

$(".next-opt-2-1").click(function(){
   if (validFields(['rating-column-3', 'item-based-col-name-3']))
       animateNext(this, $("#submit-info"));
});

$(".next-opt-2-2").click(function(){
   if (validFields(['rating-column-4']))
       animateNext(this, $("#submit-info"));
});

$(".next-opt-2-3").click(function(){
   if (validFields(['item-based-col-name-4']))
       animateNext(this, $("#submit-info"));
});

$(".next-opt-2").click(function(){
    var selectionsMade = false, tooltip;
    option_1 = true;
    if ($("#item-based-button").hasClass('button-active') && $("#ratings-based-button").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#item-rating-based-recommender-option-1");
        last_info_page = "item-rating-based-recommender-option-1";
    } else if ($("#item-based-button").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#item-based-recommender-option-1");
        last_info_page = "item-based-recommender-option-1";
    } else if ($("#ratings-based-button").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#ratings-based-recommender-option-1");
        last_info_page = "ratings-based-recommender-option-1";
    } else if ($("#user-based-button").hasClass('button-active') || $("#random-button").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#submit-info");
        last_info_page = "option-1-form-1";
    }

    if (selectionsMade) {
        animateNext(this, next_fs);
    } else {
        tooltip = $(".tooltipContent.first");
        tooltip.fadeIn();
        setTimeout(fadeOut, 2500);
    }
});

$(".next-opt-2-info").click(function(){
    var selectionsMade = false, tooltip;

    if ($("#item-based-button-2").hasClass('button-active') && $("#ratings-based-button-2").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#item-rating-based-recommender-option-2");
        last_info_page = "item-rating-based-recommender-option-2";
    } else if ($("#item-based-button-2").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#item-based-recommender-option-2");
        last_info_page = "item-based-recommender-option-2";
    } else if ($("#ratings-based-button-2").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#ratings-based-recommender-option-2");
        last_info_page = "ratings-based-recommender-option-2";
    } else if ($("#random-button-2").hasClass('button-active') || $("#user-based-button-2").hasClass('button-active')) {
        selectionsMade = true;
        next_fs = $("#submit-info");
        last_info_page = "option-2-form-1";
    }

    if (selectionsMade) {
        animateNext(this, next_fs);
    } else {
        tooltip = $(".tooltipContent.second");
        tooltip.fadeIn();
        setTimeout(fadeOut, 2500);
    }
});

$(".previous-opt-2-form").click(function(){
    animatePrevious(this, $("#option-2-form-1"));
});

$(".next-recommender-types-opt-2").click(function(){
    if (validFields(['primary-key', 'display-columns'])) {
        $("#progressbar li").eq(3).addClass("active");
        animateNext(this, $("#option-2-form-1"));
    }
});

$(".previous-opt-2-info").click(function(){
    $("#progressbar li").eq(3).removeClass("active");
    animatePrevious(this, $("#option-2-form"));
});

$(".previous-page-before-submit").click(function(){
    animatePrevious(this, $("#ratings-based-recommender-option-1"));
});

$("#sign-up-form").click(function(){
   $("#sign-up-form").hide();
});

$(".msform").click(function(e) {
    e.stopPropagation();
});

$("fieldset").click(function(e){
   e.stopPropagation();
});

$("#progressbar").click(function(e){
    e.stopPropagation();
});

$("#login-form").click(function(e){
   $("#login-form").hide();
});

$("#login-kibitz-account").click(function(e) {
    if (validFields(["repo-name"])) {

    if (popup_sign_up === null) {
	    popup_sign_up = window.open('http://datahub.csail.mit.edu/permissions/apps/allow_access/kibitz/' + $("#repo-name").val().trim() +
			    '?redirect_url=' + home_location,'newwindow', config='height=600,width=600,' +
			    'toolbar=no, menubar=no, scrollbars=no, resizable=no,' +
			    'location=no, directories=no, status=no');
    } else {
        popup_sign_up.location.href = 'http://datahub.csail.mit.edu/permissions/apps/allow_access/kibitz/' + $("#repo-name").val().trim() +
			    '?redirect_url=' + home_location;
    }

	//Create a trigger for location changes
	var intIntervalTime = 1000;
	var curPage = this;

	// This will be the method that we use to check
	// changes in the window location.
	var fnCheckLocation = function(){
	    // Check to see if the location has changed.
	    if (popup_sign_up.location.href.indexOf("auth_user") > -1) {
		var href = popup_sign_up.location.href;
		var splits = href.split("=");
		username = href.split("=")[splits.length - 1];
		sessionStorage.setItem("username", username);
		setCookie("username", username);
		window.clearInterval(id);
		popup_sign_up.close();
		var tables = client.getTables(username, $("#repo-name").val());
		$(".tables").empty();
		$(".tables").append('<option value=""></option>');
		for (var i in tables) {
		    $(".tables").append('<option value="' + tables[i] + '">' + tables[i] + '</option>');
		}
		$(".columns").empty();
		$(".columns").append('<option value=""></option>');
        $(".columns").chosen({width: "95%", allow_single_deselect:true});
		$(".tables").chosen({width: "95%", allow_single_deselect:true}).change(function(evt, params) {
		    var columns = client.getColumns(username, $("#repo-name").val(), params.selected);
		    $(".columns").empty();
		    $(".columns").append('<option value=""></option>');
		    for (var j in columns) {
			    $(".columns").append('<option value="' + columns[j] + '">' + columns[j] + '</option>');
		    }
		    $(".columns").trigger("chosen:updated");
		});

		animateNext(curPage, $("#customization-form"));
		$("#progressbar li").eq(1).addClass("active");
	    }
	}
	var id = setInterval( fnCheckLocation, intIntervalTime );
    }
});

function setColor(e) {
   var target = e.target,
       status = e.target.classList.contains('button-active');

   e.target.classList.add(status ? 'button-inactive' : 'button-active');
   e.target.classList.remove(status ? 'button-active' : 'button-inactive');
}

function setUniqueColor(e) {
   var target = e.target,
       status = e.target.classList.contains('button-active');

   e.target.classList.add(status ? 'button-inactive' : 'button-active');
   e.target.classList.remove(status ? 'button-active' : 'button-inactive');

   if (e.target.id === "option-1") {
        if ($("#option-2").hasClass("button-active")) {
            $("#option-2").removeClass("button-active");
            $("#option-2").addClass("button-inactive");
        }
   } else {
        if ($("#option-1").hasClass("button-active")) {
            $("#option-1").removeClass("button-active");
            $("#option-1").addClass("button-inactive");
        }
   }
}

function validFieldsNext(cur, fields) {
    var resubmit = false;
    var missing_fields = {"email": false, "confirm_password": false};
    for (i in fields) {
        if (fields[i] === "email") {
            if ($("#email").val().trim() === null || $("#email").val().trim() === undefined || $("#email").val().trim() === "" || $("#email").val().trim().indexOf("@") === -1) {
                $("#email").css("border", "2px solid red");
                missing_fields["email"] = true;
            } else {
                $("#email").css("border", "1px solid #ccc");
            }
        } else if (fields[i] === "confirm_password") {
            if ($("#password").val().trim() === null || $("#password").val().trim() === undefined || $("#password").val().trim() === "" || ($("#password").val().trim() !== $("#confirm_password").val().trim())) {
                $("#confirm_password").css("border", "2px solid red");
                missing_fields["confirm_password"] = true;
            } else {
                $("#confirm_password").css("border", "1px solid #ccc");
            }
	} else if (fields[i] === "dh-table-name") {
	    if ($("#dh_table_name_chosen").find(".chosen-single").find("span").html().trim() == "Table Name") {
		missing_fields["dh_table_name_chosen"] = true;
	    }
	} else if(fields[i] === "primary-key") {
	    if ($("#primary_key_chosen").find(".chosen-single").find("span").html().trim() == "ID Column") {
		missing_fields["primary_key_chosen"] = true;
	    }
	} else {
            if ($("#" + fields[i]).val().trim() === null || $("#" + fields[i]).val().trim() === undefined || $("#" + fields[i]).val().trim() === "") {
                $("#" + fields[i]).css("border", "2px solid red");
                missing_fields[fields[i]] = true;
            } else {
                $("#" + fields[i]).css("border", "1px solid #ccc");
                missing_fields[fields[i]] = false;
            }
        }
    }

    for (i in fields) {
        if (missing_fields[fields[i]] === true) {
            return false;
        }
    }

    var username, repo, table, primary_key, title, description, image, correct_table_info;
    username = sessionStorage.getItem("username");

    if (username === null || username === undefined || username === "undefined" || username === "null")
	username = getCookie("username");

    repo = $("#repo-name").val().trim();
    table = $("#dh_table_name_chosen").find(".chosen-single").find("span").html().trim();
    primary_key = $("#primary_key_chosen").find(".chosen-single").find("span").html().trim();
    title = $("#title_column_chosen").find(".chosen-single").find("span").html().trim();
    description = $("#description_column_chosen").find(".chosen-single").find("span").html().trim();
    image = $("#image_column_chosen").find(".chosen-single").find("span").html().trim();

    if (title === "Title Column") {
	title = "no_kibitz_title";
    }

    if (description === "Description Column") {
	description = "no_kibitz_description";
    }

    if (image === "Image Column") {
	image = "no_kibitz_image";
    }

    correct_table_info = client.checkCorrectDatahubLogin(username, repo, table, primary_key, title, description, image);

    if (!correct_table_info) {
	$("#database_table_error").show();
	return false;
    } else {
	$("#database_table_error").hide();
	if (!resubmit) {
	    next_fs = $(cur).parent().next();
	    //activate next step on progressbar using the index of next_fs
	    $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
	    animateNext(cur, next_fs);
	}
    }
    return true;
}

function createNewRecommender(cur, fields) {
    var resubmit = false;
    var missing_fields = {"email": false, "confirm_password": false};
    for (i in fields) {
        if (fields[i] === "email") {
            if ($("#email").val().trim() === null || $("#email").val().trim() === undefined || $("#email").val().trim() === "" || $("#email").val().trim().indexOf("@") === -1) {
                $("#email").css("border", "2px solid red");
                missing_fields["email"] = true;
            } else {
                $("#email").css("border", "1px solid #ccc");
            }
        } else if (fields[i] === "confirm_password") {
            if ($("#password").val().trim() === null || $("#password").val().trim() === undefined || $("#password").val().trim() === "" || ($("#password").val().trim() !== $("#confirm_password").val().trim())) {
                $("#confirm_password").css("border", "2px solid red");
                missing_fields["confirm_password"] = true;
            } else {
                $("#confirm_password").css("border", "1px solid #ccc");
            }
        } else {
            if ($("#" + fields[i]).val().trim() === null || $("#" + fields[i]).val().trim() === undefined || $("#" + fields[i]).val().trim() === "") {
                $("#" + fields[i]).css("border", "2px solid red");
                missing_fields[fields[i]] = true;
            } else {
                $("#" + fields[i]).css("border", "1px solid #ccc");
                missing_fields[fields[i]] = false;
            }
        }
    }

    for (i in fields) {
        if (missing_fields[fields[i]] === true) {
            return false;
        }
    }

    if (!client.checkCorrectDatahubLogin($('#dh-username').val().trim(), $('#dh-password').val().trim(), $('#dh-repository').val().trim(), $("#dh_table_name_chosen").find(".chosen-single").find("span").html().trim())) {
	$("#incorrect-datahub-login").show();
	return false;
    }
    $("#incorrect-datahub-login").hide();
    if (!resubmit) {
	next_fs = $(cur).parent().next();
	//activate next step on progressbar using the index of next_fs
	$("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
	animateNext(cur, next_fs);
    }
    return true;
}

function validFields(fields) {
    var resubmit = false;
    var missing_fields = {};
    for (i in fields) {
        if ($("#" + fields[i]).val().trim() === null || $("#" + fields[i]).val().trim() === undefined || $("#" + fields[i]).val().trim() === "") {
            $("#" + fields[i]).css("border", "2px solid red");
            missing_fields[fields[i]] = true;
        } else {
            $("#" + fields[i]).css("border", "1px solid #ccc");
            missing_fields[fields[i]] = false;
        }
    }

    for (i in fields) {
        if (missing_fields[fields[i]] === true) {
            return false;
        }
    }
    return true;
}

function submitLoginInfo(cur) {
    //Check password
    if (validFields(['login_email', 'login_password'])) {
	sessionStorage.setItem("username", $("#login_email").val().trim());
	document.cookie="username=" + $("#login_email").val().trim();
	setCookie("username", $("#login_email").val().trim());
	document.location.href = "account";
    }
    //$("#successful-login").show();
}

function fadeOut() {
    $(".tooltipContent").fadeOut();
}

function printClientId() {
    chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

    if (client_id === null) {
	var result = '';
	for (var i = 25; i > 0; --i)
	    result += chars[Math.round(Math.random() * (chars.length - 1))];
	client_id = result;
    }

    $("#client_id").html("<b>" + client_id + "</b>");
    return client_id;
}

function printExtraOptions(url, zip) {
    $("#additional_messages").html("<p>You may visit your recommender at <a href='" + url +
				  "'>" + url + "</a>. Or you may download a zip file containing all necessary files <a href='" + zip + "'>here.</a></p>");
}

$(".submit").click(function(){
    var username, repo, table, primary_key, title, description, image, ratings_column;

    username = sessionStorage.getItem("username");
    if (username === null || username === undefined || username === "" || username === "null" || username === "undefined")
	username = getCookie("username")
    repo = $("#repo-name").val().trim();
    table = $("#dh_table_name_chosen").find(".chosen-single").find("span").html().trim();
    ratings_column = $("#rating_column_2_chosen").find(".chosen-single").find("span").html().trim();
    title = $("#title_column_chosen").find(".chosen-single").find("span").html().trim();
    description = $("#description_column_chosen").find(".chosen-single").find("span").html().trim();
    image = $("#image_column_chosen").find(".chosen-single").find("span").html().trim();
    primary_key = $("#primary_key_chosen").find(".chosen-single").find("span").html().trim();

    if (ratings_column === "Ratings Column")
	ratings_column = "no_kibitz_ratings_column";

    if (title === "Title Column") {
	title = "no_kibitz_title";
    }

    if (description === "Description Column") {
	description = "no_kibitz_description";
    }

    if (image === "Image Column") {
	image = "no_kibitz_image";
    }

    client_id = printClientId();

    printExtraOptions(home_location + username + "/" + repo, window.location + "/" + username + "/" + repo + "/homepage.zip");

    transport.open();
    client.createNewRecommender(username, primary_key, repo, table, title, description, image, ratings_column, client_id);

    $("#submit-info").hide();
    $("#give-kibitz-key").show();
    return false;
});

function setCookie(cname, cvalue, exdays) {
    if (exdays !== null) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	var expires = "expires="+d.toUTCString();
	document.cookie = cname + "=" + cvalue + "; " + expires;
    } else {
	document.cookie = cname + "=" + cvalue + "; ";
    }
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

//$("#repo-name").keyup(function(event){
  //  if(event.keyCode === 13){
    //    $("#login-kibitz-account").click();
   // }
//});

function popup_register_page() {
    popup_sign_up = window.open('http://datahub.csail.mit.edu/account/register?redirect_url=' + home_location,'newwindow', config='height=600,width=600,' +
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
            selectTable(userName);
            clearInterval(id);
	    }
    }
    var id = setInterval( fnCheckLocation, intIntervalTime );
}

function selectTable(username) {
    popup_sign_up.location.href = 'http://datahub.csail.mit.edu/create/' + username + '/repo?redirect_url=' + home_location;
    $("#creating_table_instructions").show();

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

        if (popup_sign_up.location === null) {
            popup_sign_up.close();
            popup_sign_up = null;
            clearInterval(id);
            $("#creating_table_instructions").hide();
        }

        if (client.getNumRepos(username) > 0) {
            var repoName = client.getFirstRepo(username);
	        $("#repo-name").val(repoName);
            clearInterval(id);
            $("#login-kibitz-account").click();
            $("#creating_table_instructions").hide();
	    }
    }
    var id = setInterval( fnCheckLocation, intIntervalTime );
}

$("#creating_table_instructions").click(function(){
    $("#creating_table_instructions").hide();
    popup_sign_up.close();
});
