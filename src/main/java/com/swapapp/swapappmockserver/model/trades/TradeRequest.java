package com.swapapp.swapappmockserver.model.trades;

import com.swapapp.swapappmockserver.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private String id;
    private String fromUserEmail;
    private String toUserEmail;
    private TradeRequestStatus status;
    private List<Sticker> offeredStickers;
    private List<Sticker> requestedStickers;
    private boolean seen;
}
