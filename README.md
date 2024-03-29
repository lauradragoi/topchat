# topchat
Automatically exported from code.google.com/p/topchat

	
#topchat - Collaborative chat software system

This project was created as a Diploma Project by Laura Dragoi & Oana Iancu in the final year at the University "Politehnica" of Bucharest, Faculty of Automatic Control and Computers.

TopChat is a client/server chat software system that incorporates features for supporting collaborative communication between a group of people. One such feature is the ability of a participant in a conversation to graphically connect a message he/she is sending to the group with a previously sent message in that conversation to illustrate a semantic connection between the two.

TopChat uses the Extensible Messaging and Presence Protocol (XMPP) and was implemented in Java. TopChat runs on any Java SE-enabled platform.

#Details on TopChat Server

<table style="width:auto;"><tr><td><a href="http://picasaweb.google.com/lh/photo/3jyu5k2ePK23SkW7LEAuMQ?feat=embedwebsite"><img src="http://lh6.ggpht.com/_SWZTDK5kuus/Sl3eehBOP2I/AAAAAAAACdk/AA0sMUu6dkc/s144/topchatserver_logo.jpg" /></a></td><td width="10%"></td><td><h1>TopChat Server</h1></td></tr></table>

#Introduction

  __*TopChat Server*__ is a software system consisting of a _Java server_ used for connecting clients and a _web application_ used for managing user accounts and exporting conversation logs.

----

= Details =

 * == Features ==

 The Java _server application_ is in charge of:
  * Accepting connections from clients
  * Authenticating connected clients based on username and password 
  * Hosting chat rooms and allowing clients to create/join these rooms
  * Sending messages to their recipients
  * Storing chat messages and events (join, part room) to a database for future analysis
  * Assigning numerical IDs to messages in a group conversation to be used by the client when defining semantic references between messages

 and the _web application_ is used for:
  * Creating new user accounts
  * Exporting conversation logs in order for these to be further analysed


 * == Technologies ==

 This section describes the protocols and technologies used in TopChat Server.
  * TopChat Server uses the __*[http://xmpp.org/ Extensible Messaging and Presence Protocol (XMPP)]*__, implementing features defined in [http://xmpp.org/rfcs/rfc3920.html RFC 3920], [http://xmpp.org/rfcs/rfc3921.html RFC 3921] and the [http://xmpp.org/extensions/xep-0045.html Multi-User Chat extension]. XMPP was chosen due to the fact that it is an open-standard, used in a large number of applications and well documented with support for extensions. I also considered the fact that using this standard protocol allows for interoperability with existing software. 
  * The __*[http://java.sun.com/javase/ Java Standard Edition Platform]*__ was chosen for the development of the server application based on the following considerations:
    * Rapid application development
    * Multithreading support
    * Asynchronous input/output support
    * Security mechanisms
    * Database integration support 
    * Portability
    * Easy delivery of the application
  * The __*Java Concurrency API*__ was necessary to implement multithreading in the application. The TopChat Server is supposed to accept new connection while handling communication with already connected clients and saving information in the database and updating the GUI. All these take place in a multithreaded environment to ensure rapid application response and efficiency. Also if running the server on a multi processor computer or a cluster, the application can better benefit from the available resources rather than if it were single threaded.
  * The __*Java New Input/Output (NIO) API*__ was used for its asynchronous input/output and I/O multiplexing capabilities and also because it can be integrated with Transport Layer Security implementation in SSLEngine since Java SE 1.5.
  * For securing the communication between the server and the clients the following __*[http://java.sun.com/javase/technologies/security/ Java Security]*__ mechanisms were used: 
      * __*[http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html Java Secure Socket Extension (JSSE)]*__ for implementing the Transport Layer Protocol required by XMPP while using non-blocking input/output.
      * __*[http://java.sun.com/javase/6/docs/technotes/guides/security/sasl/sasl-refguide.html The Java SASL API]*__ used for including Simple Authentication and Security Layer as required by the XMPP specification.
  * For parsing and creating XML elements used for communicating between the clients and the server as the XMPP protocol specifies, the __*Java Streaming API for XML (StAX)*__ is used. This is a pull-parsing API, offering the application the entire control of the flow of XML tokens to be parsed and efficiently using CPU and memory. It is also quite easy to use.
  * The server application communicates with databases for obtaining authentication information and for saving conversations. The connection is made possible through the __*[http://java.sun.com/products/jdbc/overview.html Java Database Connectivity API]*__. The JDBC driver that was used is __*MySQL Connector/J*__.
  * An __*[http://httpd.apache.org/ Apache Web Server]*__ and a __*[http://www.mysql.com/ MySQL Database Engine]*__ were used for the web application. The server-side scripting language is __*[http://www.php.net/ PHP]*__. For easier install of these technologies __*[http://www.wampserver.com/ Wamp Server]*__ was chosen.
  * The __*[http://www.junit.org/ JUnit]*__ unit testing framework was integrated to validate the parsing of the specific XML elements used by XMPP.
  * The __*[http://logging.apache.org/log4j/ Log4j]*__ logging framework offered by Apache was used throughout the application to provide a flexible, standard way of recording messages about the activity of the server that can be used for debugging purposes in case of a system failure.


<table style="width:auto;"><tr><td><a href="http://picasaweb.google.com/lh/photo/OD_3YxQRmCS9CZ3LqfjIug?feat=embedwebsite"><img src="http://lh6.ggpht.com/_SWZTDK5kuus/SmBqfxmCh6I/AAAAAAAACeA/ivvOZDOKqsY/s400/topchatserver_tech.jpg" /></a></td></tr><tr align="center"><td style="font-family:arial,sans-serif; font-size:11px; text-align:center">Technologies used by *TopChat Server*</td></tr></table>


 * == Architecture ==

 This section describes the architecture of TopChat? Server.
<table style="width:auto;"><tr><td><a href="http://picasaweb.google.com/lh/photo/ORZjrzx8rs-0veYipQY4Iw?feat=embedwebsite"><img src="http://lh3.ggpht.com/_SWZTDK5kuus/SmBzkRwptZI/AAAAAAAACec/7UumbLR6res/s400/topchatserver_arch.jpeg" /></a></td></tr><tr align="center"><td style="font-family:arial,sans-serif; font-size:11px; text-align:center">Architecture of *TopChat Server* Java application</td></tr></table>

 * The *Java server application* was built around the *Mediator design pattern*: there is a central component, the _Mediator_, that connects all the other modules to each other. Loosely coupling the modules ensures they can be easily modified without affecting all the other modules. Also the Mediator actually works with interfaces describing the application modules, so that the actual implementation of the modules can be changed without having to modify any other component, as long as the same interface is used.
 One exception from the mediator pattern is that the _network_ module and the _protocol_ module are also coupled to each other due to performance constraints, to eliminate the overhead added by communicating through the mediator.
  The modules are:
     * the _Mediator_ module, connecting the other modules
     * the _Network_ module, handling all the network communication 
     * the _Protocol_ module, containing the implementation of the XMPP protocol
     * the _Authentication_ module, responsible for verifying user information
     * the _Data Processing_ module, handling the data from the conversations
     * the _Configuration_ module, useful for modifying properties like database server address
     * the _GUI_ module, minimalist user interface to notify of server status

  * The *web application* consists of two modules:
     * The _Add user account_ module
     * The _Export conversation_ module, which exports a conversation from a specified room to XML




----

= Releases =

 * [http://topchat.googlecode.com/files/1.0.1.zip TopChatServer 1.0.1]
 * [http://topchat.googlecode.com/files/TopChatServer_v1.0.0.zip TopChatServer 1.0.0]
