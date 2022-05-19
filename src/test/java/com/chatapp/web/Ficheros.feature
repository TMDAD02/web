Feature: Compartir ficheros con otros usuarios
  Scenario: Enviar un fichero a un usuario
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    When Usuario sube un fichero correcto
    And Cerrar el navegador
    And Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'maria'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'mario' para chatear
    And El mensaje 'mario-' es recibido con éxito
    And Cerrar el navegador

Feature: Compartir ficheros con otros usuarios
  Scenario: Enviar un fichero muy grande a un usuario sale mal
    Given Abrir el navegador y entrar en la aplicación
    Then Login es mostrado
    When Usuario introduce su nickname 'mario'
    Then Usuario es conectado con éxito
    And Usuario selecciona otro usuario 'maria' para chatear
    When Usuario sube un fichero incorrecto
    Then Usuario recibe error al subir fichero
    And Cerrar el navegador