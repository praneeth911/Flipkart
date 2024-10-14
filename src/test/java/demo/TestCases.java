package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
import net.bytebuddy.asm.Advice.Enter;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }

    @Test
    public void testCase01() throws InterruptedException{
        driver.get("https://www.flipkart.com");
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(15));

        wait.until(ExpectedConditions.urlContains("flipkart"));

        WebElement searchBox=driver.findElement(By.xpath("//input[@placeholder='Search for Products, Brands and More']"));
        searchBox.sendKeys("Washing Machine");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        
        WebElement popularity=driver.findElement(By.xpath("//div[contains(text(),'Popularity')]"));
        popularity.click();
        Thread.sleep(2000);

        List<WebElement> ratings=driver.findElements(By.xpath("//div[@class='XQDdHH']"));

        int count = 0;
        try {
            // Iterate through the ratings and count those with rating <= 4
            for (WebElement ratingElement : ratings) {
                try {
                    double rating = Double.parseDouble(ratingElement.getText());
                    if (rating <= 4) {
                        count++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Unable to parse rating.");
                }
            }
            System.out.println("Number of items with rating <= 4: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    @Test
    public void testCase02() throws InterruptedException{
        driver.get("https://www.flipkart.com");
        WebElement searchBox=driver.findElement(By.xpath("//input[@placeholder='Search for Products, Brands and More']"));
        searchBox.sendKeys("iPhone");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        List<WebElement> titles=driver.findElements(By.xpath("//div[@class='KzDlHZ']"));
        List<WebElement> discounts=driver.findElements(By.xpath("//div[@class='UkUFwK']"));

        // for (int i = 0; i < titles.size(); i++) {
        //     try {
               
        //         String title = titles.get(i).getText();
        //         String discountText = discounts.get(i).getText();
    
        //         int discount = Integer.parseInt(discountText.replaceAll("[^0-9]", ""));
                
        //         if (discount > 17) {
        //             System.out.println("Title: " + title + " | Discount: " + discount + "%");
        //         }
        //     } catch (Exception e) {
        //         System.out.println("Error processing item: " + e.getMessage());
        //     }
        // }

        int discountIndex = 0;

        for (int i = 0; i < titles.size(); i++) {
            try {
                String discountText = null;
    
                // Check if there is a discount available for the current title
                if (discountIndex < discounts.size()) {
                    discountText = discounts.get(discountIndex).getText().replaceAll("[^0-9]", ""); // Extract numeric value
                }
    
                // If discount text is not empty, parse it and check the percentage
                if (discountText != null && !discountText.isEmpty()) {
                    int discount = Integer.parseInt(discountText);
    
                    // Print only those items with a discount greater than 17%
                    if (discount > 17) {
                        String title = titles.get(i).getText();
                        System.out.println("Title: " + title + " | Discount: " + discount + "%");
                    }
    
                    // Increment the discount index only if a valid discount was found
                    discountIndex++;
                }
            } catch (Exception e) {
                System.out.println("Error processing item at index " + i + ": " + e.getMessage());
            }
        }
        
    }


    @Test
    public void testCase03() throws InterruptedException{
        driver.get("https://www.flipkart.com");
        WebElement searchBox=driver.findElement(By.xpath("//input[@placeholder='Search for Products, Brands and More']"));
        searchBox.sendKeys("Coffee Mug");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        
        List<WebElement> titles = driver.findElements(By.xpath("//a[@class='wjcEIp']"));
    List<WebElement> reviewCounts = driver.findElements(By.xpath("//span[@class='Wphh3N']"));
    List<WebElement> imageElements = driver.findElements(By.xpath("//img[@class='DByuf4']"));

   
    List<Map<String, Object>> items = new ArrayList<>();


    for (int i = 0; i < Math.min(titles.size(), reviewCounts.size()); i++) {
        try {
            String title = titles.get(i).getText();
            String reviewCountText = reviewCounts.get(i).getText().replaceAll("[^0-9]", ""); // Extract numeric review count
            String imageUrl = imageElements.get(i).getAttribute("src");

            int reviewCount = Integer.parseInt(reviewCountText);
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("title", title);
            itemData.put("reviewCount", reviewCount);
            itemData.put("imageUrl", imageUrl);

            items.add(itemData);
        } catch (Exception e) {
            System.out.println("Error processing item at index " + i + ": " + e.getMessage());
        }
    }

    // Sort items by review count in descending order
    items.sort((item1, item2) -> (int) item2.get("reviewCount") - (int) item1.get("reviewCount"));

    // Print the top 5 items based on review count
    for (int i = 0; i < Math.min(5, items.size()); i++) {
        Map<String, Object> item = items.get(i);
        System.out.println("Title: " + item.get("title") + " | Image URL: " + item.get("imageUrl") + " | Reviews: " + item.get("reviewCount"));
    }
}
}
