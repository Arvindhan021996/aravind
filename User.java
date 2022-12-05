package com.userApi.stepsdefs;

import java.io.IOException;
import java.util.Properties;
import org.junit.platform.commons.util.ClassLoaderUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class User {

	public static String checkVal(String Key) throws IOException {
		Properties p = new Properties();
		p.load(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream("PropertiesFiles/environment.properties"));
		String str = p.getProperty(Key);
		return str;
	}
	
	public static String getValueFromPropertiesPath1(String path,String Key) throws IOException {
		Properties p = new Properties();
		p.load(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream("PropertiesFiles/"+path+".properties"));
		String str = p.getProperty(Key);
		return str;
	}

	public static String getValueFromPropertiesPath(String path, String Key) throws IOException {
		try {
			if (checkVal("Run").contains("dit")) {
				Properties p = new Properties();
				p.load(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream("MiscData/GenericProperties_dit01.properties"));
				String str = p.getProperty(Key);
				return str;
			} else {
				Properties p = new Properties();
				p.load(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream("MiscData/GenericProperties_stg02.properties"));
				String str = p.getProperty(Key);
				return str;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void sync(WebDriver driver, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			throw e;
		}
	}

	public static void takescreenshot(WebDriver driver) {

	}
}
