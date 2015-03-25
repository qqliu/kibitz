/*
Orginal Page: http://thecodeplayer.com/walkthrough/jquery-multi-step-form-with-progress-bar

*/
//jQuery time
var current_fs, next_fs, previous_fs, last_info_page, option_1 = false, client_id = null; //fieldsets

//Kibitz fieldsets
var transport, protocol, client;
transport = new Thrift.Transport("//localhost:9889/kibitz/");
protocol = new Thrift.Protocol(transport);
client = new kibitz.RecommenderServiceClient(protocol);

var left, opacity, scale; //fieldset properties which we will animate
var animating; //flag to prevent quick multi-click glitches

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
    $("#login-form").show();
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
    $("#progressbar li").eq(3).removeClass("active");
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
    if (validFields(['rating-column-2'])) {
        animateNext(this, $("#submit-info"));
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
    animatePrevious(this, $("#" + last_info_page));
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
            if ($("#email").val() === null || $("#email").val() === undefined || $("#email").val() === "" || $("#email").val().indexOf("@") === -1) {
                $("#email").css("border", "2px solid red");
                missing_fields["email"] = true;
            } else {
                $("#email").css("border", "1px solid #ccc");
            }
        } else if (fields[i] === "confirm_password") {
            if ($("#password").val() === null || $("#password").val() === undefined || $("#password").val() === "" || ($("#password").val() !== $("#confirm_password").val())) {
                $("#confirm_password").css("border", "2px solid red");
                missing_fields["confirm_password"] = true;
            } else {
                $("#confirm_password").css("border", "1px solid #ccc");
            }
        } else {
            if ($("#" + fields[i]).val() === null || $("#" + fields[i]).val() === undefined || $("#" + fields[i]).val() === "") {
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

    if (!resubmit) {
        next_fs = $(cur).parent().next();
        //activate next step on progressbar using the index of next_fs
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
        animateNext(cur, next_fs);
    }
}

function validFields(fields) {
    var resubmit = false;
    var missing_fields = {};
    for (i in fields) {
        if ($("#" + fields[i]).val() === null || $("#" + fields[i]).val() === undefined || $("#" + fields[i]).val() === "") {
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
    $("#login-panel").hide();
    $("#successful-login").show();
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
    var email, password, dh_username, dh_password, dh_repo, dh_tablename, user_based,
	item_based, ratings_based, random, primary_key, display_columns, item_based_col_1, item_based_col_2,
	item_based_col_3, ratings_based_col;

    email = $("#email").val();
    password = $("#password").val();
    dh_username = $("#dh-username").val();
    dh_password = $("#dh-password").val()
    dh_repo = $("#dh-repository").val();
    dh_tablename = $("#dh-table-name").val();
    if ($("#option-1").hasClass("button-active")) {
	primary_key = "title";
	display_columns = ["title", "description", "image"];
        if ($("#user-based-button").hasClass("button-active")) {
            user_based = true;
        }

        if ($("#item-based-button").hasClass("button-active") && $("#ratings-based-button").hasClass("button-active")) {
	    item_based_col_1 = $("#rating-column-1").val();
            ratings_based_col = $("#item-based-col-name-1").val();
            if ($("#item-based-col-name-2-1").val() !== "") {
                item_based_col_2 = $("#item-based-col-name-2-1").val();
            }
            if ($("#item-based-col-name-3-1").val() !== "") {
                item_based_col_3 = $("#item-based-col-name-3-1").val();
            }
        } else if ($("#item-based-button").hasClass("button-active")) {
            item_based_col_1 = $("#item-based-col-name-2").val();
            if ($("#item-based-col-name-2-2").val() !== "") {
                item_based_col_2 = $("#item-based-col-name-2-2").val();
            }
            if ($("#item-based-col-name-3-2").val() !== "") {
                item_based_col_3 = $("#item-based-col-name-3-2").val();
            }
        } else if ($("#ratings-based-button").hasClass("button-active")) {
            ratings_based_col = $("#rating-column-2").val();
        }

        if ($("#random-button").hasClass("button-active")) {
            random = true;
        }
    } else {
        primary_key = $("#primary-key").val();
        display_columns = $("#display-columns").val();
        if ($("#user-based-button-2").hasClass("button-active")) {
            user_based = true;
        }

        if ($("#item-based-button-2").hasClass("button-active") && $("#ratings-based-button-2").hasClass("button-active")) {
            ratings_based_col = $("#rating-column-3").val();
            item_based_col_1 = $("#item-based-col-name-3").val();
            if ($("#item-based-col-name-2-3").val() !== "") {
                item_based_col_2 = $("#item-based-col-name-2-3").val();
            }
            if ($("#item-based-col-name-3-3").val() !== "") {
                item_based_col_3 = $("#item-based-col-name-3-3").val();
            }
        } else if ($("#item-based-button-2").hasClass("button-active")) {
            item_based_col_1 = $("#item-based-col-name-4").val();
            if ($("#item-based-col-name-2-4").val() !== "") {
                item_based_col_2 = $("#item-based-col-name-2-4").val();
            }
            if ($("#item-based-col-name-3-4").val() !== "") {
                item_based_col_3 = $("#item-based-col-name-3-4").val();
            }
        } else if ($("#ratings-based-button-2").hasClass("button-active")) {
            ratings_based_col = $("#rating-column-4").val();
        }

        if ($("#random-button-2").hasClass("button-active")) {
            random = true;
        }
    }

    client_id = printClientId();
    
    if ($("#option-1").hasClass("button-active")) {
	printExtraOptions(window.location + dh_username + "/" + dh_repo, window.location + "/" + dh_username + "/" + dh_repo + "/homepage.zip");
    }
    
    transport.open();
    debugger;
    client.createNewRecommender(dh_username, primary_key, dh_password, dh_repo, dh_tablename, item_based_col_1, item_based_col_2, item_based_col_3, "p", "p", "p", display_columns, client_id);

    $("#submit-info").hide();
    $("#give-kibitz-key").show();
    return false;
});

$(function() {
    $( "#dialog" ).dialog();
});
