package com.userApi.stepsdefs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.PageFactory;

import com.irm.pages.BaseObject;
import com.irm.pages.MonitorPageObj;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApplicationSteps {
	public Platform platform;
	public Scenario scenario = Hook.scenario;
	public static WebDriver driver;
	public String finalApi;
	public String finalBody = "";
	RequestSpecification reSp;
	public Response response_gen;
	public String respBody;
	JSONObject jsonObj;
	ArrayList<String> arrayLi;
	public BaseObject object = new BaseObject(driver);
	public static BaseObject baseobj;
	public static MonitorPageObj monitorpageobj;
	public static ApplicationSteps applicationsteps;
	public static User user;
	public String endpoint;

	private DesiredCapabilities cap = new DesiredCapabilities();
	private ChromeOptions options = new ChromeOptions();

	public static void initilizeEle(WebDriver driver) {
		baseobj = PageFactory.initElements(driver, BaseObject.class);
		monitorpageobj = PageFactory.initElements(driver, MonitorPageObj.class);
		applicationsteps = PageFactory.initElements(driver, ApplicationSteps.class);
		user = PageFactory.initElements(driver, User.class);
	}

	@Given("Check Environment {string}")
	public void check_Environment(String environment) throws Exception {
		try {
			scenario.log("Start");
			endpoint = System.getenv("MDM_API_ENDPOINT");

			scenario.log("API URL SET FOR GIVEN ENVIRONMENT : " + endpoint);
		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Create Api {string} with variable {string}")
	public void create_Api_with_variable(String ApiUrl, String var) throws Exception {
		try {
			if (var.equals("")) {
				String apiurl = User.getValueFromPropertiesPath1("ApiAndBody", ApiUrl);
				apiurl = apiurl.replace("{ENDPOINT}", endpoint);
				finalApi = apiurl;
				scenario.log("Final Api : " + finalApi);
			} else {
				String apiurl = User.getValueFromPropertiesPath1("ApiAndBody", ApiUrl);
				String apivar = User.getValueFromPropertiesPath("GenericProperties", var);
				apiurl = apiurl.replace("variable", apivar);
				apiurl = apiurl.replace("{ENDPOINT}", endpoint);
				finalApi = apiurl;
				scenario.log("Final Api : " + finalApi);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Given("Setup for Api Testing data")
	public void setup_for_Api_Testing_data() throws Exception {
		try {
			reSp = RestAssured.with();
			reSp.given().contentType("application/json");
			//reSp.given().auth().basic(System.getenv("MDM_API_USER"), System.getenv("MDM_API_PASSWORD"));
			scenario.log(reSp.toString());
			scenario.log("SetUp for APi Testing is successfully Done");

		} catch (Exception e) {
			throw e;
		}
	}
	@Given("Setup for Api Testing data xml")
	public void setup_for_Api_Testing_data_xml() throws Exception {
		try {
			reSp = RestAssured.with();
			reSp.given().contentType("application/xml");
			//reSp.given().auth().basic(System.getenv("MDM_API_USER"), System.getenv("MDM_API_PASSWORD"));
			scenario.log(reSp.toString());
			scenario.log("SetUp for APi Testing is successfully Done");

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Send Request GET")
	public void send_Request_GET() throws Exception {
		try {
			scenario.log("Inside Get");
			response_gen = reSp.get(finalApi).then().extract().response();
			scenario.log(response_gen.toString());
			respBody = response_gen.asString();
			if (respBody.contains("Unauthorized")) {
				launch_Browser_and_extract_token();
				setup_for_Api_Testing_data();
				response_gen = reSp.get(finalApi).then().extract().response();
				scenario.log(response_gen.toString());
				respBody = response_gen.asString();
			}
			scenario.log("Response Body :" + respBody);
			scenario.log("GET Request is successfully executed");
		} catch (Exception e) {
			scenario.log("GET Request is not successfully executed");
			throw e;
		}
	}

	@Then("Verify Status code {int}")
	public void verify_Status_code(Integer statusCode) {
		try {
			int respStatusCode = response_gen.getStatusCode();
			Assert.assertTrue("Expected Status Code is " + statusCode + " but getting " + respStatusCode,
					respStatusCode == statusCode);
			scenario.log("Status code is as Expected : " + statusCode);
			scenario.log("Status code is as Expected : " + statusCode);
		} catch (Exception e) {
			scenario.log(e.getMessage());
			throw e;
		}

	}

	@Then("Verify the limit of list of companies is {string}")
	public void verify_the_limit_of_list_of_companies_is(String limit) throws Exception {
		try {
			String varLim = User.getValueFromPropertiesPath("GenericProperties", limit);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("details");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get("nodeName").toString();
				arrayLi.add(str);
			}
			scenario.log(arrayLi.toString());
		} catch (Exception e) {
			scenario.log("Limit size of company is not " + limit + " Defect");
			throw e;
		}
	}

	@Then("Save the Response for {string} in Report")
	public void save_the_Response_for_in_Report(String ApiName) throws Exception {
		try {
			if (finalBody.equals("")) {
				finalBody = "Body Not Required";
			}
			scenario.log("Request : " + finalApi + "\nBody : " + finalBody + " \nResponse : " + respBody);
		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Verify {string} is sorted as Descending order")
	public void verify_is_sorted_as_Descending_order(String key) throws Exception {
		try {
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				arrayLi.add(str);
			}
			for (i = 0; i < arrayLi.size() - 1; i++) {
				String strDat1 = arrayLi.get(i);
				String strDat2 = arrayLi.get(i + 1);
				strDat1 = strDat1.split("T")[0];
				strDat2 = strDat2.split("T")[0];
				Date date1 = new SimpleDateFormat("yy-MM-dd").parse(strDat1);
				Date date2 = new SimpleDateFormat("yy-MM-dd").parse(strDat2);
				scenario.log(date1 + " & " + date2);
				Assert.assertTrue(date1.after(date2) || date1.equals(date2));
			}
			scenario.log("Array List " + arrayLi);
			scenario.log(key + " is sorted in desc order verified ");
		} catch (Exception e) {
			scenario.log(key + " is not sorted in desc order Defect ");
			throw e;

		}
	}

	@Then("Create Api {string} with variable {string} variable {string} and variable {string}")
	public void create_Api_with_variable_variable_and_variable(String ApiUrl, String var1, String var2, String var3)
			throws Exception {
		try {
			String apiurl = User.getValueFromPropertiesPath1("ApiAndBody", ApiUrl);
			String apivar1 = User.getValueFromPropertiesPath("GenericProperties", var1);
			String apivar2 = User.getValueFromPropertiesPath("GenericProperties", var2);
			String apivar3 = User.getValueFromPropertiesPath("GenericProperties", var3);
			apiurl = apiurl.replace("variable1", apivar1).replace("variable2", apivar2).replace("variable3", apivar3);
			finalApi = apiurl;
			scenario.log("Final Api : " + finalApi);
			scenario.log("Api Created Successfully and embedded in Report");
		} catch (Exception e) {
			scenario.log("Api not  Created Successfully");
			throw e;
		}
	}

	@Then("Verify {string} starts with {string}")
	public void verify_starts_with(String key, String value) throws Exception {
		try {
			key = User.getValueFromPropertiesPath("GenericProperties", key);
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			scenario.log(key);
			scenario.log(value);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				scenario.log("str:" + str);
				arrayLi.add(str);
				Assert.assertTrue(str.startsWith(value.toLowerCase()) || str.startsWith(value.toUpperCase())
						|| (str.startsWith(value)));
			}
			scenario.log(key + " has value which starts with" + value + " verified");

		} catch (Exception e) {
			scenario.log(key + " has value which do not start with" + value + " Defect");
			throw e;
		}
	}

	@Then("Verify {string} greater than {string}")
	public void verify_greater_than(String key, String value) throws Exception {
		try {
			key = User.getValueFromPropertiesPath("GenericProperties", key);
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			int value1 = Integer.parseInt(value);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				arrayLi.add(str);
			}
			Assert.assertTrue(arrayLi.size() > 0);
			for (int j = 0; j < arrayLi.size(); j++) {
				int intStr = Integer.parseInt(arrayLi.get(j));
				Assert.assertTrue(intStr == value1 || intStr > value1);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Verify {string} in response is {string}")
	public void verify_total_company_in_response_is(String key, String value) throws Exception {
		try {
			jsonObj = new JSONObject(respBody);
			scenario.log(jsonObj.get("total").toString());
			String str1 = jsonObj.get("total").toString();
			value = str1;
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				arrayLi.add(str);
			}
			Assert.assertTrue(arrayLi.size() == Integer.parseInt(value));
			scenario.log(key + " has a value is equal to " + value);
		} catch (Exception e) {
			scenario.log(key + " has a value not equal to " + value + " Defect");
			throw e;
		}
	}

	@Then("Create Api {string} with variable {string} and variable {string}")
	public void create_Api_with_variable_and_variable(String ApiUrl, String var1, String var2) throws IOException {
		try {
			String apiurl = User.getValueFromPropertiesPath1("ApiAndBody", ApiUrl);
			String apivar1 = User.getValueFromPropertiesPath("GenericProperties", var1);
			String apivar2 = User.getValueFromPropertiesPath("GenericProperties", var2);
			apiurl = apiurl.replace("variable1", apivar1).replace("variable2", apivar2);
			finalApi = apiurl;
			scenario.log("Final Api : " + finalApi);
		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Verify {string} ends with {string}")
	public void verify_ends_with(String key, String value) throws Exception {
		try {
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("details");
			Assert.assertTrue("No Data Found", tsmResponse.length() > 0);
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				scenario.log(str);
				arrayLi.add(str);
				System.out.println(value);
				Assert.assertTrue(arrayLi.contains(value));
			}
			scenario.log(key + " has value which ends with" + value + " verified");
		} catch (Exception e) {
			scenario.log(key + " has value which do not ends with" + value + " Defect");
			throw e;
		}
	}

	@Then("Verify {string} equals contains {string} under JsonArray {string}")
	public void verify_equals_contains_under_JsonArray(String key, String value, String reqJson) throws Exception {
		try {
			if (reqJson.equals("")) {

				jsonObj = new JSONObject(respBody);
				scenario.log(jsonObj.get(key).toString());
				String str = jsonObj.get(key).toString();
				Assert.assertTrue(str.equals(value));

			} else {

				value = User.getValueFromPropertiesPath("GenericProperties", value);
				jsonObj = new JSONObject(respBody);
				arrayLi = new ArrayList<>();
				JSONArray tsmResponse = (JSONArray) jsonObj.get(reqJson);
				int i;
				for (i = 0; i < tsmResponse.length(); i++) {
					String str = tsmResponse.getJSONObject(i).get(key).toString();
					scenario.log(str);
					arrayLi.add(str);
					//Assert.assertTrue(arrayLi.contains(value));
				}
			}
			scenario.log(key + " has value which ends with" + value + " verified");

		} catch (Exception e) {
			scenario.log(key + " has value which do not ends with" + value + " Defect");
			throw e;
		}
	}

	@Then("Verify {string} should be less than {string}")
	public void verify_should_be_less_than(String key, String value) throws Exception {
		try {
			key = User.getValueFromPropertiesPath("GenericProperties", key);
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				arrayLi.add(str);
			}
			for (i = 0; i < arrayLi.size(); i++) {
				String strDat1 = arrayLi.get(i);
				String strDat2 = value;
				strDat1 = strDat1.split("T")[0];
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(strDat1);
				Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(strDat2);
				scenario.log(date1 + " & " + date2);
				Assert.assertTrue(date1.before(date2) || date1.equals(date2));
			}
			scenario.log("Array List " + arrayLi);
			scenario.log(key + " has value which is less than " + value + " verified");
		} catch (Exception e) {
			scenario.log(key + " has value which is not less than " + value + " Defect");
			throw e;
		}
	}

	@Then("Verify {string} is {string}")
	public void verify_is(String key, String value) throws Exception {
		try {
			key = User.getValueFromPropertiesPath("GenericProperties", key);
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			Assert.assertTrue("No Data Found", tsmResponse.length() > 0);
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				scenario.log(key + " : " + str);
				Assert.assertTrue(key + " has value which is not " + value + "- Defect", str.equals(value));
			}
			scenario.log(key + " has value which is " + value + " verified");
		} catch (Exception e) {
			scenario.log(key + " has value which is not " + value + " Defect");
			throw e;
		}
	}

	@Then("Verify the given company name {string}")
	public void verify_the_given_company_name(String companyName) throws Exception {
		try {
			companyName = User.getValueFromPropertiesPath("GenericProperties", companyName);
			jsonObj = new JSONObject(respBody);
			String str = jsonObj.get("branchName").toString();
			scenario.log("company ID : " + str);
			Assert.assertTrue(str.equals(companyName));
			scenario.log("companyId is " + companyName + " verified");
		} catch (Exception e) {
			scenario.log("companyId is not " + companyName + " Defect");
			throw e;
		}
	}

	@Then("Create Body {string} with variable {string}")
	public void create_Body_with_variable(String body, String var) throws Exception {
		try {
			if (var.equals("")) {
				String bodyData = User.getValueFromPropertiesPath1("ApiAndBody", body);
				finalBody = bodyData;
				scenario.log("Final body : " + finalBody);
			} else {
				String bodyData = User.getValueFromPropertiesPath1("ApiAndBody", body);
				String bodyvar = User.getValueFromPropertiesPath("GenericProperties", var);
				bodyData = bodyData.replace("variable", bodyvar);
				finalBody = bodyData;
				System.out.println(finalBody);
				scenario.log("Final body : " + finalBody);
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Send Request POST")
	public void send_Request_POST() throws Exception {
		try {
			String PostPram = finalBody;
			response_gen = reSp.given().body(PostPram).post(finalApi).then().extract().response();
			respBody = response_gen.asString();
			if (respBody.contains("Unauthorized")) {
				launch_Browser_and_extract_token();
				setup_for_Api_Testing_data();
				response_gen = reSp.given().body(PostPram).post(finalApi).then().extract().response();
				respBody = response_gen.asString();
			}
			scenario.log("Response Body :" + respBody);
			scenario.log("POST Request is successfully executed");
		} catch (Exception e) {
			scenario.log("POST Request is not successfully executed");
			throw e;
		}
	}

	@Then("Send Request PUT")
	public void send_Request_PUT() throws Exception {
		try {
			String PostPram = finalBody;
			scenario.log("Post Body :" + PostPram);
			response_gen = reSp.given().body(finalBody).put(finalApi).then().extract().response();
			respBody = response_gen.asString();
			if (respBody.contains("Unauthorized")) {
				launch_Browser_and_extract_token();
				setup_for_Api_Testing_data();
				response_gen = reSp.given().body(PostPram).put(finalApi).then().extract().response();
				respBody = response_gen.asString();
			}
			scenario.log("Response Body :" + respBody);
			scenario.log("PUT Request is successfully executed");
		} catch (Exception e) {
			scenario.log("PUT Request is not successfully executed");
			throw e;
		}
	}

	@Then("Verify {string} value is {string}")
	public void verify_value_is(String keyName, String value) throws Exception {
		try {
			String val = value;
			jsonObj = new JSONObject(respBody);
			scenario.log(jsonObj.get(keyName).toString());
			String str = jsonObj.get(keyName).toString();
			Assert.assertTrue(str.equals(val));
			scenario.log(keyName + " has value equal to " + val + "  Verified");
		} catch (Exception e) {
			scenario.log(keyName + " has value not equal to " + value + "  Defect");
			throw e;
		}
	}

	@Then("Verify {string} value is equal to list of {string}")
	public void verify_value_is_equal_to_list_of(String val1, String val2) {
		jsonObj = new JSONObject(respBody);
		scenario.log(jsonObj.get(val1).toString());
		String str = jsonObj.get(val1).toString();
		int total = Integer.parseInt(str);
		arrayLi = new ArrayList<>();
		JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
		int i;
		for (i = 0; i < tsmResponse.length(); i++) {
			String str1 = tsmResponse.getJSONObject(i).get(val2).toString();
			jsonObj = new JSONObject(str1);
			arrayLi.add(str1);
		}
		scenario.log("Compare " + arrayLi.size() + " and " + total);
		Assert.assertTrue(arrayLi.size() == total);
		scenario.log("List of user is equal to " + total + " - Verified");
	}

	@Then("Verify total user is equal to list of {string}")
	public void verify_total_user_is_equal_to_list_of(String keyName) {
		try {
			jsonObj = new JSONObject(respBody);
			scenario.log(jsonObj.get("total").toString());
			String str = jsonObj.get("total").toString();
			int total = Integer.parseInt(str);
			arrayLi = new ArrayList<>();
			JSONArray tsmResponse = (JSONArray) jsonObj.get("data");
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str1 = tsmResponse.getJSONObject(i).get(keyName).toString();
				arrayLi.add(str1);
			}
			Assert.assertTrue(arrayLi.size() == total);
			scenario.log("List of user is equal to " + total + " - Verified");
		} catch (Exception e) {
			scenario.log("List of user is not equal to  - defect");
			throw e;
		}
	}

	@Then("Verify {string} is sorted as Descending order{string}")
	public void verify_is_sorted_as_Descending_order(String key, String listName) throws Exception {
		try {
			jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
			JSONArray arr = jsonObj.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				scenario.log(arr.get(i).toString());
				String operate = arr.get(i).toString();
				String operate1[] = operate.split(",");
				for (int j = 0; j <= operate.length(); j++) {
					String newStr = operate1[j];
					scenario.log("For " + i + " " + newStr);
					if (newStr.contains(key)) {
						newStr = newStr.split(":")[1];
						newStr = newStr.replaceAll("^\"|\"$", "");
						arrayLi.add(newStr);
						break;
					}

				}
			}
			scenario.log(arrayLi.toString());
			Assert.assertTrue(arrayLi.size() > 0);
			for (int i = 0; i < arrayLi.size() - 1; i++) {
				String strDat1 = arrayLi.get(i);
				String strDat2 = arrayLi.get(i + 1);
				strDat1 = strDat1.split("T")[0];
				strDat2 = strDat2.split("T")[0];
				Date date1 = new SimpleDateFormat("yy-MM-dd").parse(strDat1);
				Date date2 = new SimpleDateFormat("yy-MM-dd").parse(strDat2);
				scenario.log(date1 + " & " + date2);
				Assert.assertTrue(date1.after(date2) || date1.equals(date2));
			}
			scenario.log(key + " is sorted in descending order- Verified");
		} catch (Exception e) {
			scenario.log(key + " is not sorted in descending order- Defect");
			throw e;
		}
	}

	@Then("Verify {string} is same as {string}")
	public void verify_is_same_as(String key, String value) throws Exception {
		try {
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			scenario.log(value);
			jsonObj = new JSONObject(respBody);
			scenario.log(jsonObj.get(key).toString());
			String str = jsonObj.get(key).toString();
			Assert.assertTrue(str.equals(value));

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Send Request DELETE")
	public void send_Request_DELETE() {
		try {
			response_gen = reSp.given().delete(finalApi).then().extract().response();
			respBody = response_gen.asString();
			scenario.log("Response Body :" + respBody);

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Send Request DELETE with body")
	public void send_Request_DELETE_with_body() {
		try {
			response_gen = reSp.given().body(finalBody).delete(finalApi).then().extract().response();
			respBody = response_gen.asString();
			scenario.log("Response Body :" + respBody);

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("Verify {string} is found {string}")
	public void verify_is_found(String key, String value) throws Exception {
		try {
			value = User.getValueFromPropertiesPath("GenericProperties", value);
			arrayLi = new ArrayList<>();
		    JSONArray tsmResponse =new JSONArray(respBody); 
			Assert.assertTrue("No Data Found", tsmResponse.length() > 0);
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				scenario.log(key + " : " + str);
				Assert.assertTrue(key + " has value which is not " + value + "- Defect", str.equals(value));
			}
			scenario.log(key + " has value which is " + value + " verified");
		} catch (Exception e) {
			scenario.log(key + " has value which is not " + value + " Defect");
			throw e;
		}
	}
	
	@Then("Verify {string} is found as {string}")
	public void verify_is_found_as(String key, String value) throws Exception {
		try {
			value = User.getValueFromPropertiesPath("GenericProperties", value);
            jsonObj = new JSONObject(respBody);
			arrayLi = new ArrayList<>();
		    JSONArray tsmResponse = (JSONArray) jsonObj.get("details");
			Assert.assertTrue("No Data Found", tsmResponse.length() > 0);
			int i;
			for (i = 0; i < tsmResponse.length(); i++) {
				String str = tsmResponse.getJSONObject(i).get(key).toString();
				scenario.log(key + " : " + str);
				Assert.assertTrue(key + " has value which is not " + value + "- Defect", str.equals(value));
			}
			scenario.log(key + " has value which is " + value + " verified");
		} catch (Exception e) {
			scenario.log(key + " has value which is not " + value + " Defect");
			throw e;
		}
	}

	@Then("Create Body {string} with variable {string} variable {string} and variable {string}")
	public void create_Body_with_variable_variable_and_variable(String bodyUrl, String var1, String var2, String var3)
			throws IOException {
		String bodyData = User.getValueFromPropertiesPath1("ApiAndBody", bodyUrl);
		String bodyvar1 = User.getValueFromPropertiesPath("GenericProperties", var1);
		String bodyvar2 = User.getValueFromPropertiesPath("GenericProperties", var2);
		String bodyvar3 = User.getValueFromPropertiesPath("GenericProperties", var3);
		bodyData = bodyData.replace("variable1", bodyvar1).replace("variable2", bodyvar2).replace("variable3",
				bodyvar3);
		finalBody = bodyData;
		scenario.log("Final Body : " + finalBody);
	}

	@Then("Create Body {string} with variable {string} variable {string}")
	public void create_Body_with_variable_variable(String bodyUrl, String var1, String var2) throws IOException {
		String bodyData = User.getValueFromPropertiesPath1("ApiAndBody", bodyUrl);
		String bodyvar1 = User.getValueFromPropertiesPath("GenericProperties", var1);
		String bodyvar2 = User.getValueFromPropertiesPath("GenericProperties", var2);
		bodyData = bodyData.replace("variable1", bodyvar1).replace("variable2", bodyvar2);
		finalBody = bodyData;
		scenario.log("Final Body : " + finalBody);

	}

	@Then("Verify the {string} is same as stored in file {string}")
	public void verify_the_is_same_as_stored_in_file(String key, String var) throws Exception {
		try {
			jsonObj = new JSONObject(respBody);
			String str = jsonObj.get("cspDocument").toString();
			scenario.log("cspDocument :" + str);
			JSONObject js = new JSONObject(str);
			String str1 = js.get(key).toString();
			scenario.log("uid " + str1);
			String newVar = User.getValueFromPropertiesPath("GenericProperties", key);
			Assert.assertTrue(str1.equals(newVar));

		} catch (Exception e) {
			throw e;
		}
	}

	public void launchSauceLab() throws Exception {
		try {
			File folder = new File(System.getProperty("user.dir") + "//DownloadedFiles");
			scenario.log("Downloads file path = " + folder.getAbsolutePath());
			String downloadFilePath = folder.getAbsolutePath();
			File listOfFiles[] = folder.listFiles();
			try {
				if (!folder.exists()) {
					if (folder.mkdirs()) {
						scenario.log("Directory is created!");
					} else {
						scenario.log("Failed to create directory!");
					}
				} else {
					scenario.log("Directory already exists");
					if (listOfFiles.length == 0) {
						scenario.log("no files inside");
					} else {
						scenario.log("Currently no of files present in Downloads folder = " + listOfFiles.length);
						for (File file : folder.listFiles()) {
							file.delete();
							Thread.sleep(10000);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Map<String, Object> perfs = new HashMap<String, Object>();
			perfs.put("profile.default_content_setting_popups", 0);
			perfs.put("download.default_directory", downloadFilePath);
			perfs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-notifications");
			options.setExperimentalOption("prefs", perfs);
			scenario.log("Start...");
			String sauceURL = "http://InsightAutoUser:8c24f278-461b-4f9f-91d9-78af502ba7a6@ondemand.saucelabs.com:4444/wd/hub";
			System.setProperty("browser", "chrome");
			System.setProperty("browser-version", "89.0");
			System.setProperty("SAUCE_USERNAME", "InsightAutoUser");
			System.setProperty("SAUCE_ACCESS_KEY", "8c24f278-461b-4f9f-91d9-78af502ba7a6");
			System.setProperty("SAUCE_MAX_DURATION", "10800");
			System.setProperty("SAUCE_IDLE_TIMEOUT", "3000");
			System.setProperty("SAUCE_COMMAND_TIMEOUT", "600");
			platform = Platform.WIN10;
			cap.setCapability("username", "InsightAutoUser");
			cap.setCapability("accessKey", "8c24f278-461b-4f9f-91d9-78af502ba7a6");
			cap.setCapability("max-duration", 1000);
			cap.setCapability("idle-timeout", 600);
			cap.setCapability("command-timeout", 600);
			cap.setBrowserName(BrowserType.CHROME);
			cap.setVersion("89.0");
			cap.setPlatform(platform);
			driver = new RemoteWebDriver(new URL(sauceURL), cap);
			Thread.sleep(10000);
			scenario.log("End....");
			initilizeEle(driver);
			Thread.sleep(10000);
		} catch (Exception e) {
			throw e;
		}
	}

	public void localExecution() {
		try {
			System.setProperty("webdriver.chrome.driver", ".\\Drivers\\chromedriver.exe");
			options.setHeadless(true);
			driver = new ChromeDriver(options);
			initilizeEle(driver);
		} catch (Exception e) {
			throw e;
		}
	}

	@Given("Launch Browser and extract token")
	public void launch_Browser_and_extract_token() throws Exception {
		try {
			if (User.getValueFromPropertiesPath1("environment", "PlatFormRun").equalsIgnoreCase("local")) {
				localExecution();// Run in local
			} else {
				launchSauceLab(); // Run is Sauce Lab [Currently not possible through local][GitLab Ok]
			}
			String url = User.getValueFromPropertiesPath("GenericProperties", "MonitorUIURL");
			String id = User.getValueFromPropertiesPath("GenericProperties", "MonitorUIId");
			String pass = User.getValueFromPropertiesPath("GenericProperties", "MonitorUIPass");
			driver.get(url);
			driver.manage().window().maximize();
			monitorpageobj.userId.sendKeys(id);
			monitorpageobj.password.sendKeys(decode(pass));
			monitorpageobj.submit.click();
			monitorpageobj.systemTab.click();
			monitorpageobj.misTab.click();
			int i = 0;
			for (i = 0; i < 24; i++) {
				monitorpageobj.upButton.click();
				Thread.sleep(200);
			}
			monitorpageobj.getToken.click();
			Thread.sleep(2000);
			String tokenData = monitorpageobj.token.getText().trim();
			// User.updateInPropertiesPath("GenericProperties", "Token", tokenData);
			driver.close();
			driver.quit();
		} catch (Exception e) {
			throw e;
		}
	}

	public String decode(String encodedString) throws UnsupportedEncodingException {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			String decodedString = new String(decodedBytes);
			return decodedString;
		} catch (Exception e) {
			throw e;
		}
	}

	@Given("Test")
	public void test() throws UnsupportedEncodingException {
		String originalInput = "";
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		scenario.log("encodedString : " + encodedString);
		scenario.log("decodedString : " + decode(encodedString));

	}

	@Then("create timestamp variable with name {string}")
	public void create_timestamp_variable_with_name(String varName) throws Exception {
		try {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			String strDate = formatter.format(date);
			scenario.log(strDate);
			String str2 = varName + "-" + strDate;
			scenario.log(str2);

		} catch (Exception e) {
			throw e;
		}
	}

	@Then("user should get {string} in the response body with {string}")
	public void user_should_get_in_the_response_body_with(String string, String Data) {
		List<String> jsonResponse = response_gen.jsonPath().getList("$");
		jsonObj = new JSONObject(respBody);
		// JSONArray leagues = jsonObj.getJSONArray("jsonObj");
		scenario.log(jsonObj.toString());
		scenario.log(jsonResponse.toString());
		// scenario.log(leagues);

	}
}
