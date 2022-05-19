Feature: Enviar mensajes a todos los uauarios para publicidad
  Scenario: Admin envía mensaje y es recibido por todos los usuarios
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'admin'
    Then Usuario es conectado con éxito
    And Usuario introduce un mensaje 'Broadcast desde Selenium'
    Then Mensaje 'Broadcast desde Selenium' es recibido por alerta
    And Cerrar el navegador
    When Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Mensaje 'Broadcast desde Selenium' aparece en anuncios
    And Cerrar el navegador