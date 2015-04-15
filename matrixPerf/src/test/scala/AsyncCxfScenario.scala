import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class AsyncCxfScenario extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")

  val scn = scenario("AsyncCxfSimulation")
    .exec(http("request_1")
    .post("/matrixServerCxf/services/rest/matrix/power")
    .body(StringBody( """{"right": 3, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON)
    .pause(5)

  setUp(
    scn.inject(rampUsersPerSec(1) to (100) during (1 minutes))
  ).protocols(httpConf)
}
