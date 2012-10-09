all:
	mvn install
installwotest:
	mvn -Dmaven.test.skip=true install
deploy:
	cp target/bridge_devel.war $(TOMCAT_HOME)/webapps/bb.war
dir:
	mkdir -p src/main/resources src/main/java src/test/resources src/test/java
