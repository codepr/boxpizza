package com.boxpizza.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import spray.json.DefaultJsonProtocol._

object WaiterActor {
  case class Cost(pizza: String, cost: Double, client: ActorRef)

  case class Bill(pizza: String, bill: Double)

  object Bill {
    implicit val goJson = jsonFormat2(Bill.apply)
  }
}

class WaiterActor extends Actor with ActorLogging {
  import WaiterActor._

  def receive = {

    case pizza: String =>
      log.info(s"$pizza ordered")
      context.actorOf(Props[CookActor]) ! Cook(pizza, sender)

    case Cost(pizza, cost, client) =>
      client ! Bill(s"$pizza", cost)

    case _ => log.info("Still waiting for an ordination")

  }

}
