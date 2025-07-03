package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.Album.AlbumDto;
import com.swapapp.swapappmockserver.service.album.AlbumServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;
import com.swapapp.swapappmockserver.model.Album;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumServiceImpl albumServiceImpl;

    Logger logger = Logger.getLogger(AlbumController.class.getName());

    @GetMapping("/count-by-category")
    public ResponseEntity<List<AlbumCategoryCountDto>> getAlbumCountByCategory() {
        // get user id
        return new ResponseEntity<>(albumServiceImpl.getAlbumCountByCategory(), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Album>> getAlbumsCategroy(@RequestParam("categoryId") String categoryId) {
        logger.info("/albums categroy " + categoryId);
        return new ResponseEntity<>(albumServiceImpl.getAlbumsCategory(categoryId), HttpStatus.OK);
    }

    @GetMapping("/v1/{albumId}")
    public ResponseEntity<Album> getAlbum(@RequestParam("albumId") String albumId) {
        logger.info("/albums by id " + albumId);
        return new ResponseEntity<>(albumServiceImpl.getAlbum(albumId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Album>> getAlbums() {
        logger.info("/albums");
        return new ResponseEntity<>(albumServiceImpl.getAlbums(), HttpStatus.OK);
    }

    @GetMapping("/v2/{email}")
    public ResponseEntity<List<Album>> getUserAlbums(@RequestParam("email") String email) {
        logger.info("/albums");
        return new ResponseEntity<>(albumServiceImpl.getUserAlbums(email), HttpStatus.OK);
    }

    @GetMapping("/{albumId}/{email}")
    public ResponseEntity<Album> getUserAlbum(@RequestParam("albumId") String albumId, @RequestParam("email") String email) {
        logger.info("/albums by id " + albumId);
        return new ResponseEntity<>(albumServiceImpl.getUserAlbum(albumId, email), HttpStatus.OK);
    }

    @GetMapping("/category/{email}")
    public ResponseEntity<List<Album>> getUserAlbumsCategory(@RequestParam("categoryId") String categoryId, @RequestParam("email") String email) {
        logger.info("/albums categroy " + categoryId);
        return new ResponseEntity<>(albumServiceImpl.getUserAlbumsCategory(categoryId, email), HttpStatus.OK);
    }
}
