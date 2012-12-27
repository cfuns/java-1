MVN_OPTS=-Djava.awt.headless=true
fast:
	mvn $(MVN_OPTS) -Pbase -T 2C -Dmaven.test.skip=true install
all:
	mvn $(MVN_OPTS) -Pbase,meta,bridge,test,slow install
clean:
	mvn $(MVN_OPTS) -Pbase,meta,bridge,test,slow clean
	find . -name target -type d -exec rm -rf "{}" \;
test:
	mvn $(MVN_OPTS) -Pbase,meta,bridge,test,slow test
installwotest:
	mvn $(MVN_OPTS) -Pbase,meta,bridge,test,slow -Dmaven.test.skip=true install
sonar:
	mvn $(MVN_OPTS) sonar:sonar
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
packageoffice:
	cd bridge/war/office && make installwotest
deployoffice:
	cd bridge/war/office && make deployforce
packageonline:
	cd bridge/war/online && make installwotest
deployonline:
	cd bridge/war/online && make deployforce
update:
	mvn -Pbase,meta,bridge,test,slow versions:display-dependency-updates | grep ' -> ' | perl ~/bin/unique.pl
updateplugin:
	mvn -Pbase,meta,bridge,test,slow versions:display-plugin-updates | grep ' -> ' | perl ~/bin/unique.pl
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
	cd tools && make all
	cd tools_osgi && make all 
buildwebsite:
	cd website && make all
buildlunch:
	cd lunch_api && make all
	cd lunch && make all
	cd lunch_gui && make all
	cd lunch_test && make all
buildportfolio:
	cd portfolio_api && make all
	cd portfolio && make all
	cd portfolio_gui && make all
	cd portfolio_test && make all
buildconfiguration:
	cd configuration_api && make all
	cd configuration && make all
	cd configuration_tools && make all
	cd configuration_gui && make all
	cd configuration_test && make all
buildstorage:
	cd storage_api && make all
	cd storage && make all
	cd storage_tools && make all
	cd storage_gui && make all
	cd storage_test && make all
buildloggly:
	cd loggly_api && make all
	cd loggly && make all
	cd loggly_gui && make all
	cd loggly_test && make all
buildworktime:
	cd worktime_api && make all
	cd worktime && make all
	cd worktime_gui && make all
	cd worktime_test && make all
buildgallery:
	cd gallery_api && make all
	cd gallery && make all
	cd gallery_gui && make all
	cd gallery_test && make all
buildweather:
	cd weather_api && make all
	cd weather && make all
	cd weather_gui && make all
	cd weather_test && make all
buildgwt:
	cd gwt_api && make all
	cd gwt && make all
	cd gwt_gui && make all
	cd gwt_test && make all
buildblog:
	cd blog_api && make all
	cd blog && make all
	cd blog_gui && make all
	cd blog_test && make all
buildwiki:
	cd wiki_api && make all
	cd wiki && make all
	cd wiki_gui && make all
	cd wiki_test && make all
buildslash:
	cd slash_api && make all
	cd slash && make all
	cd slash_gui && make all
	cd slash_test && make all
buildbookmark:
	cd bookmark_api && make all
	cd bookmark && make all
	cd bookmark_gui && make all
	cd bookmark_test && make all
buildauth:
	cd authentication_api && make all
	cd authentication && make all
	cd authentication_gui && make all
	cd authentication_test && make all
	cd authorization_api && make all
	cd authorization && make all
	cd authorization_gui && make all
	cd authorization_test && make all
buildmonitoring:
	cd monitoring_api && make all
	cd monitoring && make all
	cd monitoring_gui && make all
	cd monitoring_test && make all
buildvnc:
	cd vnc_api && make all
	cd vnc && make all
	cd vnc_gui && make all
	cd vnc_test && make all
buildxmpp:
	cd xmpp_api && make all
	cd xmpp && make all
	cd xmpp_gui && make all
	cd xmpp_test && make all
buildwow:
	cd wow_api && make all
	cd wow && make all
	cd wow_gui && make all
	cd wow_test && make all
buildwebsearch:
	cd websearch_api && make all
	cd websearch && make all
	cd websearch_gui && make all
	cd websearch_test && make all
buildindex:
	cd index_api && make all
	cd index && make all
	cd index_gui && make all
	cd index_test && make all
buildcrawler:
	cd crawler_api && make all
	cd crawler && make all
	cd crawler_gui && make all
	cd crawler_test && make all
buildmicroblog:
	cd microblog_api && make all
	cd microblog && make all
	cd microblog_gui && make all
	cd microblog_test && make all
buildsearch:
	cd search_api && make all
	cd search && make all
	cd search_gui && make all
	cd search_test && make all
buildscala:
	cd scala_api && make all
	cd scala && make all
	cd scala_gui && make all
	cd scala_test && make all
buildtask:
	cd task_api && make all
	cd task_tools && make all 
	cd task && make all
	cd task_gui && make all
	cd task_test && make all
buildcron:
	cd cron_api && make all
	cd cron && make all
	cd cron_gui && make all
	cd cron_test && make all
buildutil:
	cd util_api && make all
	cd util && make all
	cd util_gui && make all
	cd util_test && make all
buildeventbus:
	cd eventbus_api && make all
	cd eventbus && make all
	cd eventbus_gui && make all
	cd eventbus_test && make all
buildmeta:
	cd meta && make all
buildbridge:
	cd bridge && make all
buildconfluence:
	cd confluence_api && make all
	cd confluence && make all
	cd confluence_gui && make all
	cd confluence_test && make all
buildgooglesearch:
	cd googlesearch_api && make all
	cd googlesearch && make all
	cd googlesearch_gui && make all
	cd googlesearch_test && make all
buildnavigation:
	cd navigation_api && make all
	cd navigation && make all
	cd navigation_gui && make all
	cd navigation_test && make all
buildchecklist:
	cd checklist_api && make all
	cd checklist && make all
	cd checklist_gui && make all
	cd checklist_test && make all
buildmail:
	cd mail_api && make all
	cd mail && make all
	cd mail_gui && make all
	cd mail_test && make all
buildprojectile:
	cd projectile_api && make all
	cd projectile && make all
	cd projectile_gui && make all
	cd projectile_test && make all
buildshortener:
	cd shortener_api && make all
	cd shortener && make all
	cd shortener_gui && make all
	cd shortener_test && make all
