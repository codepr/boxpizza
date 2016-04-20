# boxpizza - akka-cluster
Simple toy actor system modeling a pizzeria, it's main purpose is the exploration
of akka toolkit functionalities and spray.

## Testing
Simply open a browser and request to localhost:8080/boxpizza/&lt;pizza&gt;

### Adding a node
In order to simulate a second node on the same machine, we have to run the
second overriding some configurations, specifically the seed-port to connect
nodes to each-other

| Node   | Command                                                              |
| ------ |:--------------------------------------------------------------------:|
|Node A  |sbt run -Dakka.remote.netty.tcp.port=2500                             |
|Node B  |sbt run -Dakka.remote.netty.tcp.port=0 -Dapplication.exposed-port=8081|


### Performance
**Dependancies:** parallel, ab


Make some requests specified inside URLs.txt in parallel, 2000 times each with keep-alive connection
```sh
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```
