package ru.art0.trainer.services

import ru.art0.trainer.components.{ExecutionContextHolderComponent, ExecutionContextHolder, DaoComponent, DatabaseComponent}
import ru.art0.trainer.models.{StudyMethod, Word, WordStudy}
import ru.art0.trainer.services.WordsService.AddWordData

import scala.concurrent.Future

trait WordsServiceComponent {

  def wordsService: WordsService

}


trait WordsService {

  def getWords(userId: Int): Future[Seq[WordStudy]]

  def addWord(userId: Int, data: AddWordData): Future[Boolean]

  def deleteWord(wordStudyId: Int): Future[Boolean]

  def repeatWord(wordStudyId: Int): Future[Boolean]

}


class WordsServiceImpl extends WordsService {

  this: DaoComponent with ExecutionContextHolderComponent =>

  implicit val ec = executionContextHolder.context

  def getWords(userId: Int): Future[Seq[WordStudy]] = dao.getWordStudiesByUserId(userId)

  def addWord(userId: Int, data: AddWordData): Future[Boolean] = {
    for {
      wordId <- dao.upsertWord(Word(0, data.spelling, data.translation))
      wordStudyId <- dao.upsertWordStudy(
        WordStudy(
          id = 0,
          userId = userId,
          wordId = wordId.get,
          studyMethod = StudyMethod.Simple
        )
      )
    } yield (wordStudyId.isDefined)
  }

  def deleteWord(wordStudyId: Int): Future[Boolean] = throw new RuntimeException("Not implemented")

  def repeatWord(wordStudyId: Int): Future[Boolean] = throw new RuntimeException("Not implemented")

}


object WordsService {
  case class AddWordData(spelling: String, translation: String)
}