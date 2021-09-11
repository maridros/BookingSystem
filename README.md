# BookingSystem
A distributed booking system with Java RMI Server and Client.

In the present work a Java RMI hotel room reservation system was implemented. The hotel was considered to have the following rooms:
-	30 type A rooms (single rooms) which cost 50 Euro per night 
-	45 type B rooms (double rooms) which cost 70 Euro per night
-	25 type C rooms (twin rooms) which cost 80 Euro per night
-	10 type D rooms (triple rooms) which cost 120 Euro per night
-	5 type E rooms (quad rooms) which cost 150 Euro per night 

The hotel java RMI server can handle the following requests from a client:
-	Rooms list display
-	Reservation of a number of rooms of a specific type in one name (guest)
-	Display hotel guests
-	Cancel booking of a number of rooms of a specific type by one name

In addition, in case of non-availability of several rooms for a reservation, the customer can subscribe to a list and be informed of any cancellations in reservations of the specific room type. This is achieved with the Callback method.

The Java files created to implement the above are the following: 
## Server side:
### HRServer.java 
Includes the class that has the main function and starts the Server.
### HRImpl.java
Includes the basic Server class, which contains the list of rooms and clients, as well as all the methods used by the Server. The object of this file is created in the main of HRServer.java and with RMI Registry some of its methods are communicated to the client so that it can call them.
### HRInterface.java
Includes reference to HRImpl.java methods, which the client can also access through the RMI Registry.
### Room.java
Includes information of a room type, ie the name of the type (A, B, C, D or E), the number of this type rooms available and the price. It also has methods that return this information. Used only by the Server in the HRImpl.java file which contains a list with the 5 Room type objects.
### Client.java
Includes the data of a customer, ie his name and his reservations together with the corresponding methods of returning this data. This code is used only by the Server for keeping a list of hotel guests.
### HRInterface.idl
Includes reference to the methods declared in the HRInterface.java file, but in CORBA IDL this time. In this way and with other actions, which were not implemented in this project, communication between client and server can be achieved, even if they are written in a different programming language.
## Client side:
### HRClient.java 
Includes the class that has the main function and starts the Client.
### CancelEventListener.java
Includes reference to the only method of the customer, called by the Server using the Callback technique and in order to inform the customer about booking cancellations for rooms that the customer has expressed interest.

The above is explained in more detail in the documentation.pdf file.
