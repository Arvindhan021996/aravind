Feature: Fuzzysearch 

  @SmokeTest
  Scenario Outline:  verify all the detailsby giving relevant or misspelled keyword when fuzzy search is True and False
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Setup for Api Testing data
    Then Send Request GET
    Then Verify Status code 200
     Then Verify "name" is found "name"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api    |
      | Api80  |