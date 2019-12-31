package test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TesteAtomic {
	private static WebDriver wdriver;
	private static WebDriverWait wdw;

	@BeforeClass
	public static void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
		wdriver = new ChromeDriver();
		wdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wdw = new WebDriverWait(wdriver, 10);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		wdriver.quit();
	}

	private boolean acceptNextAlert = true;

	private StringBuffer verificationErrors = new StringBuffer();

	@Test
	public void teste1() throws Exception {
		String sitePorCep = "http://www.buscacep.correios.com.br/sistemas/buscacep/buscaEndereco.cfm";
		String siteResultado = "http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaEndereco.cfm";
		String cep = "01001-001";
		String logradoudo = "Praça da Sé - lado par";
		String bairro = "Sé";
		String estado = "São Paulo/SP";

		wdriver.get(sitePorCep);
		wdw.until(ExpectedConditions.elementToBeClickable(By.id("cep")));
		Assert.assertNotNull(wdriver.findElement(By.id("cep")));
		wdriver.findElement(By.id("cep")).clear();
		wdriver.findElement(By.id("cep")).click();
		wdriver.findElement(By.id("cep")).sendKeys(cep);
		wdriver.findElement(By.id("Geral")).submit();

		wdw.until(ExpectedConditions.urlToBe(siteResultado));
		Assert.assertTrue(siteResultado.equalsIgnoreCase(wdriver.getCurrentUrl()));

		List<WebElement> trs = wdriver.findElements(By.xpath("//table[@class='tmptabela']/tbody/tr"));
		
		//Garantindo que a tabela contém apenas 1 resultado alem do seu head.
		Assert.assertTrue(trs.size() == 2);
		List<WebElement> tds = trs.get(1).findElements(By.xpath("//td"));
		
		//Exibindo os valores em cada uma das colunas.
		for (WebElement td : tds) {
			System.out.println(td.getText());
		}

		Assert.assertEquals(logradoudo, tds.get(0).getText().trim());
		Assert.assertEquals(bairro, tds.get(1).getText().trim());
		Assert.assertEquals(estado, tds.get(2).getText().trim());
		Assert.assertEquals(cep, tds.get(3).getText().trim());
	}

	@Test
	public void teste2() throws Exception {
		String sitePorLogradouro = "http://www.buscacep.correios.com.br/sistemas/buscacep/buscaCepEndereco.cfm";
		String siteResultado = "http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm";
		String logradoudo = "Praça da Sé - lado par";

		wdriver.get(sitePorLogradouro);
		wdw.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='relaxation']")));
		wdriver.findElement(By.xpath("//input[@name='relaxation']")).clear();
		wdriver.findElement(By.xpath("//input[@name='relaxation']")).click();
		wdriver.findElement(By.xpath("//input[@name='relaxation']")).sendKeys(logradoudo);
		wdriver.findElement(By.id("Geral")).submit();

		wdw.until(ExpectedConditions.urlToBe(siteResultado));
		Assert.assertTrue(siteResultado.equalsIgnoreCase(wdriver.getCurrentUrl()));

		List<WebElement> trs = wdriver.findElements(By.xpath("//table[@class='tmptabela']/tbody/tr"));
		//Garantindo que a tabela não ficou vazia.
		Assert.assertTrue(trs.size() > 1);
		
		// removendo o head da table.
		trs.remove(0);

		for (WebElement tr : trs) {
			System.out.println(tr.getText());
		}
	}

}
