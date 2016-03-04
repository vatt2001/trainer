package ru.art0.trainer.helpers

import java.time.Instant

import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudyStatusEnum.WordToStudyStatus
import ru.art0.trainer.models.{WordStudy, Word}
import ru.art0.trainer.services.studies.DefaultStudyMethod

object WordToStudyHelper {
  case class WordToStudy(
    id: Int,
    spelling: String,
    translation: String,
    transcription: Option[String],
    trainingQtyTotal: Int,
    trainingQtyCorrect: Int,
    status: WordToStudyStatus,
    progressSymbol: String
  )

  def spawnWordStudy(w: Word, ws: WordStudy): WordToStudy = {
    WordToStudy(
      id = ws.id,
      spelling = w.spelling,
      translation = w.translation,
      transcription = w.transcription,
      trainingQtyTotal = ws.trainingQtyTotal,
      trainingQtyCorrect = ws.trainingQtyCorrect,
      status = resolveStatus(ws),
      progressSymbol = DefaultStudyMethod.getProgressSymbol(ws)
    )
  }

  private def resolveStatus(ws: WordStudy): WordToStudyStatus = {
    import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudyStatusEnum._

    if (ws.isLearned) {
      Learned
    } else if (ws.nextTrainingAt.isBefore(Instant.now())) {
      LearningReady
    } else {
      LearningWaiting
    }
  }

  object WordToStudyStatusEnum extends Enumeration {
    type WordToStudyStatus = Value
    val Learned = Value("learned")
    val LearningWaiting = Value("learning-waiting")
    val LearningReady = Value("learning-ready")
  }
}
