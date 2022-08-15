package api.crud;

import api.BaseTest;
import api.Specifications;
import framework.config.Errors;
import models.ErrorModel;
import models.UserDataModel;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class TestUserGet extends BaseTest {

    @BeforeTest  //example how can the request and response specs can be setup
    public void setupSpec(){
        setSpec(Specifications.responseSpec200());
    }
    @Test(groups = {"Smoke"})
    public void checkUsersExist(){
        List<UserDataModel> users = getUsers("/users");
        Assert.assertEquals(true,users.size()>0,"There should be at least one existing user");
    }
    @Test(groups = {"Smoke"})
    public void checkGetUserById(){
        setSpec(Specifications.responseSpec200());
        UserDataModel user = new UserDataModel(4011,"Baladitya Banerjee","baladitya_banerjee@harber-schroeder.info","female","active");
        String url = "/users/"+user.getId();
        UserDataModel requestedUser = getUserById(url);
        Assert.assertEquals(user.getEmail(),requestedUser.getEmail(),"The returned user email does not correspond the requested one");
        Assert.assertEquals(user.getName(),requestedUser.getName(),"The returned user name does not correspond the requested one");
        Assert.assertEquals(user.getGender(),requestedUser.getGender(),"The returned user gender does not correspond the requested one");
    }

    @Test(groups = {"Smoke"})
    public void getNotExistingUser(){
        setSpec(Specifications.resourceNotFound());
        String url = "/users/"+100500;
        String receivedError=getNotExisting(url);
        Assert.assertEquals(receivedError,Errors.NOT_FOUND);

    }



}
