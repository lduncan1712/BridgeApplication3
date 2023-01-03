# BridgeApplication3


An Application That Formats/Converts Responses To A Googe Forms Survey (containing messy network 'edge' references , and Static Data) Into An SQL Database. (Using Google's SheetsAPI, and JDBC)

![Visual: Phase 1,2,3](/BridgeApplication3/visuals/simulatedStart.png?raw=true "3 Surveyees")

This application, heavily emphasizing parallelism, breaks each surveyee into 3 phases, into an assembly lines of sorts, 
allowing different components of 3 surveyees to be worked on at once (2 automatically)

PHASE:

    -1) (FULLY AUTOMATIC)Using SheetsAPI, obtains next desired row, then formats the static information, and using a 
        scoring algorithm (A Stored Procedure Added To DB), matches the surveyee to a 'node' in the DB, then using the
        same algorithm, creates a short ranked list of probable DB 'node' matches for every edge (name referenced by surveyee), 
        stored locally with emphahis on preventing duplicate storage of 'nodes' in DB, (additionally provides user the opportunity 
        to skip row (to remove potentially invalid surveyees))
    
    -2) (MOSTLY AUTOMATIC) Given the user's selected match threshhold, matches each edge to top probable 'node' match where above threshold, 
         otherwise, highlights to user where manual matching is required, efficiently presenting a list of top probable matches to be simply 
         clicked, as well as containing a built in manual search bar to query the DB, and the opportunity to mark given references as duplicates, 
         or invalid (references to someone not valid for experiment) 
        
    -3 (FULLY AUTOMATIC) Adds static information to Matched 'node', as well as adding several statistics about 'edge' responses, given matched network information 
    creates or updates required 'edges', as well as updating inclusion markers in matched individuals where required
        


Automatic Processes:
    -Matching Surveyee To Individual In Database (see 
    -
User Features:
    -Provides Users
