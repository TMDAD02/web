Feature: Enviar mensajes a otros usuarios para tener conversaciones puntuales

  Scenario: Enviar mensaje simple y recepción (inferior a 500 caracteres) y tiempo inferior a 60s
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    And Usuario introduce un mensaje 'Hola maria, saludos desde Selenium'
    And Usuario envia el mensaje
    Then Mensaje 'Hola maria, saludos desde Selenium' es enviado con exito
    And Cerrar el navegador

  Scenario: Enviar mensaje simple y recepción (superior a 500 caracteres)
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    And Usuario introduce un mensaje 'Este mensaje más de 500 caracteres: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
    And Usuario envia el mensaje
    Then Mensaje 'Este mensaje más de 500 caracteres: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' no es enviado con exito
    And Cerrar el navegador


  Scenario: Enviar mensaje simple y recepción (offline)
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    And Usuario introduce un mensaje 'Hola maria, saludos desde Selenium, pero estas offline'
    And Usuario envia el mensaje
    And Cerrar el navegador
    When Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'maria'
    #Then Mensaje 'Hola maria, saludos desde Selenium' es recibido por alerta
    Then Mensaje 'Hola maria, saludos desde Selenium, pero estas offline' es recibido por alerta
    And Cerrar el navegador

  Scenario: Cinco usuarios simultaneos
    Given Abrir cinco navegadores y entrar en la aplicación
    Then Login es mostrado en cinco sesiones
    When Usuario introduce en sesion 0 su nickname 'mario'
    When Usuario introduce en sesion 1 su nickname 'maria'
    When Usuario introduce en sesion 2 su nickname 'paco'
    When Usuario introduce en sesion 3 su nickname 'omar'
    When Usuario introduce en sesion 4 su nickname 'admin'
    Then Cinco usuarios son conectados con éxito
    And Usuario selecciona otro usuario 'mario' para chatear en sesion 1
    And Usuario selecciona otro usuario 'mario' para chatear en sesion 2
    And Usuario selecciona otro usuario 'mario' para chatear en sesion 3
    And Usuario selecciona otro usuario 'mario' para chatear en sesion 4
    And Usuario introduce un mensaje 'Hola mario, soy maria en prueba simultanea' en sesion 1
    And Usuario introduce un mensaje 'Hola mario, soy paco en prueba simultanea' en sesion 2
    And Usuario introduce un mensaje 'Hola mario, soy omar en prueba simultanea' en sesion 3
    And Usuario introduce un mensaje 'Hola mario, soy admin en prueba simultanea' en sesion 4
    And Cinco usuarios envian el mensaje
    And Usuario acepta todas las alertas
    And Usuario selecciona otro usuario 'maria' para chatear en sesion 0
    Then El mensaje 'Hola mario, soy maria en prueba simultanea' es recibido con éxito en sesión 0
    And Usuario selecciona otro usuario 'paco' para chatear en sesion 0
    Then El mensaje 'Hola mario, soy paco en prueba simultanea' es recibido con éxito en sesión 0
    And Usuario selecciona otro usuario 'omar' para chatear en sesion 0
    Then El mensaje 'Hola mario, soy omar en prueba simultanea' es recibido con éxito en sesión 0
    And Usuario selecciona otro usuario 'admin' para chatear en sesion 0
    Then El mensaje 'Hola mario, soy admin en prueba simultanea' es recibido con éxito en sesión 0
    Then Cerrar todos los navegadores