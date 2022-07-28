package examples;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FirstTest {
    @Test
    public void getReqTest(){
        //set base URI
        String baseURI = "https://petstore.swagger.io/v2/pet";

        //Build Request
        given()
            .header("Content-Type", "application/json")
            .queryParam("status", "sold")

        //Send Request
        .when()
                .get(baseURI + "/findByStatus")

        //Validation
        .then()
                .statusCode(200)
                .body("[0].status", equalTo("sold"));

    }
}
