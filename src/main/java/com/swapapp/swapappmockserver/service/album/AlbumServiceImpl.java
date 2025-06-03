package com.swapapp.swapappmockserver.service.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.Album.AlbumDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.repository.album.IAlbumRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AlbumServiceImpl implements IAlbumService {

    @Autowired
    private IAlbumRepository albumRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<AlbumCategoryCountDto> getAlbumCountByCategory() {
        List<Album> albums = albumRepository.getAlbums();
        if (albums.isEmpty()){
            //
        }

        return albums.stream().collect(Collectors.groupingBy(Album::getCategory, Collectors.counting()))
                .entrySet().stream()
                .map(entry ->
                        new AlbumCategoryCountDto(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

}