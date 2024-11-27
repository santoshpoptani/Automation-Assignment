package com.automation.assignment;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;


import java.time.Duration;

import java.util.List;

public class FitPeoAutomation {

    private String xpath_Slider = "//span[@class = 'MuiSlider-thumb MuiSlider-thumbSizeMedium MuiSlider-thumbColorPrimary MuiSlider-thumb MuiSlider-thumbSizeMedium MuiSlider-thumbColorPrimary css-1sfugkh']/input";
    private String xpath_input_Id = "// div[contains(@class , 'MuiInputBase-root')]/input";
    private String xpath_ALl_Cpt_Cards = "//div[@class = 'MuiBox-root css-1p19z09']/child::div";
    private WebDriver driver;

    public FitPeoAutomation() {
        // Set up ChromeDriver using WebDriverManager

        WebDriverManager.chromedriver().clearResolutionCache();

        WebDriverManager.chromedriver().setup(); // This will automatically manage the ChromeDriver
        this.driver = new ChromeDriver(); // Initialize the ChromeDriver
    }

    // Function to navigate to the homepage
    public void navigateToHomepage() {
        try {
            driver.get("https://www.fitpeo.com"); // Replace with the correct homepage URL
            driver.manage().window().maximize();
            System.out.println("Navigated to FitPeo Homepage.");
        } catch (Exception e) {
            System.err.println("Error navigating to homepage: " + e.getMessage());
        }
    }

    // Function to navigate to the Revenue Calculator page
    public void navigateToRevenueCalculator() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement revenueCalculatorLink = wait.until(
                    ExpectedConditions.elementToBeClickable(By.linkText("Revenue Calculator"))); // Replace with correct link text
            revenueCalculatorLink.click();
            System.out.println("Navigated to Revenue Calculator page.");
        } catch (Exception e) {
            System.err.println("Error navigating to Revenue Calculator page: " + e.getMessage());
        }
    }

    // Function to adjust the slider to the value of 820
    public void adjustSliderTo820() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement slider = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range']")));
            Actions actions = new Actions(driver);

            actions.clickAndHold(slider).moveByOffset(93, 0).release().perform();  // Adjust the offset as needed
            System.out.println("Adjusted slider to 820.");
        } catch (Exception e) {
            System.err.println("Error adjusting the slider: " + e.getMessage());
        }
    }

    // Function to update the text field to 560 and adjust slider accordingly
    public void updateTextFieldAndAdjustSliderTo560() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath_input_Id)));

            WebElement slider = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range']")));

            Actions actions = new Actions(driver);
            actions.clickAndHold(slider).moveByOffset(-1000, 0).release().perform();
            actions.moveToElement(textField).click().perform();

            // Delete the selected value (Backspace)
            actions.sendKeys(Keys.BACK_SPACE).perform();

            actions.moveToElement(textField).click().sendKeys("560").perform();
            System.out.println("Text field updated to 560.");

        } catch (Exception e) {
            System.err.println("Error updating text field: " + e.getMessage());
        }
    }

    // Function to validate that the slider reflects the value 560
    public void validateSliderValueIs560() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement slider = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range']")));
            String sliderValue = slider.getAttribute("value");
            if ("560".equals(sliderValue)) {
                System.out.println("Slider value is correctly updated to 560.");
            } else {
                System.err.println("Slider value mismatch. Expected 560 but got: " + sliderValue);
            }
        } catch (Exception e) {
            System.err.println("Error validating slider value: " + e.getMessage());
        }
    }

    // Function to select CPT codes
    public void selectCPTCodes() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> cptAllCards = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath_ALl_Cpt_Cards+"//p[1]")));


            for (WebElement element : cptAllCards) {
                if (element.getText().matches("CPT-99091|CPT-99453|CPT-99454|CPT-99474")) {
                    WebElement element1= element.findElement(By.xpath("./parent::div//input[@type='checkbox']"));
                    element1.click();
                }
            }

        } catch (Exception e) {
            System.err.println("Error selecting CPT codes: " + e.getMessage());
        }
    }

    // Function to validate the Total Recurring Reimbursement
    public void validateTotalReimbursement() {
         String actualHeader = "Total Recurring Reimbursement for all Patients Per Month";
         String actualReimbursementValue = "$110700";
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement slider = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range']")));
            Actions actions = new Actions(driver);

            actions.clickAndHold(slider).moveByOffset(-1000, 0).release().perform();
            actions.clickAndHold(slider).moveByOffset(123, 0).release().perform();

            WebElement reimbursementHeader = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-m1khva']/child::p[1]")));
            WebElement reimbursementValue = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-m1khva']/child::p[2]")));

            //Asserting Header and Value
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(actualHeader , reimbursementHeader.getText() , "Header is not matching");
            softAssert.assertEquals(actualReimbursementValue , reimbursementValue.getText() , "Values not Matched");
            softAssert.assertAll();
        } catch (Exception e) {
            System.err.println("Error validating total reimbursement: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FitPeoAutomation automation = new FitPeoAutomation();

        automation.navigateToHomepage();
        automation.navigateToRevenueCalculator();
        automation.adjustSliderTo820();
        automation.updateTextFieldAndAdjustSliderTo560();
        automation.validateSliderValueIs560();
        automation.selectCPTCodes();
        automation.validateTotalReimbursement();

        // Closing the browser after task completion
        automation.driver.quit();
    }

}
