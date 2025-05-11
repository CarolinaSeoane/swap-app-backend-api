package com.swapapp.swapappmockserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private String albumId;
    private List<TradingCard> tradingCards;
    private Boolean finished;
    private AlbumCategory category;
}
