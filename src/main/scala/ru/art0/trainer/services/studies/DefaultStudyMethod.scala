package ru.art0.trainer.services.studies

import java.time.Instant
import java.time.temporal.{ChronoUnit, TemporalUnit}

import ru.art0.trainer.models.WordStudy

import scala.concurrent.duration._

class DefaultStudyMethod extends AbstractStudyMethod {
  
  val Intervals = Array(
    1.minute,
    1.minute,
    1.minute
  )
  
  override def updateWordStudy(wordStudy: WordStudy, isCorrect: Boolean): WordStudy = {
    val updatedWordStudy =
      if (isCorrect) {
        wordStudy.copy(
          nextTrainingAt = resolveNextTraining(wordStudy),
          trainingStage = wordStudy.trainingStage + 1,
          isLearned = if (wordStudy.trainingStage == Intervals.length) true else false
        )
      } else {
        wordStudy.copy(
          nextTrainingAt = now,
          trainingStage = 0
        )
      }

    updateQuantities(updatedWordStudy, isCorrect)
  }


  override def relearn(wordStudy: WordStudy): WordStudy = {
    wordStudy.copy(
      isLearned = false,
      nextTrainingAt = now,
      trainingStage = 0
    )
  }


  override protected def getProgress(wordStudy: WordStudy): Double = wordStudy.trainingStage.toDouble / Intervals.length

  private def resolveNextTraining(wordStudy: WordStudy): Instant = {
    val delay = Intervals.lift(wordStudy.trainingStage).getOrElse(0.seconds)
    now.plus(delay.toMinutes, ChronoUnit.MINUTES)
  }

  private def now = Instant.now()
}

object DefaultStudyMethod extends DefaultStudyMethod