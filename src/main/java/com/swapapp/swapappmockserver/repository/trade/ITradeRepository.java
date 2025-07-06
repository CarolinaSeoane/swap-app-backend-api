package com.swapapp.swapappmockserver.repository.trade;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;

import java.util.List;

public interface ITradeRepository {
    List<PossibleTrade> getAllTradeRequests();
    List<TradeRequest> getAllTradeRequestsByUser(String email);
    void createTradeRequestFromPossibleTrade(PossibleTrade possibleTrade, UserDto from);
}
