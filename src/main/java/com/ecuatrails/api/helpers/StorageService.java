package com.ecuatrails.api.helpers;

public interface StorageService {

	String upload(String folder, String filename, java.io.InputStream in, String contentType, long size);
}
