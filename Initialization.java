package com.userApi.stepsdefs;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;

public class Initialization implements EventListener {

	EventHandler<TestRunFinished> teardown = event -> {

		try {
			if (User.getValueFromPropertiesPath1("environment", "DryRun").equalsIgnoreCase("False")) {
						//	User.updateInPropertiesPath("GenericProperties", "Token", "Deleted Due To Security");
			} else {
				System.out.println("Dry Run is in progress");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	};

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestRunFinished.class, teardown);
	}

}
