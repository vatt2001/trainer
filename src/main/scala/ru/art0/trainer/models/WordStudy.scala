package ru.art0.trainer.models

import ru.art0.trainer.models.StudyMethod.StudyMethod

case class WordStudy(
  id: Int,
  userId: Int,
  wordId: Int,
  studyMethod: StudyMethod,
  trainingQtyTotal: Int,
  trainingQtyCorrect: Int
)
