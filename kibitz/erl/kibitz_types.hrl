-ifndef(_kibitz_types_included).
-define(_kibitz_types_included, yeah).

%% struct 'Item'

-record('Item', {'attributes' = dict:new() :: dict(),
                 'kibitz_generated_id' :: integer(),
                 'confidence' :: integer(),
                 'predictedPreferences' :: float()}).
-type 'Item'() :: #'Item'{}.

%% struct 'Recommender'

-record('Recommender', {'username' :: string() | binary(),
                        'recommenderName' :: string() | binary(),
                        'clientKey' :: string() | binary(),
                        'homepage' :: string() | binary(),
                        'repoName' :: string() | binary(),
                        'title' :: string() | binary(),
                        'description' :: string() | binary(),
                        'image' :: string() | binary(),
                        'video' :: string() | binary(),
                        'itemTypes' :: dict(),
                        'displayItems' :: list(),
                        'numRecs' :: integer(),
                        'maxRatingVal' :: integer(),
                        'ratingsColumn' :: string() | binary(),
                        'primaryKey' :: string() | binary()}).
-type 'Recommender'() :: #'Recommender'{}.

-endif.
