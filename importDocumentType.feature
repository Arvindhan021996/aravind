Feature: Import Document Type Apis

  @SmokeTest
  Scenario Outline: Verify import document type APIs feature.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
   # Then Verify "totalTime" is same as "totalTime"
   # Then user should get "value" in the response body with "data"
   #Then Verify "nodeName" ends with "nodeName"
   #Then Verify "nodeName" equals contains "nodeName" under JsonArray "details" 
   #Then Verify "fileName" equals contains "fileName" under JsonArray "details" 
  # Then Verify "totalTime" equals contains "totalTime" under JsonArray "" 
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api65 | body66 |