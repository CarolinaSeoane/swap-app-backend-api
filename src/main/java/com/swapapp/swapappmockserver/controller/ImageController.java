package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename
    ) {
        try {
            // Validar token
            String email = jwtUtil.extractEmail(authHeader.replace("Bearer ", ""));

            // ⚠️ Opcional: podrías validar que la imagen le pertenezca a ese email

            File file = new File("uploads/profile-images/" + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(403).build(); // Token inválido o error
        }
    }
}
