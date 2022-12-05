Feature: Update Inprogress Version Doctype Service Apis

  @SmokeTest
  Scenario Outline: Verify that user should be able to  Update Inprogress Version Doctype.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request PUT
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api91| body96 |
      | Api91| body97 |
      | Api91| body99 |
      | Api91| body100 |
      
      
      @SmokeTest
  Scenario Outline: Verify that user should not be able to  Update Inprogress Version Doctype.
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Create Body "<Body>" with variable ""
    Then Setup for Api Testing data
    Then Send Request PUT
    Then Verify Status code 400
    Then Save the Response for "<Api>" in Report

    Examples: 
      | Api   | Body   |
      | Api91| body95 |
      | Api91| body98 |
      | Api91| body97 |
      
    
     
      