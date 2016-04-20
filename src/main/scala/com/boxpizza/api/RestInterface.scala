package com.boxpizza.api

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.io.IO
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import spray.routing._
import com.boxpizza.actors.WaiterActor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RestInterface(waiter: ActorRef, exposedPort: Int) extends Actor with HttpServiceBase with ActorLogging {
  val route: Route = {
    path("pizzeria" / "\\S+".r) { order =>
      get {
        complete {
          log.info(s"Request for pizza $order")
          waiter.ask(order)(5 seconds).mapTo[Bill]
        }
      }
    }
  }

  def receive = runRoute(route)

  implicit val system = context.system
  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
