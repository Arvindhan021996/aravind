Feature: Export Domain Service APIS

  @SmokeTest
  Scenario Outline: Verify user get response data based upon domain name
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api  | Body   |
      | Api4 | body64 |