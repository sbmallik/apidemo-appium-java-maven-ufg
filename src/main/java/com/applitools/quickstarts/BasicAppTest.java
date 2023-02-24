package com.applitools.quickstarts;

import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.visualgrid.model.AndroidDeviceInfo;
import com.applitools.eyes.visualgrid.model.AndroidDeviceName;
import com.applitools.eyes.visualgrid.model.DeviceAndroidVersion;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BasicAppTest {
    AppiumDriver driver;
    VisualGridRunner runner;
    Eyes eyes;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "12");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("deviceName", "Pixel 4 API 32");
        caps.setCapability("app", System.getProperty("user.dir") + "/instrumented-apk/ready.apk");
        caps.setCapability("fullReset", false);
        caps.setCapability("noReset", true);

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(3));
        eyes = new Eyes(runner);
        Configuration config = new Configuration();
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        config.setServerUrl(System.getenv("APPLITOOLS_SERVER_URL"));
        config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_4));
        config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_3_XL, ScreenOrientation.LANDSCAPE, DeviceAndroidVersion.LATEST));
        eyes.setConfiguration(config);
        Eyes.setNMGCapabilities(caps, eyes.getApiKey(), String.valueOf(eyes.getServerUrl()));
    }

    @Test
    public void basicDemoTest() {
        eyes.open(driver, "Basic Android App", "Java Appium Android UFG Demo2");
        eyes.check("Check home activity", Target.window());
    }

    @AfterTest
    public void tearDown() {
        eyes.closeAsync();
        runner.getAllTestResults();
        if (driver != null) {
            driver.quit();
        }
        eyes.abortIfNotClosed();
    }
}
