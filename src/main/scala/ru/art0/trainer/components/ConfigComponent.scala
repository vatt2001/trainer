package ru.art0.trainer.components

import com.typesafe.config.{ConfigFactory, Config}
import ru.art0.trainer.components.GeneralConfig.Keys

trait ConfigComponent {

  def config: GeneralConfig
  
}


trait GeneralConfig {

  def loadConfig: Config

  val dbUrl = config.getString(Keys.DbUrl)

  val trainerHost = config.getString(Keys.TrainerHost)

  val trainerPort = config.getInt(Keys.TrainerPort)

  private val config = loadConfig
}


class GeneralConfigImpl extends GeneralConfig {

  override def loadConfig: Config = ConfigFactory.load()

}


object GeneralConfig {
  object Keys {
    val DbUrl = "db.url"
    val TrainerHost = "trainer.host"
    val TrainerPort = "trainer.port"
  }
}