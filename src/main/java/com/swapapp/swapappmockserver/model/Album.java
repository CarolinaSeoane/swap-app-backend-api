package com.swapapp.swapappmockserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.swapapp.swapappmockserver.model.trades.TradingCard;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private Integer id;
    private String albumId;
    private String name;
    private List<TradingCard> tradingCards;
    private Boolean finished;
    private AlbumCategory category;
    private String cover;
    private Integer releaseYear;
    private Integer totalCards;

}
