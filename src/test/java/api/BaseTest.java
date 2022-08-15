package api;

import io.restassured.response.ExtractableResponse;
import io.restassured.specification.ResponseSpecification;
import models.ErrorModel;
import models.UserDataModel;
import models.ResponseModel;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;

public class BaseTest {
    public static String baseUrl = "https://gorest.co.in/public/v2/";
    private String authToken = "Bearer 86aae42b2e40c785068160b93c9640f6fd88fe86cd32f7468f5c116561a62d74";
    private  String expiredToken ="Bearer be0b1ec0d8239ef812c7e8d7a45f1d4d29daafd4a4dc56c9e78c1c855772d962";

    public String getAuthToken() {
        return authToken;
    }
    public String getExpiredToken(){return expiredToken;}

    public void setSpec(ResponseSpecification responseSpec) {
        Specifications.installSpecification(Specifications.requestSpecification(baseUrl), responseSpec);
    }

    public UserDataModel postValidUser(String url, UserDataModel user) {
        return given()
                .header("Authorization", getAuthToken())
                .body(user)
                .when()
                .post(url)
                .then()
                .extract().as(UserDataModel.class);

    }
    public List<ErrorModel> postInvalidUser(String url, UserDataModel user) {
    return  given()
            .header("Authorization",getAuthToken())
            .body(user)
            .when()
            .post(url)
            .then()
            .extract().body().jsonPath().getList(rootPath,ErrorModel.class);
    }
    public List<UserDataModel> getUsers(String url) {
        return given()
                .get(url)
                .then().log().all()
                .extract().body().jsonPath().getList(rootPath, UserDataModel.class);
    }

    public UserDataModel getUserById(String url) {
        return given()
                .header("Authorization", getAuthToken())
                .get(url)
                .then().log().all()
                .extract().as(UserDataModel.class);
    }

    public String getNotExisting(String url) {
        Map<String, String> errorMap = given()
                .header("Authorization", getAuthToken())
                .get(url)
                .then().log().all()
                .extract().body().jsonPath().getMap(rootPath);
        return errorMap.get("message");
    }

    public String authWithExpiredToken(String url, UserDataModel user){
        Map<String, String> errorMap = given()
                .header("Authorization",expiredToken)
                .body(user)
                .when()
                .post(url)
                .then().log().all()
                .extract().body().jsonPath().getMap(rootPath);
        return errorMap.get("message");
    }

    public String authWithoutToken(String url, UserDataModel user){
        Map<String, String> errorMap = given()
                .body(user)
                .when()
                .post(url)
                .then().log().all()
                .extract().body().jsonPath().getMap(rootPath);
        return errorMap.get("message");
    }
    public ResponseModel putUserEmail(String url,String email) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        ExtractableResponse response = given()
                .header("Authorization", getAuthToken())
                .body(requestBody)
                .put(url)
                .then().log().all()
                .extract();

        if (response.statusCode() < 400) {
          UserDataModel success =response.as( UserDataModel.class);
            return ResponseModel.createSuccess(success);
        } else {
            List<ErrorModel> error = response.body().jsonPath().getList(rootPath, ErrorModel.class);
            return ResponseModel.createError(error);
        }
    }

    public String putInvalidGender(String url, JSONObject requestBody){
        Map<String, String> errorMap = given()
                .header("Authorization", getAuthToken())
                .body(requestBody)
                .when()
                .put(url)
                .then().log().all()
                .extract().body().jsonPath().getMap(rootPath);
        return errorMap.get("message");
    }

    public UserDataModel patchUser(String url, UserDataModel user){
        // name email gender status
        return  given()
                .header("Authorization", getAuthToken())
                .body(user)
                .patch(url)
                .then().log().all()
                .extract().as(UserDataModel.class);
    }

    public ExtractableResponse deleteUser(String url){
        return  given()
                .header("Authorization", getAuthToken())
                .delete(url)
                .then().log().all()
                .extract();
    }

    public HttpURLConnection sendDeleteRequest(String url, String token) {
        URL uri;
        HttpURLConnection con = null;
        try {
            uri = new URL(url);
            con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod("DELETE"); //type: POST, PUT, DELETE, GET
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(60000); //60 secs
            con.setReadTimeout(60000); //60 secs
            con.setRequestProperty("Authorization", token);

        } catch (Exception e) {
            System.out.println("connection i/o failed");
        }
        return con;
    }


}
