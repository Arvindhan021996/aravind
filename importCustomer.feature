Feature: Import Customer Type Apis

  @SmokeTest
  Scenario Outline: Verify import document type APIs feature.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable "gitLabTag"
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    #Then Verify the limit of list of companies is "fileName"
   # Then Verify "totalTime" is same as "totalTime"
   #Then Verify "nodeName" ends with "nodeName"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api66 | body78 |