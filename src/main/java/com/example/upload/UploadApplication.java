package com.example.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class UploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
	}

}

@RestController
@RequestMapping("/api/v1/files")
class FileUploadController {

	@PostMapping
	ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

		// Validate file size and type
		if (!file.isEmpty() && allowedFileTypes.contains(file.getContentType())) {
			// Generate unique filename
			String fileName = new File(UUID.randomUUID().toString()).getName() + "." + file.getOriginalFilename().split("\\.")[1];
			// Save file
			file.transferTo(new File(fileUploadPath, fileName));
			// Build response
			FileUploadResponse response = new FileUploadResponse(fileName, file.getOriginalFilename(), file.getContentType(), file.getSize());
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	// Define allowed file types list
	private final List<String> allowedFileTypes = Arrays.asList("image/jpeg", "image/png", "application/pdf", "text/plain");

	// Define file upload path
	private final String fileUploadPath = "C:\\files";
}

record FileUploadResponse (String fileName, String originalFileName, String contentType, long size) {}
