package com.boxpizza

import akka.actor.{ActorSystem, Props}
import akka.routing._
import akka.cluster._
import akka.cluster.routing._
import com.typesafe.config.ConfigFactory
import com.boxpizza.actors.WaiterActor
import com.boxpizza.api.RestInterface

object SingleBoxPizza extends App {
  val config = ConfigFactory.parseString(s"""
    akka.actor.provider = akka.cluster.ClusterActorRefProvider
    akka.cluster.seed-nodes = ["akka.tcp://ClusterBoxPizza@127.0.0.1:2500"]
  """)
  implicit val system = ActorSystem("ClusterBoxPizza", config)
  Cluster(system).registerOnMemberUp {
    val roundRobinPool = RoundRobinPool(nrOfInstances = 10)
    val clusterRoutingSettings = ClusterRouterPoolSettings(totalInstances = 10, maxInstancesPerNode = 5, allowLocalRoutees = true, useRole = None)
    val clusterPool = ClusterRouterPool(roundRobinPool, clusterRoutingSettings)
    val router = system.actorOf(clusterPool.props(Props[WaiterActor]))
    system.actorOf(Props(new RestInterface(router, 8080)))
  }
}
