package com.swapapp.swapappmockserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.TradeRequest.TradeRequestDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;

@RestController
@RequestMapping("/trade-requests")
public class TradeRequestController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/get-all")
    public ResponseEntity<String> getAllRequests() throws IOException {
        System.out.println("✅ LLEGUE a controller");

        // Ruta absoluta basada en el directorio actual de ejecución
        File file = new File("data/tradeRequests.json");
        if (!file.exists()) {
            System.out.println("❌ El archivo no fue encontrado en: " + file.getAbsolutePath());
            return ResponseEntity.status(404).body("Archivo no encontrado");
        }

        String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);

        System.out.println("📦 Contenido leído: " + json);
        return ResponseEntity.ok(json);
    }

}
