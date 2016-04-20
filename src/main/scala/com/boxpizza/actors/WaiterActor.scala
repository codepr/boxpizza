package com.boxpizza.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.{ExtractEntityId, ExtractShardId}
import spray.json.DefaultJsonProtocol._

object WaiterActor {

  def props = Props[WaiterActor]

  def shardName = "Waiter"

  val numberOfShards = 100

  val extractShardId: ExtractShardId = {
    case CookRequest(id, _) => (id % 100).toString
  }

  val extractEntityId: ExtractEntityId = {
    case m: CookRequest => (m.id.toString, m)
  }

  case class CookRequest(id: Long, pizza: String)

  case class Cost(pizza: String, cost: Double, client: ActorRef)

  case class Bill(pizza: String, bill: Double)

  object Bill {
    implicit val goJson = jsonFormat2(Bill.apply)
  }
}

class WaiterActor extends Actor with ActorLogging {
  import WaiterActor._

  def receive = {

    case CookRequest(id, pizza) =>
      log.info(s"$pizza ordered")
      context.actorOf(Props[CookActor]) ! Cook(pizza, sender)

    case Cost(pizza, cost, client) =>
      client ! Bill(s"$pizza", cost)

    case _ => log.info("Still waiting for an ordination")

  }

}
