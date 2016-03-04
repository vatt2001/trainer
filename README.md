Цель: удобный мобильный тренер для запоминания слов частотным методом.

Главный функционал:
- изучение текущего подмножества слов в соответствии с временем очередной тренировки
- добавление новых слов
- просмотр всего перечня слов с их прогрессом и фильтром по слову, с возможностью изучить слово заново
- ориентированность на плохую связь с сервером (слова для очередной тренировки подгружаются сразу все)
- многопользовательский режим

Вопросы:
- что делать при досрочном повторении слова?

Частотные методы:
- для начала - самый простой: 1 минута, 5 минут, 10 минут, 60 минут, 1 день, 1 неделя


API сервера:
- / - редирект либо на /login, либо на /home
- /login - авторизация (POST: email, password)
- /logout - выйти
- /train - основной скрипт, делающий запросы по API и показывающий слова
  - загрузить список слов для изучения (в это время показывать крутилку)
  - показать все слова
  - показать результаты, в фоновом режиме отправить результаты на сервер (показать крутилку)
  - по окончании отправки результатов на сервер показать кнопки: Train again, Home
- /home - сводная статистика (всего слов, уже изучено, в изучении), кнопки: Train, Add word, My dictionary
- /dict - список всех слов с базовой статистикой и фильтром (GET: q[uery], p[age])
  - базовая статистика: количество повторов, % успешности, сколько еще раз осталось повторять до изучения
- /dict/:word_id - показать одно слово с транскрипцией (надо ли?)
  
- /api/user/login (POST) - login, create user session
  - Request: {email, password}
  - Response: 200, session cookie
- /api/user/logout (POST)
  - Requst: empty
  - Response: 200
  
- /api/words/ (GET) - list words in dictionary
  - Request: empty
  - Response: 200, {words:[{id, spelling, translation, transcription, training_qty_total, training_qty_correct}]}
- /api/words/ (POST) - add word
  - Request: {spelling, translation}
  - Response: 201
- /api/words/:id (DELETE) - delete word
  - Requst: empty
  - Response: 200
- /api/words/:id (PUT) - repeat word
  - Requst: empty
  - Response: 200
  
- /api/training (GET) - получить список слов для очередного изучения
  - Request: empty
  - Response: 200, {words:[{id, spelling, translation, transcription}]}
- /api/training (POST/PUT) - загрузить результат изучения
  - Request: {answers:[{id, isCorrect}]}
  - Response: 200
- /api/training/stats (GET) - get current training statistics
  - Request: empty
  - Response: 200, {wordsTotal, wordsToRepeat, wordsLearned}
  


Модель БД:
- user: id, email, password, is_active
- word: id, spelling, translation, transcription
- method: id, name
- training: id, user_id, method_id, word_id, created_at, next_repeat_at, tries, correct, method_step, is_learned


Must have TODO:
+ learning strategy with increasing intervals and finishing
+ relearning words (reset intervals)
- adding new words
- some kind of Authentication
- serving front static from configurable directory
- move intervals setup to configuration
- packaging for deployment, config for logging (including log rotation)
- VM setup: nginx + java app on Amazon under separate user

Possible TODO:
- refactoring: introduce StudyMethodFactories
- refactoring: move progressSymbol to front
- refactoring: merge Word and WordStudy into one entity, separate Word in DB from Word in Json protocol
- refactoring: introduce data element in API response
- refactoring: front: more DRY error handling
- change response header Server
- add slick transactions


Ideas:
- keyboard bindings when using on big computer

