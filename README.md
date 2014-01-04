Concurrency and communication
=============================

This project is the simulation of a theoretical situation described below.   
Case study: N people are sharing a virtual photo album. To have access to the album, one needs to have access to the KEY. The key is shared as well. The goal is to make the N machines (or threads...) communicate, and to control concurrency between users to obtain key, modify album and give back key.  
There are 3 strategies used for implementing the communication scheme: parallel threads (no communication matters here, just manage concurrency between threads), use of sockets, use of the CORBA middleware. The concurrency is implemented using the Nahimi-Trehel algorithm of mutual exclusion. For more info visit [http://www.infres.enst.fr/~pautet/inf223/tp-dist-lock/index.html] (http://www.infres.enst.fr/~pautet/inf223/tp-dist-lock/index.html)   
NOTES:  
1) In each folder, run "make" then "java Main [-v] &lt;NodeNumber(0-3)&gt;" in separate terminals. For each node, variable "count" shows how many times that node has had access to the key. NB: For the threads version, theres are no communication strategies implemented. Just run "java Main [-v]" in the same terminal.  
2) To use the CORBA version, a CORBA provider is necessary, e.g. [JacORB] (http://www.jacorb.org/). In this case you must download the packages needed and modify the corba.env file accordingly to set your environment PATH variable.  
3) You can run the Main with the -v argument if you want to see the debug info. 
  
