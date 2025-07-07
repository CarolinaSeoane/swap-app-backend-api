package com.swapapp.swapappmockserver.repository.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import com.swapapp.swapappmockserver.model.trades.TradeRequestStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TradeRepositoryImpl implements ITradeRepository {
    private List<PossibleTrade> possibleTrades = new ArrayList<>();
    private List<TradeRequest> tradeRequests = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<PossibleTrade> getAllTradeRequests() {
        return possibleTrades;
    }

    @Override
    public List<TradeRequest> getAllTradeRequestsByUser(String email) {
        return tradeRequests.stream()
                .filter(t ->
                        email.equals(t.getFrom().getEmail()) || email.equals(t.getTo().getEmail())
                )
                .toList();
    }

    @Override
    public void createTradeRequestFromPossibleTrade(PossibleTrade possibleTrade, UserDto from) {
        TradeRequest tradeRequest = mapper.convertValue(possibleTrade, TradeRequest.class);
        tradeRequest.setStatus(TradeRequestStatus.PENDING);
        // Switch to and from
        System.out.println("FROM que llega del front: " + tradeRequest.getFrom());
        tradeRequest.setTo(tradeRequest.getFrom());
        tradeRequest.setFrom(from);
        tradeRequest.setId(UUID.randomUUID());

        tradeRequests.add(tradeRequest);
    }

    @Override
    public TradeRequest getTradeRequestById(UUID tradeId) {
        return tradeRequests.stream()
                .filter(t -> t.getId().equals(tradeId))
                .toList().getFirst();
    }

}
