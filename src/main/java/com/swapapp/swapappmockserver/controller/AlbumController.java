package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.Album.AlbumDto;
import com.swapapp.swapappmockserver.service.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumServiceImpl albumServiceImpl;

    @GetMapping("/count-by-category")
    public ResponseEntity<List<AlbumCategoryCountDto>> getAlbumCountByCategory() {
        // get user id
        return new ResponseEntity<>(albumServiceImpl.getAlbumCountByCategory(), HttpStatus.OK);
    }

}
