package com.ecuatrails.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "FirebaseAuthRequest", description = "Token emitido por Firebase para intercambio por JWT propio")
public class FirebaseAuthRequest {
	
	@Schema(description = "ID Token de Firebase", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6...")
    @NotBlank
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
