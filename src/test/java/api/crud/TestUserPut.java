package api.crud;

import api.BaseTest;
import api.Specifications;
import framework.config.Errors;
import models.ErrorModel;
import models.ResponseModel;
import models.UserDataModel;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.List;


public class TestUserPut extends BaseTest {
    private UserDataModel testUser = new UserDataModel(null, "Angelina Jolie", "emailthatyouwontget" + (int) (Math.random() * 1000 + 1) + "@mail.net", "female", "active");
    private UserDataModel testUser2 = new UserDataModel(null, "Bradd Pitt", "breddsemail" + (int) (Math.random() * 1000 + 1) + "@mail.net", "male", "active");

    public String email = "newemail" + (int) (Math.random() * 1000 + 1) + "@test.com";


    @BeforeTest
    public void postUser() {
        setSpec(Specifications.responseSpec201());
        testUser = postValidUser("/users", testUser);
    }

    @Test(groups = {"Smoke"}, priority = 1)
    public void editUserEmail() {
        setSpec(Specifications.responseSpec200());
        String url = "/users/" + testUser.getId();
        ResponseModel updateEmailResponse = putUserEmail(url, email);
        if (updateEmailResponse.success != null) {
            UserDataModel updatedUser = updateEmailResponse.success;
            Assert.assertNotEquals(testUser.getEmail(), updatedUser.getEmail(), "Email was not changed");
        } else Assert.fail("Email was not changed");
    }


    @Test(groups = {"Smoke"},priority = 2)
    public void checkEditTakenEmail() {
        setSpec(Specifications.responseSpec201());
        testUser = postValidUser("/users", testUser2);

        setSpec(Specifications.invalidDataError());
        String url = "/users/" + testUser.getId();
        String setEmail = testUser2.getEmail();
        ResponseModel updateEmailResponse = putUserEmail(url, setEmail);
        if (updateEmailResponse.error != null) {
            List<ErrorModel> error = updateEmailResponse.error;
            Assert.assertEquals(error.get(0).getMessage(), Errors.IS_TAKEN);
        } else Assert.fail(" Email was successfully changed with already taken one");

    }
}
