-ifndef(_kibitz_types_included).
-define(_kibitz_types_included, yeah).

%% struct item

-record(item, {attributes = dict:new() :: dict(),
               kibitz_generated_id :: integer()}).

%% struct recommender

-record(recommender, {username :: string() | binary(),
                      recommenderName :: string() | binary(),
                      clientKey :: string() | binary(),
                      homepage :: string() | binary(),
                      repoName :: string() | binary(),
                      title :: string() | binary(),
                      description :: string() | binary(),
                      image :: string() | binary(),
                      video :: string() | binary(),
                      itemTypes :: dict(),
                      displayItems :: list(),
                      numRecs :: integer(),
                      maxRatingVal :: integer(),
                      ratingsColumn :: string() | binary(),
                      primaryKey :: string() | binary()}).

-endif.
