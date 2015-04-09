import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class MongoSyncScenario extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")

  val scn = scenario("AsyncBasicSimulation")
    .exec(http("request_1")
    .post("/matrix/v3/power")
    .body(StringBody( """{"right": 3, "left": 3}""")).asJSON)
    .pause(5)

  setUp(
    scn.inject(rampUsersPerSec(1) to (1000) during (2 minutes))
  ).protocols(httpConf)
}
