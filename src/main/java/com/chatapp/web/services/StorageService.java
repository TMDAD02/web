package com.chatapp.web.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();
	String store(MultipartFile file, String sender);
	Resource loadAsResource(String filename);
	Path load(String filename);



}
