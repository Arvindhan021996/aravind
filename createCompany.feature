Feature: Creating a Company by calling create Company APIs.

  @SmokeTest
  Scenario Outline: Verify create company API feature.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then Verify "name" is same as "name"
   # Then Verify "hq" is same as "hq1"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api81 | body81 |