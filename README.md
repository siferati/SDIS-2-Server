# SDIS-2-Server

To compile:
Windows
javac -cp ".;./lib/json-20090211.jar;" */*.java

To open server execute:
Linux:
java -classpath ".:sqlite-jdbc-3.16.1.jar" GameServer
Windows:
java -classpath ".;sqlite-jdbc-3.16.1.jar;./lib/json-20090211.jar;" GameServer
