Feature: Enviar mensajes a todos los usuarios para publicidad
  Scenario: Admin envía mensaje y es recibido por todos los usuarios activos
    Given Abrir dos navegadores y entrar en la aplicación
    When Usuario introduce en sesion 0 su nickname 'admin'
    When Usuario introduce en sesion 1 su nickname 'mario'
    And Usuario introduce un anuncio 'Broadcast desde Selenium' en sesion 0
    Then Mensaje 'Broadcast desde Selenium' es recibido por alerta en sesión 1
    And Cerrar el navegador


  Scenario: Admin envía anuncio y es persistente
    Given Abrir el navegador y entrar en la aplicación
    When Usuario introduce su nickname 'mario'
    Then El anuncio 'Broadcast' es persistente en el sistema
    And Cerrar el navegador
