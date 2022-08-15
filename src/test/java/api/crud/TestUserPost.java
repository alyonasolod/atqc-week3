package api.crud;
import api.BaseTest;
import api.Specifications;
import framework.config.Errors;
import models.ErrorModel;
import models.UserDataModel;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;

public class TestUserPost extends BaseTest {

    private List<Integer> createdUsersIds = new ArrayList<>();


    @Test (groups = {"Smoke","Regress"},dataProvider = "data-provider-valid-test-users",dataProviderClass = TestDataProvider.class)
    public void addValidUser(String name, String email, String gender, String status){

        setSpec(Specifications.responseSpec201());
        UserDataModel validUser = new UserDataModel(null,name,email,gender,status);

        UserDataModel addedValidUser=postValidUser("/users", validUser);

        Assert.assertEquals(validUser.getName(),addedValidUser.getName());
        createdUsersIds.add(addedValidUser.getId());
    }

    @Test (groups = {"Regress"},dataProvider = "data-provider-invalid-user-email",dataProviderClass = TestDataProvider.class)
    public void checkEmailValidationOnUserCreation(String name, String email, String gender, String status){
        setSpec(Specifications.invalidDataError());
        UserDataModel userInvalidEmail = new UserDataModel(null,name,email,gender,status );
        List<ErrorModel> invalidEmail = postInvalidUser("/users",userInvalidEmail );
        Assert.assertEquals(invalidEmail.get(0).getMessage(), Errors.INVALID);
    }


    @AfterClass  // just an example what can be done after the tests in class passed (or failed)
    public void deleteTestUsers(){
        if(createdUsersIds.size()!=0){
            for(Integer userId: createdUsersIds) {
                String url = baseUrl+"/users/"+userId+"/";
             sendDeleteRequest(url, getAuthToken());
            }
        }
    }
}
