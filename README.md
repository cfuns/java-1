Java
====

Require:

* JDK 1.6 or higher
* Tomcat 6 or 7
* Cassandra

Install
-------


    cp doc/tomcat_libs/* $TOMCAT_HOME/lib/
    
    cassandra-cli
    
    connect localhost/9160;
    

Change Cassandra Settings
-------------------------
    vi $TOMCAT_HOME/conf/context.xml

Insert:

    <Environment name="CassandraMaxConnections" override="true" type="java.lang.String" value="100"/>
    <Environment name="CassandraHost" override="true" type="java.lang.String" value="localhost"/>
    <Environment name="CassandraKeyspace" override="true" type="java.lang.String" value="bb"/>
