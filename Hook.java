package com.userApi.stepsdefs;

import java.util.ArrayList;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.junit.runner.Result;
import org.openqa.selenium.WebDriver;

public class Hook {

	public static Scenario scenario;
	public static Result result;
	public static ArrayList<String> listScenario = new ArrayList();
	public static WebDriver driver;

	@Before
	public void beforeScenario(Scenario scenario) throws Exception {
		Hook.scenario = scenario;
		System.out.println(scenario.getName());
	}

	@After
	public void afterScenario() throws Exception {
		String str = null;
		if (scenario.isFailed()) {
			str = scenario.getName() + " - Fail";
		} else if (!scenario.isFailed()) {
			str = scenario.getName() + " - Pass";
		}
		listScenario.add(str);
		for (int i = 0; i < listScenario.size(); i++) {
			System.out.println(listScenario.get(i));
		}
	}

}
