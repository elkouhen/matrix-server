import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SyncScenario extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")

  val scn = scenario("SyncSimulation")
    .exec(http("request_1")
    .post("/matrix/v1/power")
    .body(StringBody( """{"right": 3, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON)
    .pause(5)

  setUp(
    scn.inject(rampUsersPerSec(1) to (500) during (2 minutes))
  ).protocols(httpConf)
}

