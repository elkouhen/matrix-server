package computerdatabase // 1

import io.gatling.core.Predef._

// 2
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SyncBasicSimulation extends Simulation { // 3

  val httpConf = http // 4
    .baseURL("http://localhost:8080") // 5

  val scn = scenario("SyncBasicSimulation") // 7
    .exec(http("request_1")  // 8
    .post("/matrix/v1/power")
    .body(StringBody("""{"right": 3, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON) // 9
    .pause(5) // 10

  setUp( // 11
    scn.inject(rampUsersPerSec(1) to (500) during (2 minutes)) // 12
  ).protocols(httpConf)
}

class AsyncBasicSimulation extends Simulation { // 3

  val httpConf = http // 4
    .baseURL("http://localhost:8080") // 5

  val scn = scenario("AsyncBasicSimulation") // 7
    .exec(http("request_1")  // 8
    .post("/matrix/v2/power")
    .body(StringBody("""{"right": 3, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON) // 9
    .pause(5) // 10

  setUp( // 11
    scn.inject(rampUsersPerSec(1) to (1000) during (2 minutes)) // 12
  ).protocols(httpConf) // 13
}


class AsyncBasicSimulation2 extends Simulation { // 3

  val httpConf = http // 4
    .baseURL("http://localhost:8080") // 5

  val scn = scenario("AsyncBasicSimulation") // 7
    .exec(http("request_1")  // 8
    .post("/matrix/v3/power")
    .body(StringBody("""{"right": 3, "left": 3}""")).asJSON) // 9
    .pause(5) // 10

  setUp( // 11
    scn.inject(rampUsersPerSec(1) to (1000) during (2 minutes)) // 12
  ).protocols(httpConf) // 13
}
