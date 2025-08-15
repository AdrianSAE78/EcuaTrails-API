package com.ecuatrails.api.dto;

public record ImageDto(Integer id, String url, String title, String alt, Boolean cover, Integer position) {
}