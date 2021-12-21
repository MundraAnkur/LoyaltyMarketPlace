# LoyaltyMarketPlace
>  * Ankur Mundra,           amundra, 
>  * Shruti Satish Magai,    smagai   

* This package contains two jar files *LoyaltyMarketPlace_T21.jar* and *LoyaltyMarketPlaceWithPortForwading_T21.jar*
* Also I've provided two properties file for the two jars namely project.properties and project_tunnel.properties these files can be used when you want to change some configuration like database user name, password, server port, etc.

**LoyaltyMarketPlaceWithPortForwading_T21.jar**
* As the database is at the remote server, we need to create SSH Tunnel to connect to the database using port forwarding mechanism
* The SSH Tunnel is created at the time of spring boot application initialization and we have used port 4321 as the forwarding port
* Datasource url changes from jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl01 to jdbc:oracle:thin:@localhost:4321:orcl01

**LoyaltyMarketPlace_T21.jar**
* If we are executing the jar file on the same server where database is hosted then database can be connected directly without SSH Tunneling
* Datasource url: jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl01

## Setup & Execution
 * Before Executing the jar file execute the sql scripts(loyalty_schema.sql and triggers_and_procedure.sql) available in the package.
 * Spring Boot application can be executed using **any** of the following commands:
   * > java -jar LoyaltyMarketPlace_T21.jar or java -jar LoyaltyMarketPlaceWithPortForwading_T21.jar
   * You can also provide the properties file as an argument during the execution:
      * > java -jar LoyaltyMarketPlace_T21.jar --spring.config.name=project
 * After executing the jar file go to chrome browser and browse (http://localhost:port i.e, http://localhost:8099) to perform the operations
 
 ## properties file attributes
 * server.port=8099
 * spring.datasource.url=jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl01
 * spring.datasource.username=abc (db username)
 * spring.datasource.password=xyz (password)


 
