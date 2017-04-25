/**
 * Created by laurazhou on 4/21/17.
 */
import io.appium.java_client.*;
import io.appium.java_client.android.*;
import org.junit.*;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import java.net.URL;
import java.util.*;


public class AppiumTest {

    final String USER_ZIP_INPUT = "95112 ";
    final String WRONG_EMAIL_ADDRESS = "wrongAddress@gmail.com";
    final String WRONG_PWD = "wrongpwd";
    final String EXPECTED_DISMISS_TEXT = "Dismiss";
    final String RIGHT_EMAIL_ADDRESS = "cmpe287team2@gmail.com";
    final String RIGHT_PWD = "tester123";
    final String FAVORITE_GIRLS_NIGHT_OUT = "Girls Night Out";

    private AppiumDriver driver;


    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "AndroidAppium");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //allow location
        threadWait(2000);
        driver.findElement(By.id("com.android.packageinstaller:id/permission_allow_button")).click();

        //enter zip
        threadWait(2000);
        driver.findElement(By.id("com.groupon:id/action_bar_search_edittext")).sendKeys(USER_ZIP_INPUT);
        threadWait(2000);
        WebElement confirmZipInput = driver.findElement(By.id("com.groupon:id/search_suggestions_list_item"));

        //confirm the right zip and click
        threadWait(2000);
        String actualZipInput = confirmZipInput.getText() + " ";
        Assert.assertEquals(actualZipInput, USER_ZIP_INPUT);
        confirmZipInput.click();
    }


    @Test
    public void logInAndDealPreference() {

        //log in with wrong info
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("com.groupon:id/done")))
                .click();
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("com.groupon:id/fragment_log_in_sign_up_email")))
                .sendKeys(WRONG_EMAIL_ADDRESS);
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("com.groupon:id/fragment_log_in_sign_up_password")))
                .sendKeys(WRONG_PWD);
        driver.findElement(By.id("com.groupon:id/fragment_log_in_sign_up_groupon_button")).click();

        WebElement dismissButton = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("com.groupon:id/dialog_positive_button")));
        String buttonText = dismissButton.getText();
        Assert.assertEquals(buttonText, EXPECTED_DISMISS_TEXT);
        dismissButton.click();



        //log in with right info
        WebElement accEle = driver.findElement(By.id("com.groupon:id/fragment_log_in_sign_up_email"));
        accEle.clear();
        accEle.sendKeys(RIGHT_EMAIL_ADDRESS);
        WebElement pwdEle = driver.findElement(By.id("com.groupon:id/fragment_log_in_sign_up_password"));
        pwdEle.clear();
        pwdEle.sendKeys(RIGHT_PWD);
        driver.findElement(By.id("com.groupon:id/fragment_log_in_sign_up_groupon_button")).click();



        //edit Deal Preference
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ImageButton[1]")))
                .click();
        ((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiSelector().text(\"Deal Preferences\")").click();
        List<WebElement> webElements = driver.findElements(By.id("com.groupon:id/deal_tag_name"));

        for(WebElement webElement : webElements) {
            String elementText = webElement.getText();
            if(elementText.equals(FAVORITE_GIRLS_NIGHT_OUT)) {
                webElement.click();
                break;
            }
        }



        //return to homepage from Deal Preference page
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.view.ViewGroup[1]/android.widget.ImageButton[1]")))
                .click();



        threadWait(3000);
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    /***
     * Thread "pauses" for milliSec milliseconds.
     * @param milliSec
     */
    private void threadWait(int milliSec) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
