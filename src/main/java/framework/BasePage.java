package framework;

import components.Products;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

  //container for driver
  private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

  public static ThreadLocal<WebDriver> getDriverThreadLocal() {
    return DRIVER_THREAD_LOCAL;
  }

  public static void setDriverThreadLocal(WebDriver driver) {
    DRIVER_THREAD_LOCAL.set(driver);
  }

  public static WebDriver getDriver() {
    return DRIVER_THREAD_LOCAL.get();
  }

  // Product container locator
  private final By productContainerLocator = By.xpath(
      "//article[@class='product-miniature js-product-miniature']");

  public WebElement find(By locator) {
    return getDriver().findElement(locator);
  }

  public List<WebElement> findAll(By locator) {
    return getDriver().findElements(locator);
  }

  public WebElement waitUntilVisible(By locator, int seconds) {
    return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds))
        .until(ExpectedConditions.presenceOfElementLocated(locator));
  }

  public WebElement waitUntilAppear(By locator, int seconds) {
    return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds))
        .until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public Boolean waitUntilDisappear(By locator, int seconds) {
    return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds))
        .until(ExpectedConditions.invisibilityOfElementLocated(locator));
  }

  public void scroll(int pixels) {
    JavascriptExecutor jse = (JavascriptExecutor) getDriver();
    jse.executeScript("window.scrollBy(0," + pixels + ")");
  }

  public void scrollPageBottom() {
    JavascriptExecutor jse = (JavascriptExecutor) getDriver();
    jse.executeScript("window.scrollBy(0, document.body.scrollHeight)");
  }

  public void selectByText(By selectLocator, String text) {
    Select select = new Select(getDriver().findElement(selectLocator));
    select.selectByVisibleText(text);
  }

  // Sort products by 'argument'
  public void sortByDropdown(By Locator, String text) {
    Select selectSortBy = new Select(find(Locator));
    selectSortBy.selectByVisibleText(text);
     }



  public String getSelectedValue(By selectLocator) {
    Select select = new Select(getDriver().findElement(selectLocator));
    return select.getFirstSelectedOption().getText();
  }

  public void hoverMouse(By locator) {
    Actions action = new Actions(getDriver());
    action.moveToElement(getDriver().findElement(locator)).build().perform();
  }

  // Get product list (containers) from page
  @SneakyThrows
  public List<Products> getAllProducts() {
    waitUntilVisible(productContainerLocator, 20);
    List<Products> product = new ArrayList<>();
    List<WebElement> containers = findAll(productContainerLocator);
    for (WebElement container : containers) {
      Products productComponent = new Products(container);
      product.add(productComponent);
    }
    return product;
  }
}
