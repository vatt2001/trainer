package ru.art0.trainer.helpers

import ru.art0.trainer.services.TrainingService.{TrainingStats, TrainingResult}
import ru.art0.trainer.services.WordsService.AddWordData
import spray.json._

object JsonProtocol extends DefaultJsonProtocol {
  implicit val addWordDataFormat = jsonFormat2(AddWordData)
  implicit val trainingResultFormat = jsonFormat1(TrainingResult)
  implicit val trainingStatsFormat = jsonFormat3(TrainingStats)
}
