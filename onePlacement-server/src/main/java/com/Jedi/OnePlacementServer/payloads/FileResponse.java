package com.Jedi.OnePlacementServer.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class FileResponse {
    private String fileName;
    private String message;
}
