package ru.art0.trainer.services

import java.time.Instant

import ru.art0.trainer.components.{ExecutionContextComponent, DaoComponent}
import ru.art0.trainer.helpers.JsonProtocol.{IdResponse, SimpleResponse}
import ru.art0.trainer.helpers.{WordToStudyHelper}
import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudy
import ru.art0.trainer.models.{StudyMethod, Word, WordStudy}
import ru.art0.trainer.services.WordsService.{SingleWord, WordsList, AddWordData}
import ru.art0.trainer.services.studies.DefaultStudyMethod

import scala.concurrent.Future

trait WordsServiceComponent {

  def wordsService: WordsService

}


trait WordsService {

  def getWords(userId: Int): Future[WordsList]

  def addWord(userId: Int, data: AddWordData): Future[IdResponse]

  def deleteWord(wordStudyId: Int): Future[SimpleResponse]

  def repeatWord(wordStudyId: Int): Future[SingleWord]

}


class WordsServiceImpl extends WordsService {

  this: DaoComponent with ExecutionContextComponent =>

  def getWords(userId: Int): Future[WordsList] = {
    dao.getWordsForUser(userId).map(
      _.map {
        case (w, ws) => WordToStudyHelper.spawnWordStudy(w, ws)
      }
    ).map(WordsList)
  }

  def addWord(userId: Int, data: AddWordData): Future[IdResponse] = {
    for {
      wordId <- dao.upsertWord(Word(0, data.spelling, data.translation))
      wordStudyId <- dao.upsertWordStudy(
        WordStudy(
          id = 0,
          userId = userId,
          wordId = wordId.get,
          isLearned = false,
          isArchived = false,
          studyMethod = StudyMethod.Simple,
          createdAt = Instant.now(),
          nextTrainingAt = Instant.now()
        )
      )
    } yield IdResponse(wordStudyId.get)
  }

  def deleteWord(wordStudyId: Int): Future[SimpleResponse] = {
    dao.archiveWordStudy(wordStudyId).map(_ == 1).map(SimpleResponse(_))
  }

  def repeatWord(wordStudyId: Int): Future[SingleWord] = {
    for {
      wordStudyOpt <- dao.getWordStudy(wordStudyId)
      wordStudy <- wordStudyOpt.fold(throw new RuntimeException(s"Unknown word study with id $wordStudyId")) { ws =>
        val updatedWordStudy = DefaultStudyMethod.relearn(ws)
        dao.upsertWordStudy(updatedWordStudy).map(_ => updatedWordStudy)
      }
      wordOpt <- dao.getWordById(wordStudy.wordId)
      word = wordOpt.getOrElse(throw new RuntimeException(s"Could not find word with id ${wordStudy.wordId}"))
    } yield SingleWord(WordToStudyHelper.spawnWordStudy(word, wordStudy))
  }
}


object WordsService {

  case class SingleWord(word: WordToStudy)

  case class WordsList(words: Seq[WordToStudy])

  case class AddWordData(spelling: String, translation: String)
}