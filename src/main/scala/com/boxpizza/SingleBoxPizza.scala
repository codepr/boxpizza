package com.boxpizza

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import com.boxpizza.actors.WaiterActor
import com.boxpizza.api.RestInterface

object SingleBoxPizza extends App {
  implicit val system = ActorSystem("boxpizza")
  val waiter = system.actorOf(Props[WaiterActor])
  system.actorOf(Props(new RestInterface(waiter, 8080)))
}
