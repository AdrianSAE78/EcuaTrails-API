package com.ecuatrails.api.service.admin;

import org.springframework.stereotype.Service;

import com.ecuatrails.api.helpers.StorageService;

@Service
public class LocalStorageService implements StorageService {

  private final java.nio.file.Path root = java.nio.file.Paths.get("uploads"); // crea carpeta

  public LocalStorageService() throws java.io.IOException {
    java.nio.file.Files.createDirectories(root);
  }

  @Override
  public String upload(String folder, String filename, java.io.InputStream in, String contentType, long size) {
    try {
      java.nio.file.Path dir = root.resolve(folder);
      java.nio.file.Files.createDirectories(dir);
      var target = dir.resolve(filename);
      java.nio.file.Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
      return "/uploads/" + folder + "/" + filename;
    } catch (Exception e) {
      throw new RuntimeException("File upload failed", e);
    }
  }
}

