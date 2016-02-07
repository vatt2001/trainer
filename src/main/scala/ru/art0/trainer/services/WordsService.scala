package ru.art0.trainer.services

import ru.art0.trainer.models.WordStudy
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

  def getWords(userId: Int): Future[Seq[WordStudy]] = throw new RuntimeException("Not implemented")

  def addWord(userId: Int, data: AddWordData): Future[Boolean] = throw new RuntimeException("Not implemented")

  def deleteWord(wordStudyId: Int): Future[Boolean] = throw new RuntimeException("Not implemented")

  def repeatWord(wordStudyId: Int): Future[Boolean] = throw new RuntimeException("Not implemented")

}


object WordsService {
  case class AddWordData(spelling: String, translation: String)
}