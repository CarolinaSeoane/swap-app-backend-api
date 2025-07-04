package com.swapapp.swapappmockserver.repository.album;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.model.Album;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlbumRepositoryImpl implements IAlbumRepository {
    private static final String ALBUMS_FILE_PATH = "data/albums.json";
    private List<Album> listOfAlbums = new ArrayList<>();

    public AlbumRepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Album> albums;

        file = new File(ALBUMS_FILE_PATH);
        albums = objectMapper.readValue(file,new TypeReference<List<Album>>(){});
        listOfAlbums = albums;
    }

    @Override
    public List<Album> getAlbums() {
        return this.listOfAlbums;
    }
}