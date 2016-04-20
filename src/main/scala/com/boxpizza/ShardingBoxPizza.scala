package com.boxpizza

import akka.actor.{ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.typesafe.config.ConfigFactory
import com.boxpizza.actors.WaiterActor
import com.boxpizza.api.RestInterface

object ShardedBoxPizza extends App {
  val config = ConfigFactory.load("sharded")
  implicit val system = ActorSystem(config getString "clustering.cluster.name", config)

  ClusterSharding(system).start(
    typeName = "Waiter",
    entityProps = WaiterActor.props,
    settings = ClusterShardingSettings(system),
    extractShardId = WaiterActor.extractShardId,
    extractEntityId = WaiterActor.extractEntityId)

  val waiter = ClusterSharding(system).shardRegion(WaiterActor.shardName)
  system.actorOf(Props(new RestInterface(waiter, config getInt "application.exposed-port")))
}
