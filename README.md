# boxpizza
Simple toy actor system modeling a pizzeria, it's main purpose is the exploration
of akka toolkit functionalities and spray.

## Testing
Simply open a browser and request to localhost:8080/boxpizza/&lt;pizza&gt;
### Performance
**Dependancies:** parallel, ab

Make some requests specified inside URLs.txt in parallel, 2000 times each with keep-alive connection
```sh
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```
