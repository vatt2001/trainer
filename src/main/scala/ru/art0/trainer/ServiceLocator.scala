package ru.art0.trainer

import ru.art0.trainer.services.{WordsService, TrainingService, AuthService}

object ServiceLocator {

  private lazy val authService = new AuthService

  private lazy val trainingService = new TrainingService

  private lazy val wordService = new WordsService

  def getAuthService = authService

  def getTrainingService = trainingService

  def getWordService = wordService
}
