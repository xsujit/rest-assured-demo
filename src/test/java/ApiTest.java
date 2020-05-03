import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void assertGetResponse() {
        given()
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .get("/test")
                .then()
                .log()
                .ifValidationFails()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("productName", is("Corn flakes"))
                .body("processIdentifier", is("1212454588"))
                .rootPath("parameters")
                .body("productLine", is("FMCG"))
                .appendRootPath("producers")
                .body("size()", is(2))
                .body("Kellogg.size()", is(2))
                .body("Nestle.size()", is(2))
                .body("Kellogg.stock", is(true))
                .body("Kellogg.additionalInfo", is("abc-01"))
                .detachRootPath("producers")
                // .body("Grains.findAll{true}", everyItem(hasItem("true")))
                .body("Grains.size()", is(2))
                .body("Grains[0]", hasKey("attribute"))
                .body("Grains.find{it.attribute == \"Organic available\"}.value", is(true))
                .appendRootPath("Preservatives")
                .body("attribute", hasItems("Preservative A", "Preservative B"));
    }

    @Test
    public void schemaTest() {
        given()
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .get("/schema")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schema.json"));
    }

    @Test
    public void postTest() {
        String payload = "{\"Title\": \"Senior Developer\"}";
        given()
                .body(payload)
                .contentType(ContentType.JSON)
                .log().body().log().uri()
                .when()
                .post("/send")
                .then()
                .log().body()
                .and()
                .assertThat()
                .statusCode(200);
    }
}
