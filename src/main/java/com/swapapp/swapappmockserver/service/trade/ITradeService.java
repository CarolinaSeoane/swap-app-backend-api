package com.swapapp.swapappmockserver.service.trade;

import com.swapapp.swapappmockserver.dto.User.*;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITradeService {
    List<TradeRequest> getAllTradeRequestsByUser(String email);
    void createTradeRequest(TradeRequest tradeRequest);
}

