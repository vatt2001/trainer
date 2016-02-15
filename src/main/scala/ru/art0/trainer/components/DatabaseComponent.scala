package ru.art0.trainer.components

import slick.dbio.{NoStream, DBIOAction}
import slick.jdbc.JdbcBackend._

import scala.concurrent.Future

trait DatabaseComponent {

  def database: Database
}

trait Database {

  def db: DatabaseDef

  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R]
}

trait DatabaseImpl extends Database {

  this: ConfigComponent =>

  override val db = Database.forURL(config.dbUrl)

  override def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(a)
}