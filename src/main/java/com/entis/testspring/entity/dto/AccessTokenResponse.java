package com.entis.testspring.entity.dto;

public record AccessTokenResponse(String accessToken, String refreshToken, long expireIn) {

}