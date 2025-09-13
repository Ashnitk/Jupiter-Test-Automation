Feature: Automate testing of Jupiter Webpage

  Scenario: Validate Jupiter website contact page mandatory fields validation
    Given I access the Jupiter website
    When I navigate to the contact page
    And I click submit button
    Then Verify error messages are present
    When I populate the mandatory fields for "John"
    Then Validate the errors have disappeared for "John"

  Scenario Outline:
    Given I access the Jupiter website
    When I navigate to the contact page
    And I populate the mandatory fields for "<Person>"
    Then Validate the errors have disappeared for "<Person>"

    Examples:
      | Person |
      | Matt   |
      | Andy   |
      | Amy    |
      | Frank  |
      | Brad   |