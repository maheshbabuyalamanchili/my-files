package tests;

import TestProjectPages.Blazedeompage;
import cucumber.api.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;

public class Blazedemo extends TestBase {
    @Autowired
    Blazedeompage blazedemo;

    @Given("^user logs into blazedemo application$")
    public void user_logs_into_blazedemo_application() throws Throwable {
        blazedemo.login();
    }
    @Then("^user validate the blaze demo page$")
    public void user_validate_the_blaze_demo_page() throws Throwable {
        blazedemo.validate_blazedemopage();
    }
//    @Given("^user validate the blaze demo page$")
//    public void user_validate_the_blaze_demo_page() throws Throwable {
//        blazedemo.validate_blazedemopage();
//    }

    @Then("^user selects depature country:\"([^\"]*)\" and destination country:\"([^\"]*)\" from dropdowns$")
    public void user_selects_depature_country_and_destination_country_from_dropdowns(String arg1, String arg2) throws Throwable {

    }

    //    @And("^user click on find flights$")
//    public void user_click_on_find_flights() throws Throwable {
//        blazedemo.click_on_find_flights();
//    }
    @Then("^user click on find flights$")
    public void user_click_on_find_flights() throws Throwable {
        blazedemo.click_on_find_flights();
    }


}
