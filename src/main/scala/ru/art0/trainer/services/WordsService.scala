package ru.art0.trainer.services

import java.time.Instant

import ru.art0.trainer.components.{ExecutionContextComponent, DaoComponent}
import ru.art0.trainer.helpers.{WordToStudyHelper}
import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudy
import ru.art0.trainer.models.{StudyMethod, Word, WordStudy}
import ru.art0.trainer.services.WordsService.{WordsList, AddWordData}

import scala.concurrent.Future

trait WordsServiceComponent {

  def wordsService: WordsService

}


trait WordsService {

  def getWords(userId: Int): Future[WordsList]

  def addWord(userId: Int, data: AddWordData): Future[Int]

  def deleteWord(wordStudyId: Int): Future[Boolean]

  def repeatWord(wordStudyId: Int): Future[Boolean]

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

  def addWord(userId: Int, data: AddWordData): Future[Int] = {
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
    } yield wordStudyId.get
  }

  def deleteWord(wordStudyId: Int): Future[Boolean] = {
    dao.archiveWordStudy(wordStudyId).map(_ == 1)
  }

  def repeatWord(wordStudyId: Int): Future[Boolean] = ???

}


object WordsService {

  case class WordsList(words: Seq[WordToStudy])

  case class AddWordData(spelling: String, translation: String)
}