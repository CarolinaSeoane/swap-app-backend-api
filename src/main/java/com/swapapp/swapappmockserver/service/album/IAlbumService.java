package com.swapapp.swapappmockserver.service.album;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;

import java.util.List;

public interface IAlbumService {
    List<AlbumCategoryCountDto> getAlbumCountByCategory();
    List<Album> getAlbumsCategory(String category);
    Album getAlbum(String albumId);

}