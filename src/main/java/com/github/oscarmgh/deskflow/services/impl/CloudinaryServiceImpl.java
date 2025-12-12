package com.github.oscarmgh.deskflow.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.github.oscarmgh.deskflow.services.CloudinaryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
	private final Cloudinary cloudinary;

	public Map<String, Object> upload(MultipartFile file) {
		try {
			return cloudinary.uploader().upload(
					file.getBytes(),
					Map.of("resource_type", "auto"));
		} catch (Exception e) {
			throw new RuntimeException("Error uploading file to Cloudinary", e);
		}
	}

	public void delete(String publicId) {
		try {
			cloudinary.uploader().destroy(publicId, Map.of());
		} catch (Exception e) {
			throw new RuntimeException("Error deleting file from Cloudinary", e);
		}
	}
}