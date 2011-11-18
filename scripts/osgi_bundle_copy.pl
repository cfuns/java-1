#!/usr/bin/env perl

use strict;

my $from_dir = $ARGV[0];
my $from_dir_test = $from_dir.'_test';
my $to_dir = $ARGV[1];
my $to_dir_test = $ARGV[1].'_test';
my $to_name = first_letter_uppercase($to_dir);
my $from_name = first_letter_uppercase($from_dir);

unless ($from_dir && $to_dir && -d $from_dir && -d $from_dir_test) {
	print STDERR 'usage: '.$0.' [FROMDIR] [TODIR]';
	exit(1);
}

my @cmds = ();
push(@cmds,'cp -R '.$from_dir.' '.$to_dir);
push(@cmds, 'cp '.$from_dir_test.' '.$to_dir_test);
push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -type f -print0 | xargs -0 sed -i \'s/'.$from_dir.'/'.$to_dir.'/g\'');
push(@cmds, 'find '.$to_dir. ' '.$to_dir_test.' -type f -print0 | xargs -0 sed -i \'s/'.$from_name.'/'.$to_name.'/g\'');

foreach my $cmd (@cmds) {
	print $cmd;
	print "\n";
}

exit(0);

sub first_letter_uppercase() {
	my $input = shift;
	return uc(substr($input,0,1)).substr($input,1);
}