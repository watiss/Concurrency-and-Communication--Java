Concurrency and communication (INF223)
=====================================

Case study: N people are sharing a virtual photo album. To have access to the album, one needs to have access to the KEY. The key is shared as well. The goal is to make the N machines (or threads...) communicaten, and to control concurrency between users to obtain key, modify album and give back key. There are 3 strategies used for implementing the communication scheme: parallel threads, use of sockets, use of the CORBA middleware. The concurrency is implemented using the Nahimi-Trehel algorithm of mutual exclusion.  
NOTES:  
1) For use of the CORBA version, a CORBA provider is needed, e.g. Jacorb, in which case you must have the packages needed and modify the corba.env file accordingly to set your environment correctly.  
2) You can run the Main with the -v argument if you want to see debug info. 
  
