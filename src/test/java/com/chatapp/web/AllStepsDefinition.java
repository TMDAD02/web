package com.chatapp.web;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class AllStepsDefinition {

    WebDriver driver;
    List<WebDriver> simultaneos;

    @Given("^Abrir el navegador y entrar en la aplicación$")
    public void abrirNavegadorEntrarAplicacion() throws Throwable {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://ge.danielhuici.ml:8888/");
    }

    @Given("^Abrir cinco navegadores y entrar en la aplicación$")
    public void abrirCincoNavegadoresEntrarAplicacion() throws Throwable {
        simultaneos = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            WebDriver drv = new ChromeDriver();
            drv.get("http://ge.danielhuici.ml:8888/");
            simultaneos.add(drv);
        }
    }

    @Given("^Abrir dos navegadores y entrar en la aplicación$")
    public void abrirDosNavegadoresEntrarAplicacion() throws Throwable {
        simultaneos = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            WebDriver drv = new ChromeDriver();
            drv.get("http://ge.danielhuici.ml:8888/");
            simultaneos.add(drv);
        }
    }

    @And("^Login es mostrado$")
    public void loginEsMostrado() throws Throwable {
        String ingresarString = driver.findElement(By.className("modal-dialog")).getText();
        assertEquals(ingresarString, "Ingresar");
    }

    @And("^Login es mostrado en cinco sesiones$")
    public void loginEsMostradoCincoSesiones() throws Throwable {
        for (WebDriver wbd : simultaneos) {
            String ingresarString = wbd.findElement(By.className("modal-dialog")).getText();
            assertEquals(ingresarString, "Ingresar");
        }
    }

    @When("^Usuario introduce su nickname '(.*)'$")
    public void usuarioIntroduceNickname(String username) throws Throwable {
        driver.findElement(By.className("form-control")).sendKeys(username);
        driver.findElement(By.className("btn-primary")).click();
    }

    @When("Usuario introduce en sesion {int} su nickname {string}")
    public void usuarioIntroduceNicknameSesion(int sesion, String username) throws Throwable {
        simultaneos.get(sesion).findElement(By.className("form-control")).sendKeys(username);
        simultaneos.get(sesion).findElement(By.className("btn-primary")).click();
    }

    @And("Usuario es conectado con éxito")
    public void muestraPanelChat() throws InterruptedException {
        Thread.sleep(5000);
        String conectado = driver.findElement(By.id("connection-text")).getText();

        assertTrue(conectado.contains("Conectado"));
    }

    @And("Cinco usuarios son conectados con éxito")
    public void muestraPanelChatCinco() throws InterruptedException {
        Thread.sleep(5000);
        for (WebDriver wb : simultaneos) {
            String conectado = wb.findElement(By.id("connection-text")).getText();
            assertTrue(conectado.contains("Conectado"));
        }
    }

    @And("^Usuario selecciona otro usuario '(.*)' para chatear$")
    public void usuarioSeleccionaOtroUsuarioParaChatear(String usuario) throws Throwable {
        driver.findElement(By.linkText(usuario)).click();
    }

    @And("Usuario selecciona otro usuario {string} para chatear en sesion {int}")
    public void usuarioSeleccionaOtroUsuarioParaChatearSesion(String usuario, int sesion) throws Throwable {
        simultaneos.get(sesion).findElement(By.linkText(usuario)).click();
    }

    @When("^Usuario introduce un mensaje '(.*)'$")
    public void usuarioIntroduceMensaje(String mensaje) throws Throwable {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@placeholder='Write your message here...']")).sendKeys(mensaje);

    }

    @When("Usuario introduce un anuncio {string} en sesion {int}")
    public void usuarioIntroduceAnuncio(String mensaje, int sesion) throws Throwable {
        Thread.sleep(3500);
        simultaneos.get(sesion).findElement(By.xpath("//input[@placeholder='Broadcast your message here...']")).sendKeys(mensaje);
        simultaneos.get(sesion).findElement(By.id("btn-broadcast")).click();
    }

    @When("Usuario introduce un mensaje {string} en sesion {int}")
    public void usuarioIntroduceMensajeSesion(String mensaje, int sesion) throws Throwable {
        Thread.sleep(5000);
        simultaneos.get(sesion).findElement(By.xpath("//input[@placeholder='Write your message here...']")).sendKeys(mensaje);
        //driver.findElement(By.className("form-control input-sm chat_input")).sendKeys(mensaje);
    }

    @And("^Usuario envia el mensaje$")
    public void usuarioEnviaMensajes() throws Throwable {
        driver.findElement(By.id("btn-chat")).click();
    }


    @And("^Cinco usuarios envian el mensaje$")
    public void cincoUsuariosEnvianMensajes() throws Throwable {
        for (WebDriver wb : simultaneos) {
            wb.findElement(By.id("btn-chat")).click();
        }
    }

    @Then("^Mensaje '(.*)' es enviado con exito$")
    public void mensajeEsEnviadoConExito(String mensaje) throws Throwable {
        Thread.sleep(2000);
        List<WebElement> mensajesEnviados = driver.findElements(By.className("msg_receive"));
        assertTrue(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }

    @Then("^Mensaje '(.*)' es enviado con exito en sesion '[0-9]'$")
    public void mensajeEsEnviadoConExitoSesion(String mensaje, int sesion) throws Throwable {
        List<WebElement> mensajesEnviados = simultaneos.get(sesion).findElements(By.className("msg_receive"));
        assertTrue(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }


    @Then("^Mensaje '(.*)' no es enviado con exito$")
    public void mensajeNoEsEnviadoConExito(String mensaje) throws Throwable {
        List<WebElement> mensajesEnviados = driver.findElements(By.className("msg_receive"));
        assertFalse(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }

    @Then("^Mensaje '(.*)' es recibido por alerta$")
    public void mensajeEsRecibidoPorAlerta(String mensaje) throws Throwable {
        Thread.sleep(5000);
        try {
            assertTrue(driver.switchTo().alert().getText().contains(mensaje));
            driver.switchTo().alert().accept();
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Then("Mensaje {string} es recibido por alerta en sesión {int}")
    public void mensajeEsRecibidoPorAlertaSesion(String mensaje, int sesion) throws Throwable {
        Thread.sleep(3000);
        try {
            assertTrue(simultaneos.get(sesion).switchTo().alert().getText().contains(mensaje));
            simultaneos.get(sesion).switchTo().alert().accept();
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Then("El mensaje {string} es recibido con éxito")
    public void mensajesSonRecibidosOk(String mensaje) throws InterruptedException {
        Thread.sleep(3000);
       // System.out.println(simultaneos.get(0).getPageSource());
        List<WebElement> mensajesEnviados = driver.findElements(By.className("msg_sent"));
        assertTrue(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }

    @Then("El anuncio {string} es persistente en el sistema")
    public void anuncioEsPersistente(String mensaje) throws InterruptedException {
        Thread.sleep(3000);
        // System.out.println(simultaneos.get(0).getPageSource());
        List<WebElement> mensajesEnviados = simultaneos.get(0).findElements(By.className("msg_sent"));
        assertTrue(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }

    @Then("El mensaje {string} es recibido con éxito en grupo")
    public void mensajesSonRecibidosOkGrupo(String mensaje) throws InterruptedException {
        Thread.sleep(3000);
        // System.out.println(simultaneos.get(0).getPageSource());
        List<WebElement> mensajesEnviados = driver.findElements(By.className("msg_sent"));
        assertTrue(mensajesEnviados.get(mensajesEnviados.size() - 1).getText().contains(mensaje));
    }

    @And("Usuario acepta todas las alertas")
    public void usuarioAceptaTodasLasAlertas() throws InterruptedException {
        Thread.sleep(2000);
        simultaneos.get(0).switchTo().alert().accept();
        simultaneos.get(0).switchTo().alert().accept();
        simultaneos.get(0).switchTo().alert().accept();
        simultaneos.get(0).switchTo().alert().accept();
        //simultaneos.get(0).switchTo().alert().accept();
    }

    @When("Usuario sube un fichero correcto")
    public void usuarioSubeFichero() throws InterruptedException {
        Thread.sleep(3000);
        WebElement fichero = driver.findElement(By.id("input-file"));
        fichero.sendKeys("C:\\Users\\danie\\IdeaProjects\\chatApp\\web\\chromedriver.exe");
        driver.findElement(By.xpath("//input[@value='Subir']")).click();
    }

    @When("Usuario sube un fichero incorrecto")
    public void usuarioSubeFicheroIncorrecto() throws InterruptedException {
        Thread.sleep(3000);
        WebElement fichero = driver.findElement(By.id("input-file"));
        fichero.sendKeys("C:\\Users\\danie\\IdeaProjects\\chatApp\\web\\qbittorrent.exe");
        driver.findElement(By.xpath("//input[@value='Subir']")).click();
    }

    @Then("Usuario recibe error al subir fichero")
    public void usuarioRecibeErrorAlSubirFichero() {
        System.out.println("Error ok");
    }

    @When("Usuario crea grupo {string}")
    public void usuarioCreaGrupoGrupoPruebas(String grupo) {
        driver.findElement(By.xpath("//input[@placeholder='Insert name of new group...']")).sendKeys(grupo);
        driver.findElements(By.id("btn-chat")).get(1).click();
    }

    @When("Usuario elimina grupo {string}")
    public void usuarioEliminaGrupoGrupoPruebas(String grupo) {
        driver.findElement(By.xpath("//input[@placeholder='Insert group name to remove..']")).sendKeys(grupo);
        driver.findElements(By.id("btn-chat")).get(2).click();
    }

    @Then("Grupo {string} aparece en la lista de grupos")
    public void grupoApareceEnLaListaDeGrupos(String grupo) {
        String nombregrupo = driver.findElement(By.linkText(grupo)).getText();
        assertEquals(nombregrupo, grupo);
    }

    @Then("Usuario entra a grupo {string}")
    public void usuarioEntraAGrupo(String grupo) {
        driver.findElement(By.linkText(grupo)).click();
    }

    @Then("Grupo {string} no aparece en la lista de grupos")
    public void grupoNoApareceEnLaListaDeGrupos(String grupo) {
        try {
            String nombregrupo = driver.findElement(By.linkText(grupo)).getText();
            assert(false);
        } catch (Exception e) {
            assert(true);
        }
    }

    @When("Usuario añade a {string} al grupo {string}")
    public void usuarioAñadeAMariaAlGrupoGrupoPruebas(String usuario, String grupo) {
        driver.findElement(By.xpath("//input[@placeholder='Insert nombre usuario añadir..']")).sendKeys(usuario);
        driver.findElement(By.xpath("//input[@placeholder='Insert nombre grupo añadir..']")).sendKeys(grupo);
        driver.findElements(By.id("btn-chat")).get(3).click();
    }


    @When("Usuario elimina a {string} del grupo {string}")
    public void usuarioEliminaUsuarioDeGrupo(String usuario, String grupo) {
        driver.findElement(By.xpath("//input[@placeholder='Insert nombre usuario añadir..']")).sendKeys(usuario);
        driver.findElement(By.xpath("//input[@placeholder='Insert nombre grupo añadir..']")).sendKeys(grupo);
        driver.findElements(By.id("btn-chat")).get(4).click();
    }

    @When("Accede sin privilegios al grupo {string}")
    public void accedeSinPrivilegiosAlGrupoGrupoPruebas(String grupo) {
        driver.get("http://ge.danielhuici.ml:8888/chat?to=" + grupo);
    }

    @And("Usuario consulta el trending")
    public void usuarioConsultaElTrending() {
        driver.get("http://ge.danielhuici.ml:8888/trend");
    }

    @Then("Recibe error sin privilegios")
    public void recibeErrorSinPrivilegios() {
        assertTrue(driver.getPageSource().contains("401"));
    }

    @Then("^Cerrar el navegador$")
    public void cerrarNavegador() throws Throwable {
        driver.close();
    }

    @Then("^Cerrar todos los navegadores$")
    public void cerrarTodosNavegadores() throws Throwable {
        for (WebDriver wb : simultaneos) {
            wb.close();
        }
    }



}