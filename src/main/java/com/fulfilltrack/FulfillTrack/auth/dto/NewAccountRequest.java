package com.fulfilltrack.FulfillTrack.auth.dto;

public record NewAccountRequest(String username, String password, String email, String nombre, String apellido) {
}