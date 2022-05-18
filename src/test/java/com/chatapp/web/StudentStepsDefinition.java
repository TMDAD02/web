package com.chatapp.web;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StudentStepsDefinition {

    WebDriver driver;

    @Given("^Open the Chrome and launch the application$")
    public void open_the_chrome_and_launch_the_application() throws Throwable {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://ge.danielhuici.ml:8888/");
    }

    @And("^Login page will be displayed$")
    public void verify_welcome_page() throws Throwable {
        String ingresarString = driver.findElement(By.className("modal-dialog")).getText();
        assertEquals(ingresarString, "Ingresar");
    }

    @When("^User visit student list$")
    public void enter_the_Username_and_Password() throws Throwable {
        driver.get("http://localhost:8080/students");
        driver.findElement(By.className("create_link")).click();
    }

    @Then("^User input and submit student form$")
    public void enter_and_submi_form() throws Throwable {
        driver.findElement(By.id("firstname")).sendKeys("John");
        driver.findElement(By.id("lastname")).sendKeys("Doe");
        driver.findElement(By.id("email")).sendKeys("john.doe@gmail.com");
        driver.findElement(By.className("btn-default")).click();
    }

    @Then("^User will see student list page$")
    public void verify_redurect_to_student__listpage() throws Throwable {
        String actualString = driver.findElement(By.className("student_list_heading")).getText();
        assertTrue(actualString.contains("Student List"));
        driver.close();
    }

}