Feature: Nested Elements Page Factory Support Test

  Scenario: Scenario-1
    Given I am on the page "https://www.ryanair.com/tr/en"
    When I select the origin "JFK"

  Scenario: Scenario-2
    Given I am on the page "https://www.ryanair.com/tr/en"
    When I select the destination "ADB"

  Scenario: Scenario-3
    Given I am on the page "https://www.ryanair.com/tr/en"
    When I select the origin "IST"

  Scenario: Scenario-4
    Given I am on the page "https://www.ryanair.com/tr/en"
    When I select the destination "SFO"