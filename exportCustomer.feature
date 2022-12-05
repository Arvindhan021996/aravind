Feature: Export Customer Services Apis

  @SmokeTest
  Scenario Outline: Verify user with permissions company.view can get all companies via services/apis limit given
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then create timestamp variable with name "mdm-export"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api  | Body   |
      | Api3 | body61 |