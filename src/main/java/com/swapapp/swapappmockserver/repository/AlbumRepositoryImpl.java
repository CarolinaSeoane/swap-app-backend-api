package com.swapapp.swapappmockserver.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AlbumRepositoryImpl implements IAlbumRepository {

    private List<Album> listOfAlbums = new ArrayList<>();

    public AlbumRepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Album> albums;

        file= ResourceUtils.getFile("classpath:albums.json");
        albums = objectMapper.readValue(file,new TypeReference<List<Album>>(){});

        listOfAlbums = albums;
    }

    @Override
    public List<Album> getAlbums() {
        return this.listOfAlbums;
    }

}
