package com.swapapp.swapappmockserver.service.album;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;

import java.util.List;

public interface IAlbumService {
    List<AlbumCategoryCountDto> getAlbumCountByCategory();
    List<Album> getAlbumsCategory(String category);
    Album getAlbum(String albumId);
    List<Album> getAlbums();
    List<Album> getUserAlbums(String email);
    Album getUserAlbum(String albumId, String email);
    List<Album> getUserAlbumsCategory(String category, String userId);
}