# Mobile Applications COMP2601
### Assignment 3 - Mobile Messaging App and Server
This project contains two parts:
 - Create a mobile app which will allow users to send messages to each other after logging into a server
 - Create the server which will allow app users to log in

The purpose of the exercise is to demonstrate client-server network interaction using sockets, 
streams and messages. The goal is to build a java-based chat room server with android clients.
The server accepts concurrent messages of various types (login, logout, announcement, and message), and
dispatches them by way of the Reactor Pattern.
Network connections are managed by the server using threads.

### Table of Contents

1. [Libraries and Installation](#installation)
2. [Project Description](#motivation)
3. [Files](#files)
4. [Findings](#results)

## Libraries and Installation <a name="installation"></a>

Libraries used:
There are no special libraries used.

Installation:
The server can be run from the main() function in the ServerMain class.  I used Eclipse to create this project.

The app is written for Android using Android Studio.  The app will run on a virtual device or can be side-loaded onto a mobile device with appropriate developer permissions set.  It is more fun to use a couple of actual Android phones to talk to each other!

Unless both phones are connected to the same LAN as the server, the router that the server is running on must be set to forward the server's port.  The app users will connect to the router's IP address.  If all devices are on the same LAN, then the LAN address of the server should be used.  The image shows and example use of the `ifconfig` command for this purpose.

To run:
*Server*
This system is provided with IP and port configuration enabled.  The intention is to make it easy to connect real devices to the server over the LAN.

Launch the server first - a GUI will open showing the local IP address and give the option to configure the port.  The GUI allows you to start the server once ready.  Messages, including control messages, are logged to the console by means of System.out.println().  This logging is an intended server feature.

Note: The server thread launches a small GUI window which can be used to broadcast messages from "admin" to all users. 

*App*
Launch two or more clients using either real or virtual devices.  It is more fun to use real devices but virtual devices have been tested.  Use the IP address and port from the server GUI as needed.  Log in using a screen name.

The GUI provides a spinner with other users and four buttons along the bottom of the screen:
 - Message selected user (Blue)
 - Refresh chat display (Green)
 - Make announcement to all users (Yellow)
 - Log out (Red)



## Project Description<a name="motivation"></a>

The Android client login screen:

![alt text](https://github.com/ismith1024/Mobile-Apps-Assignment-3-Server/blob/master/20190418213452_Screenshot_2019-04-18-21-28-22.png "Login")

The Android client chat screen:

![alt text](https://github.com/ismith1024/Mobile-Apps-Assignment-3-Server/blob/master/20190418213459_Screenshot_2019-04-18-21-28-07.png "Chat")

The java server:
![alt text](https://github.com/ismith1024/Mobile-Apps-Assignment-3-Server/blob/master/Screenshot%20from%202019-04-18%2021-29-35.png?raw=trued "Server")

When a client connects to the server the client sends a "login" message to the server identifying the user.  When new users log in a message is sent to all currently connected users informing them of the new member's arrival.  After the server connection is accepted, a LOGIN message type is sent to the server.  The server adds the client name and connection to the hashmap.  Both client and server listen for unsolicited messages.  When a user joins or leaves, the server broadcasts an UPDATE message to all users with this information.  When a client disconnects, a LOGOUT message is sent to the server, which drops their conncection and informs all users.

Other connceted Users are indicated by the client app.  Updates are pushed to clients by the server using a "UPDATE" message type with the current users list in the message body.  This is handled by the app to update the users list.

A user of the app is able to select one or all other users to send messages or updates to.  The mobile client keeps a log of messages sent and received.  Announcements from the server are indicated in the log by a megaphone emoji ('📢').

Message objects are serialized to JSON when transmitted, but are Java objects when used internally by clients or server.  Messages have a header and body component.  Objects are stringified and parsed using the message's toJSON() method and the static `Message.parseFromJSON()` respectively

The reactor pattern is implemented, and consists of:
  - A MessageReactor interface with reactTo(Message, Connection) method
  - A Dispatcher class
  - Three MessageReactors registered in Dispatcher constructor for the three valid message types that clients can send
  - Call to `dispatcher.dispatch()` method when message received or generated by server

Connections are stored in a `HashMap<String, Connection>`.  The `Connection` class inherits form the Java `Thread` class.

## Files <a name="files"></a>

### Server:
The server is in the GitHub repository `Mobile-Apps-Assignment-3-Server` at https://github.com/ismith1024/Mobile-Apps-Assignment-3-Server .

The files `Connection.java`, `ServerMain.java` and `Server.java` are in the package `src/com/comp2601/server`.  The Message class uses the files `Body.java`, `Header.java`, and `Message.Java`, and are in the package `src/com/comp2601/message`.

The server can be run from the main() function in the ServerMain.Java file using an IDE.  There is no executable jar file.

### App:
The app is in the GitHub repository `Mobile-Apps-Assignment-3-Client` at https://github.com/ismith1024/Mobile-Apps-Assignment-3-Client.

The App consists of an Android Studio project, most of which is project boilerplate.  Interesting classes are in the package `java/comp2601/ian/comp2601_a3`


## Findings<a name="results"></a>

The app and server work as intended.








