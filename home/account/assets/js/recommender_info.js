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
    
    $("#homepage").val(recommenders[client_id].homepage);
    
    console.log(recommenders[client_id].repoName);
    console.log(recommenders[client_id].recommenderName);
    console.log(username);
    var columns = client.getColumns(username, recommenders[client_id].repoName, recommenders[client_id].recommenderName);
    console.log(columns);
    console.log(recommenders[client_id]);
    console.log(recommenders[client_id].primaryKey);
    
    $(".columns").empty();
    $(".columns").append('<option value=""></option>');
    for (var i in columns) {
        if (columns[i] === recommenders[client_id].primaryKey)
            $("#primary_key").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
        else
            $("#primary_key").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
    }

    for (var i in columns) {
        if (columns[i] === recommenders[client_id].title)
            $("#title").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
        else
            $("#title").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
    }
  
        for (var i in columns) {
            if (columns[i] === recommenders[client_id].description)
                $("#description").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
            else
                $("#description").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
	}
 
    for (var i in columns) {
        if (columns[i] === recommenders[client_id].image)
            $("#image").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
        else
            $("#image").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
    }
    
    for (var i in columns) {
        if (columns[i] === recommenders[client_id].video)
            $("#video").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
        else
            $("#video").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
    }
                
    for (var i in columns) {
        if (columns[i] === recommenders[client_id].ratingsColumn)
            $("#overall_rating").append('<option value="' + columns[i] + '" selected>' + columns[i] + '</option>');
        else
            $("#overall_rating").append('<option value="' + columns[i] + '">' + columns[i] + '</option>');
    }
        
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
    
    $(".rec-select").chosen({allow_single_deselect:true});
});