package ru.art0.trainer.services.studies

import java.time.Instant
import java.time.temporal.{ChronoUnit, TemporalUnit}

import ru.art0.trainer.models.WordStudy

import scala.concurrent.duration._

class DefaultStudyMethod extends AbstractStudyMethod {
  
  val DefaultDelay: Duration = 5 minutes
  
  override def updateWordStudy(wordStudy: WordStudy, correct: Boolean): WordStudy = {
    wordStudy.copy(
      nextTrainingAt = incrementNextTraining(wordStudy, correct),
      trainingQtyTotal = wordStudy.trainingQtyTotal + 1,
      trainingQtyCorrect = if (correct) wordStudy.trainingQtyCorrect + 1 else wordStudy.trainingQtyCorrect
    )
  }
  
  private def incrementNextTraining(wordStudy: WordStudy, correct: Boolean): Instant = {
    val base = Instant.now()
    if (correct) {
      base.plus(DefaultDelay.toMinutes, ChronoUnit.MINUTES)
    } else {
      base
    }
  }
}

object DefaultStudyMethod extends DefaultStudyMethod