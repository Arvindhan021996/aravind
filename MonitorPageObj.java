package com.irm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.*;

public class MonitorPageObj {

	@FindBy(how = How.XPATH,using="//input[@name='username']")
	public WebElement userId;
	
	@FindBy(how = How.XPATH,using="//input[@name='password']")
	public WebElement password;
	
	@FindBy(how = How.XPATH,using="//input[@name='submit']")
	public WebElement submit;
	
	@FindBy(how = How.XPATH,using="//a[text()='System']")
	public WebElement systemTab;
	
	@FindBy(how = How.XPATH,using="//a[text()='Miscellaneous']")
	public WebElement misTab;
	
	@FindBy(how = How.XPATH,using="//strong[contains(text(),'Hours')]/../following-sibling::span/a[1]")
	public WebElement upButton;
	
	@FindBy(how = How.XPATH,using="//span[text()='Get CDV token']")
	public WebElement getToken;
	
	@FindBy(how = How.XPATH,using="//p[@id='miscJWTTokenText']")
	public WebElement token;
	
	
}
