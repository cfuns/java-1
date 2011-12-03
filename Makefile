all:
	mvn install
installwotest:
	mvn -Dmaven.test.skip=true install
test:
	mvn test
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
