/* 
Orginal Page: http://thecodeplayer.com/walkthrough/jquery-multi-step-form-with-progress-bar 

*/
//jQuery time
var current_fs, next_fs, previous_fs; //fieldsets
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

$(".next").click(function(){
    next_fs = $(this).parent().next();
    //activate next step on progressbar using the index of next_fs
    $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
    animateNext(this, next_fs);
});

$(".previous").click(function() {
    //de-activate current step on progressbar
    current_fs = $(this).parent();
    $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
    animatePrevious(this, $(this).parent().prev());
});

$("#option-1").click(function() {
    animateNext(this, $("#option-1-form"));
});

$("#option-2").click(function() {
    animateNext(this, $("#option-2-form"));
});

$(".previous-opt-1").click(function(){
    animatePrevious(this, $("#option-1-form-1"));
});

$(".previous-opt-2").click(function(){
    animatePrevious(this, $("#customization-form"));
});

$(".next-opt-1").click(function(){
    animateNext(this, $("#option-1-form-3"));
});


$(".next-opt-2").click(function(){
    if ($("#item-based-button").hasClass('button-active') && $("#ratings-based-button").hasClass('button-active')) {
        next_fs = $("#item-rating-based-recommender-option-1");
    } else if ($("#item-based-button").hasClass('button-active')) {
        next_fs = $("#item-based-recommender-option-1");   
    } else if ($("#ratings-based-button").hasClass('button-active')) {
        next_fs = $("#ratings-based-recommender-option-1");
    } else {
        next_fs = $("#option-1-form-3");
    }
    
    animateNext(this, next_fs);
});

$(".next-opt-2").click(function(){
    if ($("#item-based-button").hasClass('button-active') && $("#ratings-based-button").hasClass('button-active')) {
        next_fs = $("#item-rating-based-recommender-option-1");
    } else if ($("#item-based-button").hasClass('button-active')) {
        next_fs = $("#item-based-recommender-option-1");   
    } else if ($("#ratings-based-button").hasClass('button-active')) {
        next_fs = $("#ratings-based-recommender-option-1");
    } else {
        next_fs = $("#option-1-form-3");
    }
    animateNext(this, next_fs);
});

$(".next-opt-2-info").click(function(){
    if ($("#item-based-button-2").hasClass('button-active') && $("#ratings-based-button-2").hasClass('button-active')) {
        next_fs = $("#item-rating-based-recommender-option-2");
    } else if ($("#item-based-button-2").hasClass('button-active')) {
        next_fs = $("#item-based-recommender-option-2");   
    } else if ($("#ratings-based-button-2").hasClass('button-active')) {
        next_fs = $("#ratings-based-recommender-option-2");
    }
    
    animateNext(this, next_fs);
});

$(".previous-opt-2-form").click(function(){
    animatePrevious(this, $("#option-2-form-1"));
});

$(".next-recommender-types-opt-2").click(function(){
    $("#progressbar li").eq(3).addClass("active");
    animateNext(this, $("#option-2-form-1"));
});

$(".previous-opt-2-info").click(function(){
    $("#progressbar li").eq(3).removeClass("active");
    animatePrevious(this, $("#option-2-form"));
});

function setColor(e) {
   var target = e.target,
       status = e.target.classList.contains('button-active');

   e.target.classList.add(status ? 'button-inactive' : 'button-active');
   e.target.classList.remove(status ? 'button-active' : 'button-inactive'); 
}

$(".submit").click(function(){
	return false;
})
