== Datenbank anlegen ==

/opt/apache-cassandra-1.0.1/bin/cassandra-cli

connect localhost/9160;

CREATE KEYSPACE bb WITH
placement_strategy = 'SimpleStrategy'
AND strategy_options = {replication_factor:1};

or

CREATE KEYSPACE bb WITH
placement_strategy = 'SimpleStrategy'
AND strategy_options = {replication_factor:2};

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
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: date, validation_class: UTF8Type, index_type: KEYS}
  ];
  
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

drop column family websearch_page;
create column family websearch_page with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family websearch_configuration;
create column family websearch_configuration with
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
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: key_a, validation_class: UTF8Type, index_type: KEYS},
    {column_name: key_b, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family permission;
create column family permission with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family permission_role;
create column family permission_role with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: key_a, validation_class: UTF8Type, index_type: KEYS},
    {column_name: key_b, validation_class: UTF8Type, index_type: KEYS}
  ];

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

drop column family gallery_group;
create column family gallery_group with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family gallery_collection;
create column family gallery_collection with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: shared, validation_class: UTF8Type, index_type: KEYS}
  ];
  
drop column family gallery_entry;
create column family gallery_entry with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: collectionId, validation_class: UTF8Type, index_type: KEYS},
    {column_name: shared, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family gallery_image;
create column family gallery_image with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family configuration;
create column family configuration with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family confluence_instance;
create column family confluence_instance with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family confluence_page;
create column family confluence_page with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: instanceId, validation_class: UTF8Type, index_type: KEYS},
    {column_name: url, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family task;
create column family task with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: parentId, validation_class: UTF8Type, index_type: KEYS},
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS},
    {column_name: context, validation_class: UTF8Type, index_type: KEYS},
    {column_name: completed, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family task_context;
create column family task_context with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS},
    {column_name: name, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family task_context_user_relation;
create column family task_context_user_relation with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: key_a, validation_class: UTF8Type, index_type: KEYS},
    {column_name: key_b, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family checklist_list;
create column family checklist_list with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family checklist_entry;
create column family checklist_entry with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS},
    {column_name: listId, validation_class: UTF8Type, index_type: KEYS},
  ];

drop column family message_queue;
create column family message_queue with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  gc_grace = 3600 and
  column_metadata = [
    {column_name: type, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family search_query_history;
create column family search_query_history with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: user, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family projectile_report;
create column family projectile_report with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: username, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family projectile_team;
create column family projectile_team with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family projectile_team_user;
create column family projectile_team_user with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: key_a, validation_class: UTF8Type, index_type: KEYS},
    {column_name: key_b, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family shortener_url;
create column family shortener_url with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type; 

drop column family distributed_search_page;
create column family distributed_search_page with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: 'index', validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family distributed_index_word;
create column family distributed_index_word with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family distributed_index_entry;
create column family distributed_index_entry with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family analytics_report_log;
create column family analytics_report_log with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family analytics_report;
create column family analytics_report with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: name, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family analytics_report_value;
create column family analytics_report_value with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family dashboard;
create column family dashboard with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family lunch_user_settings;
create column family lunch_user_settings with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: notificationActivated, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family monitoring_node;
create column family monitoring_node with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family poker_game;
create column family poker_game with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family poker_player;
create column family poker_player with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family note;
create column family note with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: owner, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family notification_type;
create column family notification_type with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family notification_media;
create column family notification_media with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family virt_network;
create column family virt_network with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family task_attachment;
create column family task_attachment with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type and
  column_metadata = [
    {column_name: task, validation_class: UTF8Type, index_type: KEYS}
  ];

drop column family filestorage_entry;
create column family filestorage_entry with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;

drop column family selenium_xml;
create column family selenium_xml with
  comparator = UTF8Type and
  key_validation_class = UTF8Type and
  default_validation_class = UTF8Type;
