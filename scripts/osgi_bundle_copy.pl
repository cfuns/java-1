#!/usr/bin/env perl

use strict;

my $from_dir = $ARGV[0];
my $from_dir_api = $from_dir.'_api';
my $from_dir_gui = $from_dir.'_gui';
my $from_dir_test = $from_dir.'_test';

my $to_dir = $ARGV[1];
my $to_dir_api = $ARGV[1].'_api';
my $to_dir_gui = $ARGV[1].'_gui';
my $to_dir_test = $ARGV[1].'_test';
my $to_name = first_letter_uppercase($to_dir);
my $from_name = first_letter_uppercase($from_dir);

unless ($from_dir && $to_dir && -d $from_dir && -d $from_dir_api && -d $from_dir_gui && -d $from_dir_test && ! -d $to_dir && ! -d $to_dir_api && ! -d $to_dir_gui && ! -d $to_dir_test) {
	print STDERR 'usage: '.$0.' [FROMDIR] [TODIR]'."\n";
#	exit(1);
}

my @cmds = ();
push(@cmds, 'rm -rf '.$to_dir);
push(@cmds, 'rm -rf '.$to_dir_api);
push(@cmds, 'rm -rf '.$to_dir_gui);
push(@cmds, 'rm -rf '.$to_dir_test);

push(@cmds, 'rm -rf '.$from_dir.'/target');
push(@cmds, 'rm -rf '.$from_dir_api.'/target');
push(@cmds, 'rm -rf '.$from_dir_gui.'/target');
push(@cmds, 'rm -rf '.$from_dir_test.'/target');

push(@cmds, 'cp -R '.$from_dir.' '.$to_dir);
push(@cmds, 'cp -R '.$from_dir_api.' '.$to_dir_api);
push(@cmds, 'cp -R '.$from_dir_gui.' '.$to_dir_gui);
push(@cmds, 'cp -R '.$from_dir_test.' '.$to_dir_test);

push(@cmds, 'find '.$to_dir. ' '.$to_dir_api.' '.$to_dir_gui.' '.$to_dir_test.' -type f | xargs sed -i \'\' \'s/'.$from_dir.'/'.$to_dir.'/g\'');
push(@cmds, 'find '.$to_dir. ' '.$to_dir_api.' '.$to_dir_gui.' '.$to_dir_test.' -type f | xargs sed -i \'\' \'s/'.$from_name.'/'.$to_name.'/g\'');

push(@cmds, 'echo '.$to_dir.'/target >> .gitignore');
push(@cmds, 'echo '.$to_dir_api.'/target >> .gitignore');
push(@cmds, 'echo '.$to_dir_gui.'/target >> .gitignore');
push(@cmds, 'echo '.$to_dir_test.'/target >> .gitignore');

push(@cmds, '# git add '.$to_dir);
push(@cmds, '# git add '.$to_dir_api);
push(@cmds, '# git add '.$to_dir_gui);
push(@cmds, '# git add '.$to_dir_test);

push(@cmds, '# rename files and dirs');
push(@cmds, '# add module to pom.xml');
push(@cmds, '# add module to bridge/pom.xml');


# -exec mv "{}" echo "{}" sed s/'.$from_name.'/'.$to_name.'/" \;
#push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -name \'*'.$from_name.'*\' -exec bash -c "echo {} `echo {} | sed s/'.$from_name.'/'.$to_name.'/g`" \;');
#push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -name \'*'.$from_dir.'*\' -exec bash -c "echo {} `echo {} | sed s/'.$from_dir.'/'.$to_dir.'/g`" \;');
#push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -name \'*'.$from_name.'*\' -exec echo "{}" `echo "{}" | sed s/'.$from_name.'/'.$to_name.'/g` \;');
#push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -name \'*'.$from_dir.'*\' -exec echo "{}" `echo "{}" | sed s/'.$from_dir.'/'.$to_dir.'/g` \;');


foreach my $cmd (@cmds) {
	print $cmd;
	print "\n";
}

exit(0);

sub first_letter_uppercase() {
	my $input = shift;
	return uc(substr($input,0,1)).substr($input,1);
}
