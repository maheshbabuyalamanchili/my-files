package tests;

import TestProjectPages.Loginpage;
import cucumber.api.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

public class Login {
    @Autowired
    Loginpage login;

    @Given("^the user is on the nopCommerce login page$")
    public void the_user_is_on_the_nopCommerce_login_page() throws Throwable {

    }

    @When("^user enter valid username & password:\"([^\"]*)\",\"([^\"]*)\"$")
    public void user_enter_valid_username_password(String arg1, String arg2) throws Throwable {

    }

    @Then("^user clicks on login button$")
    public void user_clicks_on_login_button() throws Throwable {

    }

    @Then("^user should be redirected to the my account page$")
    public void user_should_be_redirected_to_the_my_account_page() throws Throwable {

    }

    @Then("^user should see a welcome message$")
    public void user_should_see_a_welcome_message() throws Throwable {

    }

  



}
