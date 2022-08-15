package api;

import framework.config.Errors;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ExtractableResponse;
import models.UserDataModel;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;


public class EndToEndTest extends BaseTest {


    private UserDataModel validUser = new UserDataModel(null, "Test Name", "email@test.com", "male", "active");
    private Integer userId;

    //attempt to create new user with expired auth token returns an error
    // attempt to create new user without any token returns an error
    // create user is possible with valid User and valid token
    // user can be updated with only 1 field - status: inactive-  sent to BE
    // check sending not existing gender returns valid error
    // delete User returns 204 status message
    // try to get deleted user returns 404 error

    @BeforeTest
    public void setup() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        setSpec(Specifications.responseSpec200());
        List<UserDataModel> user = given()
                .header("Authorization",getAuthToken())
                .queryParam("email",validUser.getEmail())
                .get("/users")
                .then().log().all()
                .extract().body().jsonPath().getList(rootPath, UserDataModel.class);;

                if (user.size()>0) {
            setSpec(Specifications.responseSpec204());
            String url = user.get(0).getId().toString();
            ExtractableResponse response = deleteUser("users/" + url);
            System.out.println("status " + response.statusCode());

        }
    }

    @Test(groups = {"Regress"}, priority = 0)
    public void checkExpiredToken() {
        setSpec(Specifications.authFailed());
        String error = authWithExpiredToken("/users", validUser);
        Assert.assertEquals(error, Errors.TOKEN_EXPIRED);
    }

    @Test(groups = {"Regress"}, priority = 1)
    public void checkMissingToken() {
        setSpec(Specifications.authFailed());
        String error = authWithoutToken("/users", validUser);
        Assert.assertEquals(error, Errors.AUTH_FAILED);
    }

    @Test(groups = {"Regress"}, priority = 2)
    public void checkValidUserCreation() {
        setSpec(Specifications.responseSpec201());
        UserDataModel addedValidUser = postValidUser("/users", validUser);
        Assert.assertEquals(validUser.getEmail(), addedValidUser.getEmail());
        userId = addedValidUser.getId();
    }

    @Test(groups = {"Regress"}, dependsOnMethods = {"checkValidUserCreation"}, priority = 3)
    public void checkUpdatingUsersActivity() {
        String oldStatus = validUser.getStatus();
        JSONObject requestBody = new JSONObject();
        requestBody.put("status", "inactive");
        setSpec(Specifications.responseSpec201());
        UserDataModel editedUser = given()
                .header("Authorization", getAuthToken())
                .body(requestBody)
                .put("/users/" + userId)
                .then().log().all()
                .extract().as(UserDataModel.class);
        Assert.assertNotEquals(oldStatus, editedUser.getStatus());
    }

    @Test(groups = {"Regress"}, dependsOnMethods = {"checkValidUserCreation"}, priority = 3)
    public void checkFailureOnUpdatingUsersGender() {
        String url = "/users/" + userId;
        JSONObject requestBody = new JSONObject();
        requestBody.put("gender", "");
        setSpec(Specifications.invalidDataError());
        String responseError = putInvalidGender(url, requestBody);
        Assert.assertEquals(responseError, Errors.INCORRECT_GENDER);

    }

    @Test(groups = {"Regress"}, priority = 10)
    public void checkUserDeletion() {
        setSpec(Specifications.responseSpec204());
        String url = "/users/" + userId;
        deleteUser(url);

        setSpec(Specifications.resourceNotFound());
        deleteUser(url);
    }


}
