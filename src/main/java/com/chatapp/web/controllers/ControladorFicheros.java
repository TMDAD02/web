package com.chatapp.web.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.chatapp.web.models.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.chatapp.web.services.ServicioAlmacenamiento;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ControladorFicheros {

    @Autowired
    private ControladorWebSocket controladorWebSocket;
    private final ServicioAlmacenamiento storageService;
    private static final String PUBLIC_FILES_PATH = "/files";

    @Autowired
    public ControladorFicheros(ServicioAlmacenamiento storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = PUBLIC_FILES_PATH + "/{filename:.+}" + "/{sender}", method = RequestMethod.GET)
    public StreamingResponseBody getSteamingFile(HttpServletResponse response, @PathVariable String filename,@PathVariable String sender)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException{

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        InputStream inputStream = storageService.loadAsStream(filename,sender);
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
        };
    }

    @PostMapping("/fichero")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("currentTo") String destino, @AuthenticationPrincipal final UserDetails ud) throws Throwable{
        String sender = ud.getUsername();
        String filename = storageService.store(file, sender);
        if(filename != null) { // Chapuza pero funciona. Cambiar en el futuro
            controladorWebSocket.enviarMensajeFichero(
                    new Mensaje(sender, "<a href=" + PUBLIC_FILES_PATH + "/" + filename + "/" + sender + " >" + filename + "</a>", destino));
        }
        return "redirect:/chat?&to=" + destino;
    }

}
