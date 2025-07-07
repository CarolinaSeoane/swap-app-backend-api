package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile-images/{filename}")
    public ResponseEntity<Resource> getProfileImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename
    ) {
        System.out.println("Llego al controller con token");

        try {
            // Validar JWT
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            // [Opcional] Validar si el usuario tiene permiso para ver esa imagen

            Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", "profile-images", filename);
            Resource resource = new FileSystemResource(imagePath.toFile());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build(); // Acceso denegado
        }
    }

    @GetMapping("/album-images/{filename}")
    public ResponseEntity<Resource> getAlbumImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename
    ) {
        System.out.println("Llego al controller de album images con token");

        try {
            // Validar JWT
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", "album-images", filename);
            Resource resource = new FileSystemResource(imagePath.toFile());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build(); // Acceso denegado
        }
    }

    @GetMapping("/kiosko-images/backgrounds/{filename}")
    public ResponseEntity<Resource> getKioskoBackgroundImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename
    ) {
        System.out.println("Llego al controller de kiosko background images con token");

        try {
            // Validar JWT
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", "kiosko-images", "backgrounds", filename);
            Resource resource = new FileSystemResource(imagePath.toFile());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build(); // Acceso denegado
        }
    }

    @GetMapping("/kiosko-images/owner/{filename}")
    public ResponseEntity<Resource> getKioskoOwnerImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename
    ) {
        System.out.println("Llego al controller de kiosko owner images con token");

        try {
            // Validar JWT
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);

            Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", "kiosko-images", "owner", filename);
            Resource resource = new FileSystemResource(imagePath.toFile());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build(); // Acceso denegado
        }
    }
}
