package com.chatapp.web.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ServicioAlmacenamiento {

	void init();
	String store(MultipartFile file, String sender, String receiver);
	Resource loadAsResource(String filename);
	Path load(String filename);



}
