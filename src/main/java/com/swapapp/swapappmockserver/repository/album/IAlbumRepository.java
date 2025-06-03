package com.swapapp.swapappmockserver.repository.album;

import com.swapapp.swapappmockserver.model.Album;

import java.util.List;

public interface IAlbumRepository {
    List<Album> getAlbums();
}