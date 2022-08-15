package api.crud;

import api.BaseTest;
import api.Specifications;
import models.UserDataModel;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestUserDelete extends BaseTest {
    private UserDataModel testUser = new UserDataModel(null, "Jane Doe", "deletemerightnow@mail.net", "female", "active");

    @BeforeTest
    public void postUser() {
        setSpec(Specifications.responseSpec201());
        testUser = postValidUser("/users", testUser);
    }
    @Test
    public void checkUserDeletion(){
        setSpec(Specifications.responseSpec204());
        String url = "/users/"+testUser.getId();
        deleteUser(url);

        setSpec(Specifications.resourceNotFound());
        deleteUser(url);

    }

}
