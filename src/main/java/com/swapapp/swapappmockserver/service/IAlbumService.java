package com.swapapp.swapappmockserver.service;

import java.util.List;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.Album.AlbumDto;

public interface IAlbumService {
    List<AlbumCategoryCountDto> getAlbumCountByCategory();

}