package ru.art0.trainer.services.studies

import ru.art0.trainer.models.WordStudy

trait AbstractStudyMethod {

  val ProgressSymbols = "▁,▂,▃,▄,▅,▆,▇,█".split(",").array


  def updateWordStudy(wordStudy: WordStudy, correct: Boolean): WordStudy

  def relearn(wordStudy: WordStudy): WordStudy

  final def getProgressSymbol(wordStudy: WordStudy): String = getProgressSymbolForProgress(getProgress(wordStudy))

  protected def getProgress(wordStudy: WordStudy): Double

  final protected def getProgressSymbolForProgress(progress: Double): String = {
    val index = Math.floor(progress * ProgressSymbols.length).toInt
    if (index <= 0.0) {
      ProgressSymbols.head
    } else if (index >= ProgressSymbols.length) {
      ProgressSymbols.last
    } else {
      ProgressSymbols(index)
    }
  }

  protected def updateQuantities(wordStudy: WordStudy, isCorrect: Boolean): WordStudy = {
    wordStudy.copy(
      trainingQtyTotal = wordStudy.trainingQtyTotal + 1,
      trainingQtyCorrect = if (isCorrect) wordStudy.trainingQtyCorrect + 1 else wordStudy.trainingQtyCorrect
    )
  }
}
