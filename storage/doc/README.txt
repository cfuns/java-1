== Datenbank anlegen ==

/opt/apache-cassandra-1.0.1/bin/cassandra-cli

connect localhost/9160;

create keyspace bb;

use bb;

drop column family data;
create column family tracking with
  comparator = AsciiType and
  key_validation_class = AsciiType and
  default_validation_class = AsciiType;
