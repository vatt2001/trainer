pragma foreign_keys = on;

create table user (
  id int primary key autoincrement,
  email varchar(255) not null,
  password_hash varchar(255) not null,
  is_active boolean not null default true,

  constraint uq_user__email unique (email)
);


create table word (
  id int primary key autoincrement,
  spelling varchar(255) not null,
  translation varchar(255) not null,
  transcription varchar(255)
);

create index idx_word__spelling on word(spelling);


create table word_study(
  id int primary key autoincrement,
  user_id int not null,
  word_id int not null,
  study_method_id int not null check(study_method_id = 1),
  training_qty_total int not null check(training_qty_total >= 0),
  training_qty_correct int not null check(training_qty_correct >= 0),

  foreign key (user_id) references user(id),
  foreign key (word_id) references word(id)
);
