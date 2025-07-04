package com.swapapp.swapappmockserver.service.album;

import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.trades.TradingCard;

import java.util.List;

public interface IAlbumService {
    List<AlbumCategoryCountDto> getAlbumCountByCategory();
    List<Album> getUserAlbums(String email);
    Album getUserAlbum(String albumId, String email);
    List<Album> getUserAlbumsCategory(String category, String userId);
    Album updateUserCards(String albumId, List<TradingCard> tradingCards, String email);
}