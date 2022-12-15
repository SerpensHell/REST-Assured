import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class GetRequestTest {
    private static RequestSpecification requestSpec;

    @BeforeEach
    public void pankapiSpec() {
        requestSpec = RestAssured.given()
                .baseUri("https://api.punkapi.com/v2")
                .contentType(ContentType.JSON);
    }

    @Test
    public void pankApiPaginationBadRequest() {
        requestSpec
                .queryParam("page", -1)
                .queryParam("per_page", 80)
                .log().all()
                .when()
                .get("/beers")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(400);
    }

    @Test
    public void pankApiPaginationNotFound() {
        requestSpec
                .queryParam("page", -1)
                .queryParam("per_page", 80)
                .log().all()
                .when()
                .get("/biers")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(404);
    }

    @Test
    public void pankApiPaginationSizeOk() {
        requestSpec.queryParam("page", 1);
        requestSpec.queryParam("per_page", 80);
        requestSpec.log().all();
        Response response = requestSpec.get("/beers");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.assertThat().header("Content-Type", equalTo("application/json; charset=utf-8"));
        validatableResponse.log().headers();
        validatableResponse.assertThat().body("size()", greaterThan(0));
        validatableResponse.log().body();
        validatableResponse.statusCode(200);
        validatableResponse.log().status();
    }

    @Test
    public void pankApiPaginationNameOk() {
        requestSpec.queryParam("page", 1);
        requestSpec.queryParam("per_page", 80);
        requestSpec.log().all();
        Response response = requestSpec.get("/beers");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.assertThat().header("Content-Type", equalTo("application/json; charset=utf-8"));
        validatableResponse.log().headers();
        validatableResponse.assertThat().body("[1].name", containsString("Trashy Blonde"));
        validatableResponse.log().body();
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.log().status();
    }
}