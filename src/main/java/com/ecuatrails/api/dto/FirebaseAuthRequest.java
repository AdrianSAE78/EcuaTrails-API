package com.ecuatrails.api.dto;

public class FirebaseAuthRequest {
	
	private String idToken;

    public FirebaseAuthRequest() {}

    public FirebaseAuthRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
