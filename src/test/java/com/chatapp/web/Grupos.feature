Feature: Salas de chat permanentes para tener un registro de conversaciones pasadas
  Scenario: Creación de las salas con éxito
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario crea grupo 'grupoPruebas'
    Then Grupo 'grupoPruebas' aparece en la lista de grupos
    And Cerrar el navegador

  Scenario: Añadir usuarios al grupo
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario añade a 'maria' al grupo 'grupoPruebas'
    And Usuario añade a 'paco' al grupo 'grupoPruebas'
    And Cerrar el navegador
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'maria'
    Then Usuario es conectado con éxito
    And Grupo 'grupoPruebas' aparece en la lista de grupos
    And Cerrar el navegador
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'paco'
    Then Usuario es conectado con éxito
    And Grupo 'grupoPruebas' aparece en la lista de grupos
    And Cerrar el navegador

  Scenario: Usuario sin privilegios intenta añadir participante
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'paco'
    Then Usuario es conectado con éxito
    When Usuario añade a 'omar' al grupo 'grupoPruebas'
    And Cerrar el navegador
    When Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'omar'
    Then Usuario es conectado con éxito
    And Grupo 'grupoPruebas' no aparece en la lista de grupos
    And Cerrar el navegador

  Scenario: Usuario sin privilegios intenta eliminar participante
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'paco'
    Then Usuario es conectado con éxito
    When Usuario elimina a 'mario' del grupo 'grupoPruebas'
    And Cerrar el navegador
    When Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Grupo 'grupoPruebas' aparece en la lista de grupos
    And Cerrar el navegador


  Scenario: Usuario sin privilegios intenta eliminar grupo
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'paco'
    Then Usuario es conectado con éxito
    When Usuario elimina grupo 'grupoPruebas'
    And Cerrar el navegador
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Grupo 'grupoPruebas' aparece en la lista de grupos
    And Cerrar el navegador

  Scenario: Usuario sin privilegios intenta acceder al grupo
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'omar'
    Then Usuario es conectado con éxito
    When Accede sin privilegios al grupo 'grupoPruebas'
    Then Recibe error sin privilegios
    And Cerrar el navegador

  Scenario: Los mensajes tienen persistencia
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario entra a grupo 'grupoPruebas'
    And Usuario introduce un mensaje 'Hola grupo!, saludos desde Selenium'
    And Usuario envia el mensaje
    And Cerrar el navegador
    And Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario entra a grupo 'grupoPruebas'
    And El mensaje 'Hola grupo!, saludos desde Selenium' es recibido con éxito en grupo
    And Cerrar el navegador

  Scenario: Usuario intenta enviar mensaje con más de 500 caracteres
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario entra a grupo 'grupoPruebas'
    And Usuario introduce un mensaje 'Este mensaje más de 500 caracteres: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
    And Usuario envia el mensaje
    Then Mensaje 'Este mensaje más de 500 caracteres: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' no es enviado con exito
    And Cerrar el navegador

  Scenario: Enviar un fichero a un grupo
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    When Usuario entra a grupo 'grupoPruebas'
    When Usuario sube un fichero correcto
    And Cerrar el navegador
    And Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'maria'
    Then Usuario es conectado con éxito
    When Usuario entra a grupo 'grupoPruebas'
    And El mensaje 'mario-' es recibido con éxito
    And Cerrar el navegador