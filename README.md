Scenario: Enviar mensaje simple y recepción (inferior a 500 caracteres)
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
Then Mensaje 'Hola maria, saludos desde Selenium' es recibido por alerta
Then Mensaje 'Hola maria, saludos desde Selenium, pero estas offline' es recibido por alerta
And Cerrar el navegador

