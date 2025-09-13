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

  Scenario: Purchase items from the Jupiter website shopping site
    Given I access the Jupiter website
    When I navigate to the shop page
    And I purchase 2 "Stuffed Frog"
    And I purchase 5 "Fluffy Bunny"
    And I purchase 3 "Valentine Bear"
    And I navigate to the cart page
    Then Validate price and subtotal for each product is correct
    And Validate the total cost