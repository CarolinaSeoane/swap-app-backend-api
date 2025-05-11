package com.swapapp.swapappmockserver.repository;

import com.swapapp.swapappmockserver.dto.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;

import java.util.List;

public interface IAlbumRepository {
    List<Album> getAlbums();
}
