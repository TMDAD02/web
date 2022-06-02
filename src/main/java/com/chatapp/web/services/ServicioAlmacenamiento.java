package com.chatapp.web.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ServicioAlmacenamiento {

	void init()  throws IOException, NoSuchAlgorithmException, InvalidKeyException;
	String store(MultipartFile file, String sender)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException;
	InputStream loadAsStream(String filename, String sender)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException;



}
