import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class AsyncScenario extends Simulation {

  val httpConf = http.baseURL("http://localhost:8081")

  //val appContext = "/matrixServerCxf/services/rest/matrix/"
  val appContext = "/matrix/"
  val version = "V3"
  val resource = "/power"

  val scn = scenario("AsyncSimulation")
    .exec(http("request")
      .post(appContext + version + resource)
      .body(StringBody("""{"right": 2, "left": {"nx": "3", "data": [1, 0, 0, 0, 1, 0, 0, 0, 1]}}""")).asJSON)

  setUp(
    scn.inject(
        //rampUsersPerSec(1) to (2) during (2 minutes)
        constantUsersPerSec(100) during(2 minutes)
        )).protocols(httpConf)
}
