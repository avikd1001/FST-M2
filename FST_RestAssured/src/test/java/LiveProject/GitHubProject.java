package LiveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class GitHubProject {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    String sshKey = "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";
    int Id;

    @BeforeClass
    public void setUp() {

        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token "+sshKey)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(3000L), TimeUnit.MILLISECONDS)
                .build();
    }

    @Test(priority = 1)
    public void gitPostSSH() {
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDOkCv0eTA6txzfIfoOvXFyE1gVdyEt3t+S7ujsoAda54gkU630Gw8njXoQtgBTbeDJ/x/BKD/I8N/fXoIvq/Q57FnSAUF8C5nf3cpe2afoz/lhC6I560H3u1iFJwqTQRH+BMWDE5KNIT4BdnTRlZfxLrqzmuQW/ZKiwl8ymnZ8BKieR8V6XSSGLNPyVFTMLOOKu4k8ObIl2EyXJp2ruyNJFbr2ReKRyuFXckRGkeSoKeCzCTH1naIqSDxoUm5on8ERNJhT8xhsSJa3FAnDXqrF5i0+U61WBOVW9LXqY7ycD8f0ijt2kIMYUmZVzosdwwRVQMSh+u8cWOD9tMcZcVab\"}";
        //Generate response
        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post("/user/keys");
        System.out.println(response.getBody().asPrettyString());
        Id = response.then().extract().path("id");
        System.out.println(Id);
        //Assertions
        response.then().statusCode(201);
        response.then().spec(responseSpec);
    }

    @Test(priority = 2)
    public void gitGetSSH() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", Id)
                .when().get("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());

        //Assertion
        response.then().statusCode(200);
        response.then().spec(responseSpec);
    }

    @Test(priority = 3)
    public void gitRemoveSSH() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", Id)
                .when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());

        //Assertion
        response.then().statusCode(204);
        response.then().spec(responseSpec);
    }

}
