Feature:  Document Export Services Apis

  @SmokeTest
  Scenario Outline: Verify user with permissions company.view can get all companies via services/apis limit given
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request POST
    Then Verify Status code 200
    Then Verify the limit of list of companies is "nodeName"
    Then create timestamp variable with name "mdm-export"
    # Then Verify the given company name "branchName"
    # Then Verify "totalTime" is same as "branchName"
    Then Verify "nodeType" is found as "nodeType"
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api  | Body   |
      | Api2 | body60 |
