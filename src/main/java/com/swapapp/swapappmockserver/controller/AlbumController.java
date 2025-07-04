package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.trades.TradingCard;
import com.swapapp.swapappmockserver.service.album.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumServiceImpl albumServiceImpl;

    Logger logger = Logger.getLogger(AlbumController.class.getName());

    @GetMapping("/count-by-category/{email}")
    public ResponseEntity<List<AlbumCategoryCountDto>> getAlbumCountByCategory(@PathVariable("email") String email) {
        logger.info("/count-by-category");
        return new ResponseEntity<>(albumServiceImpl.getAlbumCountByCategory(email), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<Album>> getUserAlbums(@PathVariable("email") String email) {
        logger.info("/albums");
        return new ResponseEntity<>(albumServiceImpl.getUserAlbums(email), HttpStatus.OK);
    }

    @GetMapping("/{albumId}/{email}")
    public ResponseEntity<Album> getUserAlbum(@PathVariable("albumId") String albumId, @PathVariable("email") String email) {
        logger.info("/albums by id");
        return new ResponseEntity<>(albumServiceImpl.getUserAlbum(albumId, email), HttpStatus.OK);
    }

    @GetMapping("/category/{email}")
    public ResponseEntity<List<Album>> getUserAlbumsCategory(@PathVariable("email") String email, @RequestParam("categoryId") String categoryId) {
        logger.info("/albums category ");
        return new ResponseEntity<>(albumServiceImpl.getUserAlbumsCategory(categoryId, email), HttpStatus.OK);
    }

    @PutMapping("/{albumId}/{email}")
    public ResponseEntity<Album> updateUserCards(@PathVariable String albumId, @PathVariable String email, @RequestBody List<TradingCard> tradingCards) {
        logger.info("/updateUserCards");
        return new ResponseEntity<>(albumServiceImpl.updateUserCards(albumId, tradingCards, email), HttpStatus.OK);
    }
}
