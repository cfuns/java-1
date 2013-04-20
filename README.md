Java Playground
=============

Require
* JDK 1.7
* Tomcat7
* Cassandra

copy doc/tomcat_libs $TOMCAT_HOME/lib/

vi $TOMCAT_HOME/conf/context.xml

<Environment name="CassandraMaxConnections" override="true" type="java.lang.String" value="100"/>
<Environment name="CassandraHost" override="true" type="java.lang.String" value="localhost"/>
<Environment name="CassandraKeyspace" override="true" type="java.lang.String" value="bb"/>

