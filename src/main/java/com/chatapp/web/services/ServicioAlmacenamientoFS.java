package com.chatapp.web.services;

import com.chatapp.web.scheduled.Metricas;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ServicioAlmacenamientoFS implements ServicioAlmacenamiento {

	private final String bucket = "chatapp02-tmdad2022-";
	private MinioClient minioClient;

	@Autowired
	private Metricas metricas;

	@Override
	public String store(MultipartFile file, String sender) throws IOException, NoSuchAlgorithmException, InvalidKeyException{
		try {
			String generatedFilename = generateFilename(file.getOriginalFilename());

			boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket+sender).build());
			if (!found) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket+sender).build());
			} else {
				System.out.println("Bucket "+ bucket + sender +" already exists.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				minioClient.putObject(
						PutObjectArgs.builder()
								.bucket(bucket+sender)
								.object(generatedFilename)
								.stream(inputStream, inputStream.available(), -1)
								.build());
				inputStream.close();

				System.out.println(generatedFilename +" is uploaded successfully");
				return generatedFilename;
			}
			//metricas.incrementBytes(file.getBytes().length);

		} catch (MinioException e) {
			return null;
		}
	}


	@Override
	public InputStream loadAsStream(String filename, String sender)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException{
		try {

			InputStream stream =
					minioClient.getObject(
							GetObjectArgs.builder()
									.bucket(bucket+sender)
									.object(filename)
									.build());

			return stream;
		}
		catch (MinioException e) {
			System.out.println("Could not read file: " + e);
		}
		return null;
	}


	@Override
	public void init() {
		minioClient =
				MinioClient.builder()
						.endpoint("https://play.min.io")
						.credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
						.build();

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
