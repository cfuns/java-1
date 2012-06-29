all:
	mvn install
package:
	cd bridge && make installwotest
installwotest:
	mvn -Dmaven.test.skip=true install
update:
	make all deploy
test:
	mvn test
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
buildbookmark:
	cd bookmark_api && make all
	cd bookmark && make all
	cd bookmark_gui && make all
	cd bookmark_test && make all
deploy:
	cd bridge && make deploy
dir:
	find . -type d -d 1 -exec sh -c 'cd {} &&  make dir' \;
sonar:
	mvn sonar:sonar
help:
	echo "help"
findwrongnamedtests:
	find . -name "*Test.java" | grep -v UnitTest | grep -v IntegrationTest
