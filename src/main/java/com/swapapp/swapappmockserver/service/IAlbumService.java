package com.swapapp.swapappmockserver.service;

import com.swapapp.swapappmockserver.dto.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.AlbumDto;

import java.util.List;

public interface IAlbumService {
    List<AlbumCategoryCountDto> getAlbumCountByCategory();

}
