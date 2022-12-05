Feature: Create Inprogress Version Doctype Service Apis

  @SmokeTest
  Scenario Outline: Verify that user should be able to  Create Inprogress Version Doctype.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api90| body85 |
      | Api90| body90 |
      | Api90| body91 |
      | Api90| body92 |
      | Api90| body93 |
      | Api90| body94 |
      