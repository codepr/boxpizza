# boxpizza - akka-cluster-sharding
Simple toy actor system modeling a pizzeria, it's main purpose is the exploration
of akka toolkit functionalities and spray.

## Testing
Simply open a browser and request to localhost:8080/boxpizza/&lt;pizza&gt;
### Adding a node
In order to simulate a second node on the same machine, we should run the second JVM
overriding some configurations, specifically the exposed-port and the seed-node port.
#### Node A
```sh
sbt run
```
#### Node B
```sh
sbt run -Dapplication.exposed-port=8081 -Dclustering.port=2552
```
### Performance
**Dependancies:** parallel, ab
Make some requests specified inside shardedURLs.txt in parallel, 2000 times each with keep-alive connection,
in order to simulate a round-robin load balancer, shardedURLs.txt contains the same route repeated for each node
(e.g. 8080 and 8081)
```sh
cat src/main/resources/URLs.txt | parallel -j 10 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```
