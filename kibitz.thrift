namespace php kibitz
namespace py kibitz
namespace csharp kibitz
namespace cpp kibitz
namespace perl kibitz
namespace d kibitz
namespace java kibitz
namespace js kibitz

service RecommenderService {
    list<list<string>> makeRecommendation(1:i32 userId, 2:i32 numRecs)
    list<list<string>> getItems()
    void recordRatings(1:i32 userId, 2:i32 itemId, 3:i32 rating)
    void deleteRatings(1:i32 userId, 2:i32 itemId)
    string createNewUser(1:string username, 2:string email, 3:string password)
    bool checkUsername(1:string username)
    bool checkLogin(1:string username, 2:string password)
    list<list<string>> getUserRatedItems(1:i32 userId)
    void initiateModel(1:string table)
}
