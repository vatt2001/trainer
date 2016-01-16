package ru.art0.trainer.services

import ru.art0.trainer.models.WordStudy
import ru.art0.trainer.services.TrainingService.{TrainingResult, TrainingStats}

import scala.concurrent.Future

class TrainingService {

  def getNew(userId: Int): Future[Seq[WordStudy]] = ???
  
  def submit(userId: Int, result: TrainingResult): Future[Boolean] = ???
  
  def getStats(userId: Int): Future[TrainingStats] = ???
}

object TrainingService {

  case class TrainingResult(answers: Seq[TrainingAnswer])

  case class TrainingAnswer(wordStudyId: Int, isCorrect: Boolean)

  case class TrainingStats(wordsTotal: Int, wordsToRepeat: Int, wordsLearned: Int)
}