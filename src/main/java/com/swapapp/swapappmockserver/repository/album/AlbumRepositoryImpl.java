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
        File file = new File(ALBUMS_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();

        if (!file.exists()) {
            System.err.println("Archivo albums.json no encontrado en: " + file.getAbsolutePath());
            file.getParentFile().mkdirs(); // Crear directorio data si no existe
            listOfAlbums = new ArrayList<>(); // Lista vacía por defecto
        } else {
            listOfAlbums = objectMapper.readValue(file, new TypeReference<List<Album>>() {});
            System.out.println("Albums cargados: " + listOfAlbums.size() + " desde " + ALBUMS_FILE_PATH);
        }
    }

    @Override
    public List<Album> getAlbums() {
        return this.listOfAlbums;
    }
}