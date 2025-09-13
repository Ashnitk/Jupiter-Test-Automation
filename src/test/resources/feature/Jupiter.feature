Feature: Automate testing of Jupiter Webpage

  Scenario: Validate Jupiter website contact page mandatory fields validation
    Given I access the Jupiter website the "First" time
    When I navigate to the contact page
    And I click submit button
    Then Verify error messages are present
    When I populate the mandatory fields
    Then Validate the errors have disappeared

  Scenario Outline:
    Given I access the Jupiter website the "<NumberOfAttempts>" time
    When I navigate to the contact page
    And I populate the mandatory fields
    Then Validate the errors have disappeared

    Examples:
      | NumberOfAttempts |
      | First              |
      | Second             |
      | Third              |
      | Forth              |
      | Fifth              |