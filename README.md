# PillX-Backend

The backend for the PillX server. Current configuration is for UQ EAIT zones.

Assumes mongo database is available at local port 27017 with a database named pillx.  
Assumes traffic is appropriately redirected to port 8080.

# Running Instructions
Start application by running `./gradlew bootRun` in directory.  
If gradle wrapper is not working or not built, run `gradle build` and try again.  
If still not working, run without gradle wrapper; `gradle bootRun`.  

Java version can be changed under build.gradle. Change the `sourceCompatibility` variable. Note that changes to java version may cause errors.


