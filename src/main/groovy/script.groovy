import com.softeam.formations.resources.dto.Matrix
import com.softeam.formations.resources.dto.Pair
import groovy.json.JsonOutput
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils


def makeMatrix() {
    Matrix m = new Matrix(3)

    for (i = 0; i < m.getNx(); i++) {
        for (j = 0; j < m.getNx(); j++) {
            m.set(i, j, i == j ? 1.0 / (j + 1.0) : 0.0)
        }
    }
    return m
}

def makeRequest(m, power) {

    HttpPost multRequest = new HttpPost("http://localhost:8080/matrix/v2/power");
    HttpClient client = HttpClientBuilder.create().build();

    def json = JsonOutput.toJson(new Pair(m, power))
    multRequest.setEntity(new StringEntity(json))
    multRequest.addHeader("Content-Type", "application/json")
    HttpResponse response = client.execute(multRequest);

    println(EntityUtils.toString(response.getEntity()))
}

makeRequest(makeMatrix(), 30)
println("END")