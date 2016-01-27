package ru.art0.trainer.models

import java.sql.{Date, Timestamp}
import java.time._

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.{PositionedParameters, SetParameter}

trait AbstractModel {
  val databaseProfile: JdbcProfile
  def implicits = databaseProfile.simple

  import databaseProfile.simple._

  implicit lazy val instantColumnType = MappedColumnType.base[Instant, Timestamp] (
    { i => Timestamp.from(i) },
    { t => t.toInstant }
  )
  
  implicit lazy val localDateColumnType = MappedColumnType.base[LocalDate, Date] (
    { d => Date.valueOf(d) },
    { d => d.toLocalDate }
  )

  implicit lazy val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
    { dateTime => Timestamp.valueOf(dateTime)},
    { timestamp => timestamp.toLocalDateTime}
  )
}
