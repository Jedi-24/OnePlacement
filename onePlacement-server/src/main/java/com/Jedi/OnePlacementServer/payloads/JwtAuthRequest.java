package com.Jedi.OnePlacementServer.payloads;

import lombok.Data;

@Data
public class
JwtAuthRequest {
    private String username, password;
}