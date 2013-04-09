MVN_OPTS=-Djava.awt.headless=true
fast:
	mvn $(MVN_OPTS) -Pdefault,base,lib -T 2C -Dmaven.test.skip=true install
installlib:
	mvn $(MVN_OPTS) -Pdefault,lib install
all:
	mvn $(MVN_OPTS) -Pdefault,base,lib,meta,bridge,test,slow install
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
	cd tools && make all
	cd tools_osgi && make all 
buildwebsite:
	cd website && make all
buildlunch:
	cd lunch_api && make all
	cd lunch_core && make all
	cd lunch_gui && make all
	cd lunch_test && make all
buildportfolio:
	cd portfolio_api && make all
	cd portfolio && make all
	cd portfolio_gui && make all
	cd portfolio_test && make all
buildconfiguration:
	cd configuration && make all
buildstorage:
	cd storage_api && make all
	cd storage_tools && make all
	cd storage_core && make all
	cd storage_gui && make all
	cd storage_test && make all
buildsample:
	cd sample && make all
buildloggly:
	cd loggly_api && make all
	cd loggly_core && make all
	cd loggly_gui && make all
	cd loggly_test && make all
buildworktime:
	cd worktime_api && make all
	cd worktime_core && make all
	cd worktime_gui && make all
	cd worktime_test && make all
buildgallery:
	cd gallery_api && make all
	cd gallery_core && make all
	cd gallery_gui && make all
	cd gallery_test && make all
buildweather:
	cd weather_api && make all
	cd weather_core && make all
	cd weather_gui && make all
	cd weather_test && make all
buildgwt:
	cd gwt_api && make all
	cd gwt_core && make all
	cd gwt_gui && make all
	cd gwt_test && make all
buildblog:
	cd blog_api && make all
	cd blog && make all
	cd blog_gui && make all
	cd blog_test && make all
buildwiki:
	cd wiki_api && make all
	cd wiki_core && make all
	cd wiki_gui && make all
	cd wiki_test && make all
buildslash:
	cd slash_api && make all
	cd slash_core && make all
	cd slash_gui && make all
	cd slash_test && make all
buildbookmark:
	cd bookmark_api && make all
	cd bookmark_core && make all
	cd bookmark_gui && make all
	cd bookmark_test && make all
buildauth:
	cd authentication && make all
	cd authorization && make all
buildmonitoring:
	cd monitoring_api && make all
	cd monitoring_tools && make all
	cd monitoring_core && make all
	cd monitoring_gui && make all
	cd monitoring_test && make all
buildvnc:
	cd vnc_api && make all
	cd vnc_core && make all
	cd vnc_gui && make all
	cd vnc_test && make all
buildxmpp:
	cd xmpp_api && make all
	cd xmpp_core && make all
	cd xmpp_gui && make all
	cd xmpp_test && make all
buildwow:
	cd wow_api && make all
	cd wow_core && make all
	cd wow_gui && make all
	cd wow_test && make all
buildwebsearch:
	cd websearch_api && make all
	cd websearch_core && make all
	cd websearch_gui && make all
	cd websearch_test && make all
buildindex:
	cd index_api && make all
	cd index_core && make all
	cd index_gui && make all
	cd index_test && make all
buildluceneindex:
	cd lucene_index_api && make all
	cd lucene_index && make all
	cd lucene_index_gui && make all
	cd lucene_index_test && make all
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
	cd search_core && make all
	cd search_gui && make all
	cd search_test && make all
buildscala:
	cd scala_api && make all
	cd scala_core && make all
	cd scala_gui && make all
	cd scala_test && make all
buildtask:
	cd task && make all
buildcron:
	cd cron_api && make all
	cd cron_core && make all
	cd cron_gui && make all
	cd cron_test && make all
buildutil:
	cd util_api && make all
	cd util_core && make all
	cd util_gui && make all
	cd util_test && make all
buildeventbus:
	cd eventbus_api && make all
	cd eventbus_core && make all
	cd eventbus_gui && make all
	cd eventbus_test && make all
buildmeta:
	cd meta && make all
buildbridge:
	cd bridge && make all
buildconfluence:
	cd confluence_api && make all
	cd confluence_core && make all
	cd confluence_gui && make all
	cd confluence_test && make all
buildgooglesearch:
	cd googlesearch_api && make all
	cd googlesearch_core && make all
	cd googlesearch_gui && make all
	cd googlesearch_test && make all
buildnavigation:
	cd navigation && make all
buildchecklist:
	cd checklist_api && make all
	cd checklist_core && make all
	cd checklist_gui && make all
	cd checklist_test && make all
buildmail:
	cd mail_api && make all
	cd mail_core && make all
	cd mail_gui && make all
	cd mail_test && make all
buildprojectile:
	cd projectile_api && make all
	cd projectile_core && make all
	cd projectile_gui && make all
	cd projectile_test && make all
buildshortener:
	cd shortener_api && make all
	cd shortener_core && make all
	cd shortener_gui && make all
	cd shortener_test && make all
buildmessage:
	cd message_api && make all
	cd message_core && make all
	cd message_gui && make all
	cd message_test && make all
builddistributed:
	cd distributed && make all
buildanalytics:
	cd analytics_api && make all
	cd analytics_core && make all
	cd analytics_gui && make all
	cd analytics_test && make all
buildperformance:
	cd performance_api && make all
	cd performance_core && make all
	cd performance_gui && make all
	cd performance_test && make all
builddashboard:
	cd dashboard_api && make all
	cd dashboard_core && make all
	cd dashboard_gui && make all
	cd dashboard_test && make all
buildkiosk:
	cd kiosk_api && make all
	cd kiosk_core && make all
	cd kiosk_gui && make all
	cd kiosk_test && make all
buildsystemstatus:
	cd systemstatus_api && make all
	cd systemstatus_core && make all
	cd systemstatus_gui && make all
	cd systemstatus_test && make all
buildcache:
	cd cache_api && make all
	cd cache_core && make all
	cd cache_gui && make all
	cd cache_test && make all
buildpoker:
	cd poker_api && make all
	cd poker_core && make all
	cd poker_gui && make all
	cd poker_test && make all
	cd poker_client && make all
	cd poker_table && make all
buildcms:
	cd cms_api && make all
	cd cms_core && make all
	cd cms_gui && make all
	cd cms_test && make all
builddhl:
	cd dhl && make all
buildnote:
	cd note_api && make all
	cd note_core && make all
	cd note_gui && make all
	cd note_test && make all
buildxmlrpc:
	cd xmlrpc_api && make all
	cd xmlrpc_core && make all
	cd xmlrpc_gui && make all
	cd xmlrpc_test && make all
buildnotification:
	cd notification_api && make all
	cd notification_core && make all
	cd notification_gui && make all
	cd notification_test && make all
buildhtml:
	cd html_api && make all
	cd html_test && make all
buildvirt:
	cd virt && make all
buildfilestorage:
	cd filestorage && make all
buildproxy:
	cd proxy && make all
