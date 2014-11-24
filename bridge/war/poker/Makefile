all:
	mvn install
clean:
	mvn clean
installwotest:
	mvn -Dmaven.test.skip=true install
deploy:
	cp target/bridge.war $(TOMCAT_HOME)/webapps/bb.war
deployforce:
	~/bin/tomcat.sh stop
	rm -rf $(TOMCAT_HOME)/webapps/bb*
	cp target/bridge.war $(TOMCAT_HOME)/webapps/bb.war
	~/bin/tomcat.sh start
dir:
	mkdir -p src/main/resources src/main/java src/test/resources src/test/java
