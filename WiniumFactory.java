package com.bank.webdriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import com.bank.util.PropertiesLoader;

public class WiniumFactory {
	protected static WiniumDriver winiumDriver = null;
	private static DesktopOptions options = null;
	private static WiniumDriverService service = null;

	public WiniumFactory(WiniumDriver remoteWiniumDriver) {
		this.winiumDriver = remoteWiniumDriver;
	}

	public void initialize(Object o) {
		PageFactory.initElements(winiumDriver, o);
	}

	public synchronized static WiniumDriver getDriver() throws IOException {

		//this needs to be moved to CucumberTestNG Hooks later
		PropertiesLoader.loadBaseProperties();
		if(winiumDriver==null){
		options = new DesktopOptions();
		if (System.getProperty("host.type").equalsIgnoreCase("local")) {
			options.setApplicationPath(System.getProperty("local.appLocation"));
			System.out.println(System.getProperty("local.appLocation"));
			File driverPath = new File(System.getProperty("winium.desktop.driver.location"));
			System.out.println(driverPath.getAbsolutePath());
			service = new WiniumDriverService.Builder().usingDriverExecutable(driverPath).usingPort(9999)
					.withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			if (winiumDriver == null) {
				System.out.println("starting local driver");
				winiumDriver = new WiniumDriver(service, options);
				System.out.println("started local driver");
			}

		} else {

			URL remoteURL = new URL(System.getProperty("remote.hub.hostaddress"));
			options.setApplicationPath(System.getProperty("remote.appLocation"));
			System.out.println("7");
			if (winiumDriver == null) {
				winiumDriver = new WiniumDriver(remoteURL, options);

			}
		}
		}
		return winiumDriver;
	}

	public static void closeWiniumSession() {
		winiumDriver.quit();
	}

}
