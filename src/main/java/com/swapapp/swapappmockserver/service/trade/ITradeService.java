package com.swapapp.swapappmockserver.service.trade;

import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;

import java.util.List;
import java.util.UUID;

public interface ITradeService {
    List<TradeRequest> getAllTradeRequestsByUser(String email);
    void createTradeRequest(PossibleTrade possibleTrade, String token);
    void rejectTradeRequest(UUID tradeId);
    void acceptTradeRequest(TradeRequest tradeRequest);
}

