#!/usr/bin/env perl

use strict;
use warnings;

use Net::Telnet;
use Cwd;
use File::Copy;
use File::Basename;

my $host = shift || 'localhost';
my $port = shift || 5555;
my $prompt = '/->/';

sub cmd {
	my ( $telnet, $cmd ) = @_;
	print 'cmd ' . $cmd . "\n";
	return $telnet->cmd($cmd);
}

sub get_bundle_id {
	my ($procs) = @_;
	my @result = ();
	foreach my $line (@$procs) {
		$line =~ /^\s*\[\s*(\d+)\s*\].*?Benjamin\sBorbe/;
		if ($1) {
			push( @result, $1 );
		}
	}
	return @result;
}

sub update_obr {
	my ($telnet) = @_;
	my @urls = cmd( $telnet, 'obr list-url' );
	foreach my $url (@urls) {
		chomp($url);
		cmd( $telnet, 'obr refresh-url ' . $url );
	}
	return;
}

sub update_bundles {
	my ($telnet) = @_;
	my @ps = cmd( $telnet, 'ps' );
	my @bundleIds = get_bundle_id( \@ps );
	foreach my $bid (@bundleIds) {
		cmd( $telnet, 'update ' . $bid );
	}
	return;
}

print "start.\n";

my $telnet = new Net::Telnet( Timeout => 30, Errmode => 'die', Prompt => $prompt );
$telnet->open( Host => $host, Port => $port );
$telnet->waitfor($prompt);
print "update obr started\n";
update_obr($telnet);
print "update obr finished\n";
print "update bundles started\n";
update_bundles($telnet);
print "update bundles finished\n";
print "done.\n";
exit(0);
