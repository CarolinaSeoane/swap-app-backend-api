package com.swapapp.swapappmockserver.repository.album;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    /*@Override
    public void save(User user) {
        Optional<User> existing = findByEmail(user.getEmail());
        if (existing.isPresent()) {
            users.remove(existing.get());
        }
        users.add(user);
        persistUsers(); // Guardar en el archivo
    }*/

}