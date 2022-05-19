package com.chatapp.web.services;

import com.chatapp.web.scheduled.Metricas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ServicioAlmacenamientoFS implements ServicioAlmacenamiento {

	private final String LOCAL_PATH = "upload_dir";
	private final Path rootLocation = Paths.get(LOCAL_PATH);

	@Autowired
	private Metricas metricas;

	@Override
	public String store(MultipartFile file, String sender, String receiver) {
		try {
			Path fileLocation = Paths.get(LOCAL_PATH, sender, receiver);
			Files.createDirectories(fileLocation);

			String generatedFilename = generateFilename(file.getOriginalFilename());
			Path destinationFile = fileLocation.resolve(generatedFilename)
					.normalize().toAbsolutePath();

			if (file.isEmpty() || !destinationFile.getParent().equals(fileLocation.toAbsolutePath())) {
				throw new IOException("Failed to store empty file.");
			}
			//metricas.incrementBytes(file.getBytes().length);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
				return generatedFilename;
			}
		} catch (IOException e) {
			return null;
		}
	}


	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				System.out.println("Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			System.out.println("Could not read file: " + e);
		}
		return null;
	}


	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			System.out.println("No se puede inicializar FS: " + e.getMessage());
		}
	}

	private String generateFilename(String filename) {
		return getFileName(filename) + "-" + System.currentTimeMillis() + getFileExtension(filename);
	}

	private String getFileExtension(String filename) {
		int lastIndexOf = filename.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return filename.substring(lastIndexOf);
	}

	private String getFileName(String filename){
		int lastIndexOf = filename.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return filename;
		}
		return filename.substring(0,lastIndexOf);
	}
}
