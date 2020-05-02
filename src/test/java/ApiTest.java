import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Test
    public void assertGetResponse() {
        RestAssured.baseURI = "http://localhost:8080";
        given()
                .request()
                .with()
                .contentType(ContentType.JSON)
                .log().uri()
                .when()
                .get("/test")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("productName", is("Corn flakes"))
                .body("processIdentifier", is("1212454588"))
                .rootPath("parameters")
                .body("productLine", is("FMCG"))
                .appendRootPath("producers")
                .body("Kellogg.size()", is(2))
                .body("Nestle.size()", is(2))
                .body("Kellogg.stock", is(true))
                .body("Kellogg.additionalInfo", is("abc-01"))
                .detachRootPath("producers")
                .body("Grains.size()", is(2))
                .body("Grains[0]", hasKey("attribute"))
                .body("Grains.find{it.attribute == \"Organic available\"}.value", is(true))
                .appendRootPath("Preservatives")
                .body("attribute", hasItems("Preservative A", "Preservative B"));
    }
}
