package ru.art0.trainer.components

import slick.jdbc.JdbcBackend._

trait DatabaseComponent {

  def database: Database

}

trait Database {

  def db: DatabaseDef
}

trait DatabaseImpl extends Database {

  this: ConfigComponent =>

  val db = Database.forURL(config.dbUrl)

}