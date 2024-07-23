Feature: ABC
@Smoke123
  Scenario Outline: Booking a flight ticket
    Given user logs into blazedemo application
    Then user validate the blaze demo page
#    Then user selects depature country:"<country>" and destination country:"<country1>" from dropdowns
    Then user click on find flights
    Examples:
      | country | country1 |
      | Paris   | New York |


