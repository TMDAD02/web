package com.chatapp.web.controllers;

import java.io.IOException;

import com.chatapp.web.models.Mensaje;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.core.io.Resource;
        import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.chatapp.web.services.ServicioAlmacenamiento;

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


    @GetMapping(PUBLIC_FILES_PATH + "/{filename:.+}" + "/{sender}" + "/{receiver}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,@PathVariable String sender, @PathVariable String receiver) {
        Resource file = storageService.loadAsResource(filename,sender, receiver);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/fichero")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("currentTo") String destino, @AuthenticationPrincipal final UserDetails ud) throws JSONException {
        String sender = ud.getUsername();
        String filename = storageService.store(file, sender, destino);
        if(filename != null) { // Chapuza pero funciona. Cambiar en el futuro
            controladorWebSocket.enviarMensajeFichero(new Mensaje(sender, "<a href=" + PUBLIC_FILES_PATH + "/" + filename + "/" + sender + "/" + destino + " >" + filename + "</a>", destino));
        }
        return "redirect:/chat?&to=" + destino;
    }





}
