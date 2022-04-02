package com.chatapp.web.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

	private final String LOCAL_PATH = "upload_dir";
	private final Path rootLocation = Paths.get(LOCAL_PATH);


	@Override
	public String store(MultipartFile file, String sender) {
		try {
			String generatedFilename = generateFilename(sender, file.getOriginalFilename());
			Path destinationFile = this.rootLocation.resolve(generatedFilename)
					.normalize().toAbsolutePath();

			if (file.isEmpty() || !destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new IOException("Failed to store empty file.");
			}

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

	private String generateFilename(String sender, String filename) {
		return sender + "-" + System.currentTimeMillis() + getFileExtension(filename);
	}

	private String getFileExtension(String filename) {
		int lastIndexOf = filename.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return filename.substring(lastIndexOf);
	}
}
