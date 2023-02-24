package Android;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.visualgrid.model.AndroidDeviceInfo;
import com.applitools.eyes.visualgrid.model.AndroidDeviceName;
import com.applitools.eyes.visualgrid.model.DeviceAndroidVersion;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
// import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ApiDemoTest {
  public AppiumDriver driver;
  public AndroidTouchAction action;
  public VisualGridRunner runner;
  public Eyes eyes;
  public WebDriverWait wait;

  public void tapOnElement(WebElement element) {
    action.tap(ElementOption.element(element)).perform();
  }

  public void scrollDown() {
    Dimension dimension = driver.manage().window().getSize();
    int scrollStart = (int) (dimension.getHeight() * 0.8);
    int scrollEnd = (int) (dimension.getHeight() * 0.1);
    action = new AndroidTouchAction((PerformsTouchActions) driver)
        .press(PointOption.point(0, scrollStart))
        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        .moveTo(PointOption.point(0, scrollEnd))
        .release()
        .perform();
  }

  public void dragAndDrop(WebElement dragElement, WebElement dropElement) {
    action.longPress(ElementOption.element(dragElement))
        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        .moveTo(ElementOption.element(dropElement))
        .release()
        .perform();
  }

  @BeforeTest
  public void setUp() throws MalformedURLException {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName","Android");
    caps.setCapability("automationName", "UiAutomator2");
    caps.setCapability("platformVersion", "12.0");
    caps.setCapability("deviceName", "Pixel 4 API 32");
    caps.setCapability("app", System.getProperty("user.dir") + "/instrumented-apk/ready.apk");
    caps.setCapability("fullReset", false);
    caps.setCapability("noReset", true);

    driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, 9);
    action = new AndroidTouchAction((PerformsTouchActions) driver);

    runner = new VisualGridRunner(new RunnerOptions().testConcurrency(3));
    eyes = new Eyes(runner);
    Configuration config = new Configuration();
    config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
    config.setServerUrl(System.getenv("APPLITOOLS_SERVER_URL"));
    config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_4));
    config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_3_XL, ScreenOrientation.LANDSCAPE, DeviceAndroidVersion.LATEST));
    config.setForceFullPageScreenshot(false);
    eyes.setConfiguration(config);
    Eyes.setNMGCapabilities(caps, eyes.getApiKey(), String.valueOf(eyes.getServerUrl()));
    eyes.open(driver, "API Demo app", "App button details");
  }

  @Test
  public void clickAppButton() {
    WebElement app = driver.findElementByAccessibilityId("App");
    wait.until(ExpectedConditions.elementToBeClickable(app));
    app.click();
    eyes.checkWindow("Viewport Only");
    driver.navigate().back();
  }

  @Test
  public void scroll_test() {
    tapOnElement(driver.findElementByAccessibilityId("Views"));
    scrollDown();
    tapOnElement(driver.findElementByAccessibilityId("Lists"));
    driver.navigate().back();
    driver.navigate().back();
  }

  @Test
  public void drag_drop() {
    tapOnElement(driver.findElementByAccessibilityId("Views"));
    tapOnElement(driver.findElementByAccessibilityId("Drag and Drop"));
    dragAndDrop(
        driver.findElement(By.id("drag_dot_1")),
        driver.findElement(By.id("drag_dot_2"))
    );
    driver.navigate().back();
    driver.navigate().back();
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
