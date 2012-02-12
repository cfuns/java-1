== Datenbank anlegen ==

/opt/apache-cassandra-1.0.1/bin/cassandra-cli

connect localhost/9160;

create keyspace bb;

use bb;

drop column family data;
create column family data with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family worktime;
create column family worktime with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;
  
drop column family microblog;
create column family microblog with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family test;
create column family test with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family page;
create column family page with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family session;
create column family session with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;
  