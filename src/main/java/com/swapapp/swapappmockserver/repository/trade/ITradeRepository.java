package com.swapapp.swapappmockserver.repository.trade;

import com.swapapp.swapappmockserver.model.trades.TradeRequest;

import java.util.List;

public interface ITradeRepository {
    List<TradeRequest> getAllTradeRequests();
    List<TradeRequest> getAllTradeRequestsByUser(String email);

//    List<TradeRequest> getTradeRequestsMadeByUser(String id);
//    List<TradeRequest> getTradeRequestsForUser(String id);
}
