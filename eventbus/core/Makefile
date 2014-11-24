# Target tomcat hostname
TOMCAT_HOST ?= localhost
# Copy bundles first here before telling osgi to deploy the bundle
# For example /remote/mydevel/bundles
LOCAL_BUNDLE_BASE ?= 
# Tell the osgi console to look in this basepath for bundles
# For example /thedevel/bundles
REMOTE_BUNDLE_BASE ?= 
all:
	mvn clean install
clean:
	mvn clean
installwotest:
	mvn -Dmaven.test.skip=true resources:resources install
bundle:
	mvn org.apache.felix:maven-bundle-plugin:bundle
deploy:
	../../scripts/osgi_deploy.pl 'Benjamin Borbe - Eventbus' ./target/de.benjaminborbe.eventbus.core.jar ${TOMCAT_HOST} 5555 ${LOCAL_BUNDLE_BASE} ${REMOTE_BUNDLE_BASE}
.PHONY: clean installwotest deploy
dir:
	mkdir -p src/main/resources src/main/java src/test/resources src/test/java
update:
	make installwotest deploy
