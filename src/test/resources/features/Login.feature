Feature: User login

  @Smoke30052024
  Scenario Outline: User login to application succesfully
    Given the user is on the nopCommerce login page
    When user enter valid username & password:"<username>","<password>"
    Then user clicks on login button
    Then user should be redirected to the my account page
    And user should see a welcome message


    Examples:
      | username | password |
      | abc      | 123      |
      | efg      | 456      |
