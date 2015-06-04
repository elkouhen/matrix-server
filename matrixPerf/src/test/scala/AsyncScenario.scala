import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class AsyncScenario extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")

  //val appContext = "/matrixServerCxf/services/rest/matrix/"
  val appContext = "/matrix/"
  val version = "V3"
  val scn = scenario("AsyncSimulation")
    .exec(http("request_2")
    .post(appContext + version  + "/power")
    .body(StringBody( """{"right": 2, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON)
    .pause(5)

  setUp(
    scn.inject(rampUsersPerSec(1) to (200) during (1 minutes))
  ).protocols(httpConf)
}
