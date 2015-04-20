$(document).ready(function() {
    var client_id = sessionStorage.getItem("cur_client_key");
    
    if (client_id === null || client_id === undefined)
        client_id = localStorage.getItem("cur_client_key");
    
    $("#client_key").empty();
    $("#client_key").append(client_id);
    
    $("#recommender_name").val(recommenders[client_id].recommenderName);
        
    $("#repo").empty();
    $("#repo").append(recommenders[client_id].repoName);
    
    $("#table").empty();
    $("#table").append(recommenders[client_id].repoName + "." + recommenders[client_id].recommenderName);
    
    console.log(recommenders);
    $("#homepage").val(recommenders[client_id].homepage);
    $("#primary_key").val(recommenders[client_id].primaryKey);
    
    if (recommenders[client_id].title && recommenders[client_id].title !== "no_kibitz_title" && recommenders[client_id].title !== undefined && recommenders[client_id].title !== null
        && recommenders[client_id].title !== "undefined" && recommenders[client_id].title !== "null")
        $("#title").val(recommenders[client_id].title);
        
    if (recommenders[client_id].description && recommenders[client_id].description !== "no_kibitz_description" && recommenders[client_id].description !== undefined && recommenders[client_id].description !== null
        && recommenders[client_id].description !== "undefined" && recommenders[client_id].description !== "null")
        $("#description").val(recommenders[client_id].description);
    
    if (recommenders[client_id].image && recommenders[client_id].image !== "no_kibitz_image" && recommenders[client_id].image !== undefined && recommenders[client_id].image !== null
        && recommenders[client_id].image !== "undefined" && recommenders[client_id].image !== "null" && recommenders[client_id].image.indexOf("Undefined") === -1
        && recommenders[client_id].image.indexOf("undefined") === -1)
        $("#image").val(recommenders[client_id].image);
    
    if (recommenders[client_id].video && recommenders[client_id].video !== "no_kibitz_video" && recommenders[client_id].video !== undefined && recommenders[client_id].video !== null
        && recommenders[client_id].video !== "undefined" && recommenders[client_id].video !== "null" && recommenders[client_id].video.indexOf("Undefined") === -1
        && recommenders[client_id].video.indexOf("undefined") === -1)
        $("#video").val(recommenders[client_id].video);
        
    if (recommenders[client_id].ratingsColumn && recommenders[client_id].ratingsColumn.indexOf("no_kibitz") === -1 && recommenders[client_id].ratingsColumn !== undefined && recommenders[client_id].ratingsColumn !== null
        && recommenders[client_id].ratingsColumn !== "undefined" && recommenders[client_id].ratingsColumn !== "null" && recommenders[client_id].ratingsColumn.indexOf("Undefined") === -1
        && recommenders[client_id].ratingsColumn.indexOf("undefined") === -1)
        $("#overall_rating").val(recommenders[client_id].ratingsColumn);
        
    var onDisplay = "";
    for (var i = 0; i < recommenders[client_id].displayItems.length - 1; i++) {
        debugger;
        if (recommenders[client_id].displayItems[i] !== recommenders[client_id].title && recommenders[client_id].displayItems[i] !== recommenders[client_id].description
            && recommenders[client_id].displayItems[i] !== recommenders[client_id].image && recommenders[client_id].displayItems[i] !== recommenders[client_id].video)
                onDisplay += recommenders[client_id].displayItems[i] + ",";
    }
     if (recommenders[client_id].displayItems[i] && recommenders[client_id].displayItems[i] !== recommenders[client_id].title && recommenders[client_id].displayItems[i] !== recommenders[client_id].description
            && recommenders[client_id].displayItems[i] !== recommenders[client_id].image && recommenders[client_id].displayItems[i] !== recommenders[client_id].video)
        onDisplay += recommenders[client_id].displayItems[i];
    else {
        if (onDisplay.length > 0)
            onDisplay = onDisplay.substring(0, onDisplay.length - 1);
    }
    
    $("#display_columns").empty();
    $("#display_columns").val(onDisplay);
    
    var itemTypes = "";
    var keys = Object.keys(recommenders[client_id].itemTypes);
    for (i in keys)
        itemTypes += keys[i] + ":" + recommenders[client_id].itemTypes[keys[i]] + ",";
    itemTypes = itemTypes.substring(0, itemTypes.length - 1);
    
    $("#display_item_types").empty();
    $("#display_item_types").val(itemTypes);
    
    $("#num_recs").empty();
    $("#num_recs").val(recommenders[client_id].numRecs);
    
    $("#max_rating_value").empty();
    $("#max_rating_value").val(recommenders[client_id].maxRatingVal);
    
    $("#zip-file").html("Download zip file <a href='" + homepageURL + "/" + username + "/" + recommenders[client_id].repoName + "/homepage.zip'>here</a>.");
});