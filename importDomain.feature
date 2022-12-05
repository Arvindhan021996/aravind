Feature: Import Domain Service Apis

  @SmokeTest
  Scenario Outline: Verify import document type APIs feature.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable "gitLabTag"
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
   # Then Verify "totalTime" is same as "totalTime"
    #Then user should get "value" in the response body with "data"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api84 | body84 |