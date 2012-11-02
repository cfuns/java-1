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
 
drop column family bookmark;
create column family bookmark with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family role;
create column family role with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family user;
create column family user with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family user_role;
create column family user_role with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family permission;
create column family permission with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family permission_role;
create column family permission_role with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family wiki_page;
create column family wiki_page with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family wiki_space;
create column family wiki_space with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family dhl;
create column family dhl with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family blog_post;
create column family blog_post with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family gallery_image;
create column family gallery_image with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family gallery;
create column family gallery with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family configuration;
create column family configuration with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family task;
create column family task with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family task_context;
create column family task_context with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family task_context_relation;
create column family task_context_relation with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;
