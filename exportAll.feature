Feature: Export All Services Apis

  @SmokeTest
  Scenario Outline: Verify that user should be able to export All .
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api85 | body85 |