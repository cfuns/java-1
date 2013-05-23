MVN_OPTS=-Djava.awt.headless=true
all:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow install
sonar:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow sonar:sonar
fast:
	mvn $(MVN_OPTS) -Pdefault,base,lib -T 2C -Dmaven.test.skip=true install
installlib:
	mvn $(MVN_OPTS) -Pdefault,lib install
clean:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow clean
	find . -name target -type d -exec rm -rf "{}" \;
test:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow test
installwotest:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow -Dmaven.test.skip=true install
update:
	mvn -Pdefault,base,lib,meta,bridge,test,slow versions:display-dependency-updates | grep ' -> ' | perl ~/bin/unique.pl
updateplugin:
	mvn -Pdefault,base,lib,meta,bridge,test,slow versions:display-plugin-updates | grep ' -> ' | perl ~/bin/unique.pl
package:
	make packagedevel
deploy:
	make deploydevel
packagedevel:
	cd bridge/war/devel && make installwotest
deploydevel:
	cd bridge/war/devel && make deployforce
packageobr:
	cd bridge/war/obr && make installwotest
deployobr:
	cd bridge/war/obr && make deployforce
packagereport:
	cd bridge/war/report && make installwotest
deployreport:
	cd bridge/war/report && make deployforce
packageoffice:
	cd bridge/war/office && make installwotest
deployoffice:
	cd bridge/war/office && make deployforce
packageonline:
	cd bridge/war/online && make installwotest
deployonline:
	cd bridge/war/online && make deployforce
dllir:
	find . -type d -d 1 -exec sh -c 'cd {} && make dir' \;
help:
	echo "help"
findwrongnamedtests:
	find . -name "*Test.java" | grep -v UnitTest | grep -v IntegrationTest
telnet:
	telnet localhost 5555
uploadoffice:
	scp bridge/war/office/target/bridge.war bborbe@bborbe.devel.lf.seibert-media.net:~/
uploadonline:
	scp bridge/war/online/target/bridge.war bborbe@10.4.0.21:~/
	scp bridge/war/online/target/bridge.war bborbe@10.4.0.22:~/
uploadnexus:
	mvn $(MVN_OPTS) -DremoteOBR deploy
uploadnexusforce:
	mvn $(MVN_OPTS) -DremoteOBR -DignoreLock deploy
osgitest:
	find . -type d -d 1 -name "*_test" -exec sh -c 'cd {} && make' \;
guitest:
	find . -type d -d 1 -name "*_gui" -exec sh -c 'cd {} && make' \;
apitest:
	find . -type d -d 1 -name "*_api" -exec sh -c 'cd {} && make' \;
console:
	telnet localhost 5555
cleanup:
	find . -name '.DS_Store' -exec rm -rf "{}" \;
	find . -name '.svn' -exec rm -rf "{}" \;
	find . -name 'target' -exec rm -rf "{}" \;
buildtools:
	cd lib/tools && make all
	cd lib/tools_osgi && make all 
buildwebsite:
	cd website && make all
buildlunch:
	cd lunch && make all
buildportfolio:
	cd portfolio && make all
buildconfiguration:
	cd configuration && make all
buildstorage:
	cd storage && make all
buildsample:
	cd sample && make all
buildloggly:
	cd loggly && make all
buildworktime:
	cd worktime && make all
buildgallery:
	cd gallery && make all
buildweather:
	cd weather && make all
buildgwt:
	cd gwt && make all
buildblog:
	cd blog && make all
buildwiki:
	cd wiki && make all
buildslash:
	cd slash && make all
buildbookmark:
	cd bookmark && make all
buildauth:
	cd authentication && make all
	cd authorization && make all
buildmonitoring:
	cd monitoring && make all
buildvnc:
	cd vnc && make all
buildxmpp:
	cd xmpp && make all
buildwow:
	cd wow && make all
buildwebsearch:
	cd websearch && make all
buildindex:
	cd index && make all
buildluceneindex:
	cd lucene_index && make all
buildcrawler:
	cd crawler && make all
buildmicroblog:
	cd microblog && make all
buildsearch:
	cd search && make all
buildscala:
	cd scala && make all
buildtask:
	cd task && make all
buildcron:
	cd cron && make all
buildutil:
	cd util && make all
buildeventbus:
	cd eventbus && make all
buildmeta:
	cd meta && make all
buildbridge:
	cd bridge && make all
buildconfluence:
	cd confluence && make all
buildgooglesearch:
	cd googlesearch && make all
buildnavigation:
	cd navigation && make all
buildchecklist:
	cd checklist && make all
buildmail:
	cd mail && make all
buildprojectile:
	cd projectile && make all
buildshortener:
	cd shortener && make all
buildmessage:
	cd message && make all
builddistributed:
	cd distributed && make all
buildanalytics:
	cd analytics && make all
buildperformance:
	cd performance && make all
builddashboard:
	cd dashboard && make all
buildkiosk:
	cd kiosk && make all
buildsystemstatus:
	cd systemstatus && make all
buildcache:
	cd cache && make all
buildpoker:
	cd poker && make all
buildcms:
	cd cms && make all
builddhl:
	cd dhl && make all
buildnote:
	cd note && make all
buildxmlrpc:
	cd xmlrpc && make all
buildnotification:
	cd notification && make all
buildhtml:
	cd html && make all
buildvirt:
	cd virt && make all
buildfilestorage:
	cd filestorage && make all
buildproxy:
	cd proxy && make all
builddns:
	cd dns && make all
