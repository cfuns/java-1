#!/usr/bin/env perl

use strict;
use warnings;

use Net::Telnet;
use Cwd;
use File::Copy;
use File::Basename;

if (@ARGV < 2) {
	print "$0: [bundle name substring] [bundle jar absolute path] [hostname] [port]\n";
	exit 1;
}

my $bname = shift;
my $bfile = Cwd::abs_path(shift);
my $host = shift || 'localhost';
my $port = shift || 5555;
my $target = shift;
my $rtarget = shift;

unless (-f $bfile) {
	print "File $bfile not found!\n";
	exit(1);
}

sub get_bundle_id($$) {
	my ($procs, $bname) = @_;
	my @proc = grep { m/ $bname /i } @$procs;
	if (@proc == 0) {
		print "No bundle $bname found.\n" ;
		return;
	}
	warn "Multiple bundles $bname found." if @proc > 1;
	$proc[0] =~ m/^\[\s*(\d+)\s*\]/;
	return $1;
}

my $prompt = '/->/';
my $telnet = new Net::Telnet ( Timeout=>30, Errmode=>'die', Prompt => $prompt);
$telnet->open(Host => $host, Port => $port);
$telnet->waitfor($prompt);

my @procs = $telnet->cmd('ps');
my $bid = get_bundle_id(\@procs, $bname);
if ($bid) {
	print "Bundle id for $bname is $bid\n";

	my $remote_bundle_path;
	if ($target) {
		print "Using local target dir $target\n";
		copy($bfile, $target);
		my $base = basename($bfile);
		$remote_bundle_path = "$rtarget/$base";
	}
	else {
		$remote_bundle_path = $bfile;
	}

	{
		print "Updating bundle $bid with $bfile\n";
		my @result = $telnet->cmd(String => "update $bid file:$remote_bundle_path", Timeout => 60);
		print "Error: ", $result[0], "\n" if (@result > 0 && $result[0] =~ m/unable|error|fail/i);
	}
	{
		print "Starting bundle $bid\n";
		my @result = $telnet->cmd("start $bid");
		print "Error: ", $result[0], "\n" if (@result > 0 && $result[0] =~ m/unable|error|fail/i);		
	}

	if ($target) {
		my $base = basename($bfile);
		print "Removing copied bundle file $target/$base\n";
		unlink("$target/$base");
	}
} 
else {
#	print "Current bundles:\n";
#	foreach my $proc (@procs) {
#		print "$proc";
#	}
	{
		print "Try installing $bname\n";
		my @result = $telnet->cmd("install file:$bfile");
		print "Error: ", $result[0], "\n"	if (@result > 0 && $result[0] =~ m/unable|error|fail/i);	
	}
	{
		my @procs = $telnet->cmd('ps');
		my $bid = get_bundle_id(\@procs, $bname);
		print "Starting bundle $bid\n";
		my @result = $telnet->cmd("start $bid");
		print "Error: ", $result[0], "\n" if (@result > 0 && $result[0] =~ m/unable|error|fail/i);
	}
}
print "done.\n";
exit(0);
