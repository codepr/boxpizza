package com.boxpizza.actors

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}

case class Cook(pizza: String, client: ActorRef)

class CookActor extends Actor with ActorLogging {
  import WaiterActor._

  def receive = {
    case Cook(pizza, client) =>
      pizza match {
        case "margherita" =>
          log.info("Preparing a margherita")
          Thread.sleep(2)
          sender ! Cost("margherita", 3.80, client)
        case "viennese" =>
          log.info("Preparing a viennese")
          Thread.sleep(3)
          sender ! Cost("viennese", 4.00, client)
        case "maialata" =>
          log.info("Preparing a maialata")
          Thread.sleep(5)
          sender ! Cost("maialata", 5.00, client)
        case "diavola" =>
          log.info("Preparing a diavola")
          Thread.sleep(3)
          sender ! Cost("diavola", 4.00, client)
        case "marinara" =>
          log.info("Preparing a marinara")
          Thread.sleep(2)
          sender ! Cost("marinara", 3.00, client)
        case "prosciutto" =>
          log.info("Preparing a prosciutto")
          Thread.sleep(3)
          sender ! Cost("prosciutto", 3.00, client)
        case _ =>
          log.info("Preparing a margherita")
          Thread.sleep(2)
          sender ! Cost("margherita", 3.80, client)
      }
      self ! PoisonPill
  }

}
