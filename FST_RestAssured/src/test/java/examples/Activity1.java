package examples;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity1 {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    int petId;
    String baseUri = "https://petstore.swagger.io/v2/pet";

    @BeforeClass
    public void setUp() {

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addHeader("Content-Type", "application/json")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(600L), TimeUnit.MILLISECONDS)
                .expectBody("status", equalTo("alive"))
                .expectBody("name", equalTo("Riley"))
                .build();
    }

    @Test(priority = 1)
    public void addNewPet() {
        String reqBody = "{\"id\": 75232, \"name\": \"Riley\", \"status\": \"alive\"}";
        //Generate response
        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post();
        System.out.println(response.getBody().asString());
        petId = response.then().extract().path("id");
        System.out.println(petId);
        //Assertions
        response.then().spec(responseSpec);
    }

    @Test(priority = 2)
    public void getPetById() {
        //Generate Response
        Response response = given().spec(requestSpec)
                .pathParam("petId", petId)
                .when().get("/{petId}");
        System.out.println(response.getBody().asString());

        //Assertion
        response.then().spec(responseSpec);
    }

    @Test(priority=3)
    public void deletePet() {
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .when().pathParam("petId", "77232") // Set path parameter
                        .delete(baseUri + "/{petId}"); // Send DELETE request

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("77232"));
    }
}
