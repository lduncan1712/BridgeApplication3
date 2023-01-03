# BridgeApplication3


An Application That Formats/Converts Responses To A Googe Forms Survey (containing messy network 'edge' references , and Static Data) Into An SQL Database. (Using Google's SheetsAPI, and JDBC)

![matchedAutomatically.png](https://user-images.githubusercontent.com/113392258/210441418-d723a14b-33a8-4529-8c81-3167b273085b.png?)


This application, heavily emphasizing parallelism, breaks each surveyee into 3 phases and threads, into an assembly lines of sorts, 
allowing different components of 3 surveyees to be worked on at once (2 automatically) by 3 unique threads

PHASE:

    -1) (FULLY AUTOMATIC)Using SheetsAPI, obtains next desired row, then formats the static information, and using a 
        scoring algorithm (A Stored Procedure Added To DB), matches the surveyee to a 'node' in the DB, then using the
        same algorithm, creates a short ranked list of probable DB 'node' matches for every edge (name referenced by surveyee), 
        stored locally with emphahis on preventing duplicate storage of 'nodes' in DB, (additionally provides user the opportunity 
        to skip row (to remove potentially invalid surveyees))
    
    -2) (MOSTLY AUTOMATIC) Given the user's selected match threshhold, matches each edge to top probable 'node' match where above 
        threshold, otherwise, highlights to user where manual matching is required, efficiently presenting a list of top probable
        matches to be simply clicked, as well as containing a built in manual search bar to query the DB, and the opportunity 
        to mark given references as duplicates, or invalid (references to someone not valid for experiment) 
        
    -3 (FULLY AUTOMATIC) Adds static information to Matched 'node', as well as adding several statistics about 
    'edge' responses, given matched network information creates or updates required 'edges', as well as updating 
    inclusion markers in matched individuals where required
        


REPLICATION/MODIFICATION:
For anyone interested in replicating/modifying this application (or whole project)
the process is quite straight forward if main structure kept intact
    
-This Project Requires Creating An OAuth Credential Token (required by SheetsAPI to access Sheet)
https://developers.google.com/workspace/guides/create-credentials
    
-You will need to create the database outlined in AAAAAAAAAAAAAAAAA
(with slight modifications to starting entries to fit your needs), ensuring you also
include the stored procedures (2)
    
-Update ConfigController.java file to select your DB connection, SheetId, Credential File Path, 
(an increase or decrease to static or edge columns in Spreadsheet/Form is also easily modified within aswell,
assuming no further deviation from core structure (SEE SSSSSSSSSSSSSSSSSS))
    
    
    
    
    
    
    
    
    
