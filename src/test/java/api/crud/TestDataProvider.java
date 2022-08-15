package api.crud;


import org.testng.annotations.DataProvider;

public class TestDataProvider {
    @DataProvider(name = "data-provider-valid-test-users")
    public  Object[][] addValidUsers() {
        int tempNum1=(int)(Math.random()*5000+1);
        int tempNum2=(int)(Math.random()*5000+1);
        int tempNum3=(int)(Math.random()*5000+1);
        return new Object[][]{
                {"Jamshut QAevich","dummymail"+tempNum1+"@test.com","male","inactive"},
                {"Oleg Yasdelal","dummymail"+tempNum2+"@test.com","female","active"},
                {"John Doe","dummymail"+tempNum3+"@test.com","male","active"}
        };
    }

    @DataProvider(name = "data-provider-invalid-user-email")
    public  Object[][] UsersWithInvalidEmail() {
        return new Object[][]{

                {"Oleg Yasdelal", "dummymail", "female", "active"},
                {"John Doe",  "@test.com", "male", "active"}
        };
    }



}
