package ru.art0.trainer.services.studies

import ru.art0.trainer.models.WordStudy

trait AbstractStudyMethod {
  def updateWordStudy(wordStudy: WordStudy, correct: Boolean): WordStudy
}
