package ru.art0.trainer

import akka.actor.{ActorSystem, ActorContext}
import com.typesafe.config.{ConfigFactory, Config}
import ru.art0.trainer.components._
import ru.art0.trainer.models.{WordStudyModel, UserModel, WordModel}
import ru.art0.trainer.services._
import slick.jdbc.JdbcBackend
import akka.actor.ActorSystem

object ComponentWiring {

  private lazy val generalConfigImpl = new GeneralConfigImpl
  trait ConfigComponentImpl extends ConfigComponent {
    override def config: GeneralConfig = generalConfigImpl
  }

  private lazy val databaseImpl = new DatabaseImpl with ConfigComponentImpl
  trait DatabaseComponentImpl extends DatabaseComponent {
    override def database: Database = databaseImpl
  }

  private lazy val daoImpl =
    new DaoImpl
      with DatabaseComponentImpl
      with ExecutionContextComponentImpl
      with WordModel
      with UserModel
      with WordStudyModel
  trait DaoComponentImpl extends DaoComponent {
    override def dao: Dao = daoImpl
  }

  private lazy val wordsServiceImpl =
    new WordsServiceImpl with DaoComponentImpl with ExecutionContextComponentImpl
  trait WordsServiceComponentImpl extends WordsServiceComponent {
    override def wordsService: WordsService = wordsServiceImpl
  }

  private lazy val trainingServiceImpl =
    new TrainingServiceImpl with DaoComponentImpl with ConfigComponentImpl with ExecutionContextComponentImpl
  trait TrainingServiceComponentImpl extends TrainingServiceComponent {
    override def trainingService: TrainingService = trainingServiceImpl
  }

  private lazy val authServiceImpl = new AuthServiceImpl
  trait AuthServiceComponentImpl extends AuthServiceComponent {
    override def authService: AuthService = authServiceImpl
  }
}
