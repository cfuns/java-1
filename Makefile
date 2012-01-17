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
console:
	telnet localhost 5555
cleanup:
	find . -name '.DS_Store' -exec rm -rf "{}" \;
	find . -name '.svn' -exec rm -rf "{}" \;
	find . -name 'target' -exec rm -rf "{}" \;
deploy:
	cd bridge && make deploy
dir:
	find . -type d -d 1 -exec sh -c 'cd {} &&  make dir' \;
sonar:
	mvn sonar:sonar
