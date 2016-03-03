package ru.art0.trainer.helpers

import java.time.Instant

import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudyStatus.WordToStudyStatus
import ru.art0.trainer.models.{WordStudy, Word}

object WordToStudyHelper {
  case class WordToStudy(
    id: Int,
    spelling: String,
    translation: String,
    transcription: Option[String],
    trainingQtyTotal: Int,
    trainingQtyCorrect: Int,
    status: WordToStudyStatus
  )

  def spawnWordStudy(w: Word, ws: WordStudy): WordToStudy = {
    WordToStudy(
      id = ws.id,
      spelling = w.spelling,
      translation = w.translation,
      transcription = w.transcription,
      trainingQtyTotal = ws.trainingQtyTotal,
      trainingQtyCorrect = ws.trainingQtyCorrect,
      status = resolveStatus(ws)
    )
  }

  private def resolveStatus(ws: WordStudy): WordToStudyStatus = {
    import ru.art0.trainer.helpers.WordToStudyHelper.WordToStudyStatus._

    if (ws.isLearned) {
      Learned
    } else if (ws.nextTrainingAt.isBefore(Instant.now())) {
      LearningReady
    } else {
      LearningWaiting
    }
  }

  object WordToStudyStatus extends Enumeration {
    type WordToStudyStatus = Value
    val Learned = Value("learned")
    val LearningWaiting = Value("learning-waiting")
    val LearningReady = Value("learning-ready")
  }
}
