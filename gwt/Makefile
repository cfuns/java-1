# Target tomcat hostname
TOMCAT_HOST ?= localhost
# Copy bundles first here before telling osgi to deploy the bundle
# For example /remote/mydevel/bundles
LOCAL_BUNDLE_BASE ?= 
# Tell the osgi console to look in this basepath for bundles
# For example /thedevel/bundles
REMOTE_BUNDLE_BASE ?= 
all: setversion
	rm -rf target war
	mvn clean install
clean:
	mvn clean
	rm -rf target war tomcat
run:
	rm -rf target war
	mvn clean gwt:run
runwotest:
	mvn -Dmaven.test.skip=true gwt:run
installwotest: 
	mvn -Dmaven.test.skip=true install
installwotestdraft:
	mvn ${MVNOPT}-Dgwt.draftCompile=true -Dmaven.test.skip=true resources:resources install
test:
	mvn resources:resources test
bundle:
	mvn resources:resources org.apache.felix:maven-bundle-plugin:bundle
deploy:
	../scripts/osgi_deploy.pl 'Benjamin Borbe - GWT' ./target/de.benjaminborbe.gwt.jar ${TOMCAT_HOST} 5555 ${LOCAL_BUNDLE_BASE} ${REMOTE_BUNDLE_BASE}
gwt:
	mvn gwt:run
