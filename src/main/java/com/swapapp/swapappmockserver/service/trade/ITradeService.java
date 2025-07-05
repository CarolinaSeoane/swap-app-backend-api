package com.swapapp.swapappmockserver.service.trade;

import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;

import java.util.List;

public interface ITradeService {
    List<TradeRequest> getAllTradeRequestsByUser(String email);
    void createTradeRequest(PossibleTrade possibleTrade, String token);
}

