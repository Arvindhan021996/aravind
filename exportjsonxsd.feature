Feature: Verify MDM-Schema Validate Export json/xsd api - Add "key" field  feature

  @SmokeTest
  Scenario Outline: Verify Export json/xsd api - Add "key" field  feature APIs
    Given Check Environment "Env"
    Then Create Api "<Api>" with variable "Limit"
    Then Setup for Api Testing data xml
    Then Send Request GET
    Then Verify Status code 200
    Then Save the Response for "<Api>" in Report
     
  
    Examples: 
      | Api   |
      | Api86 |