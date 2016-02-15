package ru.art0.trainer

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import ru.art0.trainer.ComponentWiring.ConfigComponentImpl
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App with ConfigComponentImpl {

  implicit val system = ActorSystem("trainer")

  val service = system.actorOf(Props[TrainerApiActor], "service")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(service, interface = config.trainerHost, port = config.trainerPort)
}
