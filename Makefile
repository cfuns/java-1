installwotest:
	mvn -Dmaven.test.skip=true install
cleanup:
	find . -name '.DS_Store' -exec rm -rf "{}" \;
	find . -name '.svn' -exec rm -rf "{}" \;
deploy:
	cd bridge && make deploy
