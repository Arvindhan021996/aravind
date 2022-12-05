Feature: Find Zip Code 

  @SmokeTest
  Scenario Outline: Verify that all created company should display by calling GetAll Company APIs
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