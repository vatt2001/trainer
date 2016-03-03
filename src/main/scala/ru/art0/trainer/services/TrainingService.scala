package ru.art0.trainer.services

import ru.art0.trainer.components.{ExecutionContextComponent, ConfigComponent, DaoComponent}
import ru.art0.trainer.helpers.JsonProtocol.SimpleResponse
import ru.art0.trainer.helpers.WordToStudyHelper
import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudy
import ru.art0.trainer.models.{Word, WordStudy}
import ru.art0.trainer.services.TrainingService.{TrainingNew, TrainingResult, TrainingStats}
import ru.art0.trainer.services.studies.DefaultStudyMethod

import scala.concurrent.Future

trait TrainingServiceComponent {

  def trainingService: TrainingService

}


trait TrainingService {

  def getNew(userId: Int): Future[TrainingNew]
  
  def submit(userId: Int, result: TrainingResult): Future[SimpleResponse]
  
  def getStats(userId: Int): Future[TrainingStats]
}


class TrainingServiceImpl extends TrainingService {

  this: DaoComponent with ConfigComponent with ExecutionContextComponent =>

  def getNew(userId: Int): Future[TrainingNew] = {
    dao.getWordsForTraining(userId, config.trainerTrainingLimit).map(
      _.map( row =>
        WordToStudyHelper.spawnWordStudy(row._1, row._2)
      )
    ).map(TrainingNew)
  }

  def submit(userId: Int, result: TrainingResult): Future[SimpleResponse] = {
    val wordStudyIds = result.answers.map(_.id)
    for {
      wordStudies <- dao.getWordStudies(wordStudyIds)
      result <- processTrainingResult(userId, result, wordStudies)
    } yield SimpleResponse(result)
  }

  def getStats(userId: Int): Future[TrainingStats] = {
    for {
      stats <- aggregateStats(dao.getWordStudyLearnedQty(userId))
      wordsToRepeatNow <- dao.getWordsForTrainingCount(userId)
    } yield stats.copy(wordsToRepeatNow = wordsToRepeatNow)
  }

  private def aggregateStats(rawStats: Future[Seq[(Boolean, Int)]]): Future[TrainingStats] = {
    rawStats.map { s =>
      s.foldLeft(TrainingStats(0, 0, 0, 0)) { (r, item) =>
        item._1 match {
          case true => r.copy(wordsTotal = r.wordsTotal + item._2, wordsLearned = r.wordsLearned + item._2)
          case false => r.copy(wordsTotal = r.wordsTotal + item._2, wordsToRepeat = r.wordsToRepeat + item._2)
        }
      }
    }
  }

  private def processTrainingResult(userId: Int, result: TrainingResult, wordStudies: Seq[WordStudy]): Future[Boolean] = {
    val wordStudiesById = wordStudies.map(s => s.id -> s).toMap
    val updatedWordStudies =
      result.answers.map { answer =>
        val wordStudy =
          wordStudiesById.getOrElse(
            answer.id,
            throw new RuntimeException(s"Unknown WordStudy id: ${answer.id}")
          )

        if (wordStudy.userId != userId) {
          throw new RuntimeException(s"Wrong WordStudy id: ${wordStudy.id}")
        }

        // TODO: implement different study methods and StudyMethodFactory or WordStudyUpdateService
        DefaultStudyMethod.updateWordStudy(wordStudy, answer.isCorrect)
      }

    // TODO: do it in transaction
    Future.sequence(
      updatedWordStudies.map {
        dao.upsertWordStudy(_).map {
          case None => true
          case _ => false
        }
      }
    ).map(!_.contains(false))
  }
}


object TrainingService {

  case class TrainingNew(words: Seq[WordToStudy])

  case class TrainingResult(answers: Seq[TrainingAnswer])

  case class TrainingAnswer(id: Int, isCorrect: Boolean)

  case class TrainingStats(wordsTotal: Int, wordsToRepeat: Int, wordsLearned: Int, wordsToRepeatNow: Int)
}