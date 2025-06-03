package com.swapapp.swapappmockserver.dto.Album;

import com.swapapp.swapappmockserver.model.AlbumCategory;
import com.swapapp.swapappmockserver.model.trades.TradingCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
    private String albumId;
    private List<TradingCard> tradingCards;
    private Boolean finished;
    private AlbumCategory category;
}
