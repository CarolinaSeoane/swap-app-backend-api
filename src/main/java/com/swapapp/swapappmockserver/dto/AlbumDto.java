package com.swapapp.swapappmockserver.dto;

import com.swapapp.swapappmockserver.model.AlbumCategory;
import com.swapapp.swapappmockserver.model.TradingCard;
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
