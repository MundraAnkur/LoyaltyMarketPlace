# LoyaltyMarketPlace
This package contains two jar files LoyaltyMarketPlace_T21.jar and LoyaltyMarketPlaceWithPortForwading_T21.jar

**LoyaltyMarketPlaceWithPortForwading_T21.jar**
* As the database is at the remote server, we need to create SSH Tunnel to connect to the database using port forwarding mechanism if we are connecting database from remote system.
* The SSH Tunnel is created at the time of spring boot application initialization and we have used port 4321 as the forwarding port.
* Datasource url changes from ora.csc.ncsu.edu:1521:orcl01 to localhost:4321:orcl01
