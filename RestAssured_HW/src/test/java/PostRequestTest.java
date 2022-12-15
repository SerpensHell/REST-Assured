import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class PostRequestTest {
    private static RequestSpecification requestSpec;

    @BeforeEach
    public void shadifyApiSpec() {
        requestSpec = RestAssured.given()
                .baseUri("https://shadify.dev/api")
                .contentType(ContentType.JSON);
    }

    @Test
    public void sudokuVerifierOk() {
        String postJson = "{\r\n" +
                " \"task\": [\n" +
                "        [9, 6, 3, 2, 8, 5, 7, 4, 1],\n" +
                "        [8, 5, 2, 1, 7, 4, 6, 3, 9],\n" +
                "        [1, 7, 4, 3, 9, 6, 8, 5, 2],\n" +
                "        [6, 3, 9, 8, 5, 2, 3, 1, 7],\n" +
                "        [5, 2, 8, 7, 4, 1, 3, 9, 6],\n" +
                "        [7, 4, 1, 9, 6, 3, 5, 2, 8],\n" +
                "        [4, 1, 7, 6, 3, 9, 2, 8, 5],\n" +
                "        [3, 9, 6, 5, 2, 8, 1, 7, 4],\n" +
                "        [2, 8, 5, 4, 1, 7, 9, 6, 3]\n" +
                "    ]\n"+
                "}";
        requestSpec.body(postJson);
        requestSpec.log().all();
        Response response = requestSpec.post("/sudoku/verifier");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.assertThat().header("Content-Type", equalTo("application/json"));
        validatableResponse.log().headers();
        validatableResponse.assertThat().body("size()", greaterThan(0));
        validatableResponse.log().body();
        validatableResponse.statusCode(200);
        validatableResponse.log().status();
    }

    @Test
    public void sudokuBadRequest() {
        String postJson = "{\r\n" +
                " \"task\": [\n" +
                "        [9, 6, 3, 2, 8, 5, 7, 4, 1],\n" +
                "        [8, 5, 2, 1, 7, 4, 6, 3, 9],\n" +
                "        [1, 7, 4, 3, 9, 6, 8, 5, 2],\n" +
                "        [6, 3, 9, 8, 5, 2, 3, 1, 7],\n" +
                "        [5, 2, 8, 7, 4, 1, 3, 9, 6],\n" +
                "        [7, 4, 1, 9, 6, 3, 5, 2, 8],\n" +
                "        [4, 1, 7, 6, 3, 9, 2, 8, 5],\n" +
                "        [3, 9, 6, 5, 2, 8, 1, 7, 4],\n" +
                "        [2, 8, 5, 4, 1\n" +
                "    ]\n"+
                "}";
        requestSpec
                .body(postJson)
                .log().all()
                .when()
                .post("/sudoku/verifier")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(400);
    }

    @Test
    public void takuzuVerifierOk() {
        String postJson = "{\r\n" +
                "\"task\": [\n" +
                "        [\"1\", \"0\", \"1\", \"0\"],\n" +
                "        [\"1\", \"1\", \"0\", \"0\"],\n" +
                "        [\"0\", \"0\", \"1\", \"1\"],\n" +
                "        [\"0\", \"1\", \"0\", \"1\"]\n" +
                "    ]\n"+
                "}";
        requestSpec.body(postJson);
        requestSpec.log().all();
        Response response = requestSpec.post("/takuzu/verifier");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.assertThat().header("Content-Type", equalTo("application/json"));
        validatableResponse.log().headers();
        validatableResponse.assertThat().body("size()", greaterThan(0));
        validatableResponse.log().body();
        validatableResponse.statusCode(200);
        validatableResponse.log().status();
    }

    @Test
    public void takuzuNotFound() {
        String postJson = "{\r\n" +
                "\"task\": [\n" +
                "        [\"1\", \"0\", \"1\", \"0\"],\n" +
                "        [\"1\", \"1\", \"0\", \"0\"],\n" +
                "        [\"0\", \"0\", \"1\", \"1\"],\n" +
                "        [\"0\", \"1\", \"0\", \"1\"]\n" +
                "    ]\n"+
                "}";
        requestSpec
                .body(postJson)
                .log().all()
                .when()
                .post("/tekizu/verifier")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(404);
    }
}
