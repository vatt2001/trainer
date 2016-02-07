package ru.art0.trainer.models

import java.sql.{Date, Timestamp}
import java.time._

import slick.driver.JdbcProfile


trait AbstractModel {

  val driverProfile = slick.driver.SQLiteDriver.api

  import driverProfile._

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

  implicit lazy val studyMethodMapper = MappedColumnType.base[StudyMethod.StudyMethod, String](
    { b => b.toString },
    { i => StudyMethod.withName(i) }
  )
}
