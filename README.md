INTROSDE  Assignment 02: RESTful Services
===============

--------

The code
-------------

In the /src folder there are the Java classes; the package dao ("data access object", a typical data accessing pattern) contains LifeCoachDao the enum class that manages the connection to the database.
The 'model' package contains the Person, LifeStatus, MeasureDefinition and HealthMesureHistory java classes that map to the database tables, the 'resources' package contains classes that represent the resource in REST and the default package contains the main class and it's configuration to run the standalone server.

The 'client' package contains the main class to be run to test the server, of my partner student, with the requests in both XML and JSON and to write the log files (client-server-xml.log and client-server-json.log) in the root folder.



----------

How run the code 
---------------------
The code can be run simply execute in the terminal ```git clone https://github.com/michelebof/introsde-2016-assignment-2```
Then:
 - ```ant start``` : to install all the dependencies and to run the standalone server
 - ```ant execute.client``` : to execute the client and save the requests/responses information into the logs file

------------

#### My heroku server:
https://introsde2016-assignment2.herokuapp.com/assignment/

#### Information of my partner student:
Name:	Sara Gasperetti

Link git:	https://github.com/SaraGasperetti

Link server heroku:	https://introsde2016-assignment-2.herokuapp.com/sdelab/ 


