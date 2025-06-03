package com.swapapp.swapappmockserver.dto.TradeRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.swapapp.swapappmockserver.model.trades.Sticker;
import com.swapapp.swapappmockserver.model.trades.TradeRequestStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDto {
    private String id;
    private String fromUserEmail;
    private String toUserEmail;
    private TradeRequestStatus status;
    private List<Sticker> offeredStickers;
    private List<Sticker> requestedStickers;
    private Boolean seen;
}