package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //headers
    Map<String, String> reqHeaders = new HashMap<>();
    //API resource path
    String resourcePath = "/api/users";

    //Creating the Pact
    @Pact(consumer= "UserConsumer", provider="UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        //Set the header
        reqHeaders.put("Content-Type", "application/json");

        //Create Req & Resp body
        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        return builder.given("Request to create a user")
                //Request & Response builder
                .uponReceiving("Request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(reqHeaders)
                    .body(reqResBody)
                .willRespondWith()
                    .status(201)
                    .body(reqResBody)
                //Add more API methods here with upon receiving
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8181")
    public void consumerTest() {
        //Set baseURI
        String baseURI = "http://localhost:8181";
        //Define Request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Avik");
        reqBody.put("lastName", "D");
        reqBody.put("email", "avik@email.com");

        Response response = given().headers(reqHeaders).body(reqBody)
                .when().post(baseURI + resourcePath);

        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(201);
    }

}
