import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import static org.assertj.core.api.Assertions.assertThat;

public class pageobjectTest {
    private WelcomePage welcomePage;
    private ChooseFlightPage chooseFlightPage;
    private PurchaseFlightPage purchaseFlightPage;
    private ChromeDriver driver;

    public pageobjectTest() {
        this.welcomePage = new WelcomePage();
        this.chooseFlightPage = new ChooseFlightPage();
        this.purchaseFlightPage = new PurchaseFlightPage();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions().setBinary("/usr/bin/brave-browser");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void testUntitledTestCase() {
        driver.get("https://blazedemo.com/");
        welcomePage.findFlights("Portland", "Berlin");
        chooseFlightPage.clickChooseFlight();
        purchaseFlightPage.purchaseFlight("la", "la", "la", "al", "12", "1", "j");
        assertThat(driver.getTitle()).contains("BlazeDemo Confirmation");

    }

}

class WelcomePage {

    @FindBy(name = "fromPort")
    private WebElement fromPort;

    @FindBy(name = "toPort")
    private WebElement toPort;

    @FindBy(xpath = "//input[@value='Find Flights']")
    private WebElement findFlights;

    public void selectFromPort(String fromPort) {
        new Select(this.fromPort).selectByVisibleText(fromPort);
    }

    public void selectToPort(String toPort) {
        new Select(this.toPort).selectByVisibleText(toPort);
    }

    public void clickFindFlights() {
        this.findFlights.click();
    }

    public void findFlights(String fromPort, String toPort) {
        selectFromPort(fromPort);
        selectToPort(toPort);
        clickFindFlights();
    }
}

class ChooseFlightPage {

    @FindBy(xpath = "//tr[3]/td/input")
    private WebElement chooseFlight;

    public void clickChooseFlight() {
        this.chooseFlight.click();
    }
}

class PurchaseFlightPage {

    @FindBy(id = "inputName")
    private WebElement inputName;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "zipCode")
    private WebElement zipCode;

    @FindBy(id = "creditCardNumber")
    private WebElement creditCardNumber;

    @FindBy(id = "nameOnCard")
    private WebElement nameOnCard;

    @FindBy(xpath = "//input[@value='Purchase Flight']")
    private WebElement purchaseFlight;

    public void inputName(String inputName) {
        this.inputName.clear();
        this.inputName.sendKeys(inputName);
    }

    public void inputAddress(String address) {
        this.address.clear();
        this.address.sendKeys(address);
    }

    public void inputCity(String city) {
        this.city.clear();
        this.city.sendKeys(city);
    }

    public void inputState(String state) {
        this.state.clear();
        this.state.sendKeys(state);
    }

    public void inputZipCode(String zipCode) {
        this.zipCode.clear();
        this.zipCode.sendKeys(zipCode);
    }

    public void inputCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber.clear();
        this.creditCardNumber.sendKeys(creditCardNumber);
    }

    public void inputNameOnCard(String nameOnCard) {
        this.nameOnCard.clear();
        this.nameOnCard.sendKeys(nameOnCard);
    }

    public void clickPurchaseFlight() {
        this.purchaseFlight.click();
    }

    public void purchaseFlight(String inputName, String address, String city, String state, String zipCode,
            String creditCardNumber, String nameOnCard) {
        inputName(inputName);
        inputAddress(address);
        inputCity(city);
        inputState(state);
        inputZipCode(zipCode);
        inputCreditCardNumber(creditCardNumber);
        inputNameOnCard(nameOnCard);
        clickPurchaseFlight();
    }
}