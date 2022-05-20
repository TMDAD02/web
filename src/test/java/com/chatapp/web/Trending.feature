Feature: Conocer los temas que se están hablando en tiempo real

  Scenario: Admin pretende conocer trending
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'admin'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    And Usuario introduce un mensaje 'gato gato gato gato gato gato gato gato gato gato'
    And Usuario consulta el trending
    Then Mensaje 'Hola maria, saludos desde Selenium' es enviado con exito
    And Cerrar el navegador