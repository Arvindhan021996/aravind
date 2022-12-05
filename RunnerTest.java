package com.userApi.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = { "classpath:MdmServicesApi/UpdateInprogress.feature" },
		glue = { "com.userApi.stepsdefs" },
		plugin = {"com.userApi.stepsdefs.Initialization",
				  "json:build/cucumber.json"},
		monochrome = true,
		dryRun = false)
public class RunnerTest {
}
