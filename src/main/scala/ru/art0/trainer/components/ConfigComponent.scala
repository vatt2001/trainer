package ru.art0.trainer.components

import com.typesafe.config.{ConfigFactory, Config}
import ru.art0.trainer.components.GeneralConfig.Keys

trait ConfigComponent {

  def config: GeneralConfig
  
}


trait GeneralConfig {

  def loadConfig: Config

  val config = loadConfig

  val dbUrl = config.getString(Keys.DbUrl)
  val dbDriver = config.getString(Keys.DbDriver)

  val trainerHost = config.getString(Keys.TrainerHost)
  val trainerPort = config.getInt(Keys.TrainerPort)

  val trainerTrainingLimit = config.getInt(Keys.TrainerTrainingLimit)

}


class GeneralConfigImpl extends GeneralConfig {

  override def loadConfig: Config = ConfigFactory.load()

}


object GeneralConfig {
  object Keys {
    val DbUrl = "db.url"
    val DbDriver = "db.driver"
    val TrainerHost = "trainer.host"
    val TrainerPort = "trainer.port"
    val TrainerTrainingLimit = "trainer.training.limit"
  }
}
