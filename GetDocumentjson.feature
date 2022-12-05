Feature: Get Schema 

  @SmokeTest
  Scenario Outline: Verify that all Get Schema should return the Inprogress version of the Document Types
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Setup for Api Testing data
    Then Send Request GET
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api    |
      | Api88 |