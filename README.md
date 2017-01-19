INTROSDE  Assignment 02: RESTful Services
===============

--------

The code
-------------

In the /src folder there are the Java classes; the package dao ("data access object", a typical data accessing pattern) contains LifeCoachDao the enum class that manages the connection to the database.
The 'model' package contains the Person, LifeStatus, MeasureDefinition and HealthMesureHistory java classes that map to the database tables, the 'resources' package contains classes that represent the resource in REST and the default package contains the main class and it's configuration to run the standalone server.

----------

How run the code 
---------------------

 - ```ant start``` : to install all the dependencies and to run the standalone server
 - ```ant execute.client``` : to execute the client and save the requests/responses information into the logs file

------------
