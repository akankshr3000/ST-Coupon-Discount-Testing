package edu.bitm.coupon;

import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CouponDiscountTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        String page = Path.of("src", "test", "resources", "coupon-demo.html").toAbsolutePath().toUri().toString();
        driver.get(page);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() { if (driver != null) driver.quit(); }

    private void enterAndApply(String code) {
        driver.findElement(By.id("coupon")).sendKeys(code);
        driver.findElement(By.id("apply")).click();
    }
    private String text(String id) { return wait.until(d -> d.findElement(By.id(id)).getText()); }

    @Test public void validCouponAppliesTenPercentDiscount() {
        enterAndApply("SAVE10");
        Assert.assertEquals(text("message"), "Coupon applied successfully!");
        Assert.assertEquals(text("discount"), "Discount: ₹100.00");
        Assert.assertEquals(text("total"), "Final total: ₹900.00");
    }

    @Test public void secondValidCouponAppliesTwentyPercentDiscount() {
        enterAndApply("SAVE20");
        Assert.assertEquals(text("discount"), "Discount: ₹200.00");
        Assert.assertEquals(text("total"), "Final total: ₹800.00");
    }

    @Test public void invalidCouponShowsErrorAndDoesNotChangeTotal() {
        enterAndApply("NOTREAL");
        Assert.assertEquals(text("message"), "Invalid coupon code.");
        Assert.assertEquals(text("discount"), "Discount: ₹0.00");
        Assert.assertEquals(text("total"), "Final total: ₹1000.00");
    }

    @Test public void emptyCouponShowsValidationMessage() {
        driver.findElement(By.id("apply")).click();
        Assert.assertEquals(text("message"), "Please enter a coupon code.");
        Assert.assertEquals(text("total"), "Final total: ₹1000.00");
    }

    @Test public void couponCannotBeAppliedTwice() {
        enterAndApply("SAVE10");
        WebElement field = driver.findElement(By.id("coupon"));
        field.clear(); field.sendKeys("SAVE20");
        driver.findElement(By.id("apply")).click();
        Assert.assertEquals(text("message"), "A coupon has already been applied.");
        Assert.assertEquals(text("discount"), "Discount: ₹100.00");
        Assert.assertEquals(text("total"), "Final total: ₹900.00");
    }
}
