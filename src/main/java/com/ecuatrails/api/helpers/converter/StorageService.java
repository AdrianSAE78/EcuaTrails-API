package com.ecuatrails.api.helpers.converter;

public interface StorageService {

	String upload(String folder, String filename, java.io.InputStream in, String contentType, long size);
}
