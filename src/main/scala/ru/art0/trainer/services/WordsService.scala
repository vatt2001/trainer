package ru.art0.trainer.services

import ru.art0.trainer.models.WordStudy
import ru.art0.trainer.services.WordsService.AddWordData

import scala.concurrent.Future

class WordsService {
  def getWords(userId: Int): Future[Seq[WordStudy]] = ???

  def addWord(userId: Int, data: AddWordData): Future[Boolean] = ???

  def deleteWord(wordStudyId: Int): Future[Boolean] = ???

  def repeatWord(wordStudyId: Int): Future[Boolean] = ???

}

object WordsService {
  case class AddWordData(spelling: String, translation: String)
}