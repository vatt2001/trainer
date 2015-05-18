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
- /login - авторизация (POST: email, password)
- /logout - выйти
- /train - основной скрипт, делающий запросы по API и показывающий слова
- /stats - сводная статистика (всего слов, уже изучено, в изучении)
- /words - список всех слов с базовой статистикой и фильтром (GET: q, POST: action=add/repeat/delete, id=<word_id>)
  - базовая статистика: количество повторов, % успешности, сколько еще раз осталось повторять до изучения
- /words/add (POST): id, spelling, translation
- /words/delete (POST): id
- /words/repeat (POST): id
- /api/training_start - получить список слов для очередного изучения (GET)
  - words: [{id, spelling, translation, transcription}]
- /api/training_finish - загрузить результат изучения (POST)
  - result: [{word_id, is_correct}]


Модель БД:
- user: id, email, password, is_active
- word: id, spelling, translation, transcription
- method: id, name
- training: id, user_id, method_id, word_id, created_at, next_repeat_at, tries, correct, method_step, is_learned
