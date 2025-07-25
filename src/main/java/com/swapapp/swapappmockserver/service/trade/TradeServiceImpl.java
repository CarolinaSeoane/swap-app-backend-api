package com.swapapp.swapappmockserver.service.trade;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import com.swapapp.swapappmockserver.model.trades.TradeRequestStatus;
import com.swapapp.swapappmockserver.repository.trade.ITradeRepository;
import com.swapapp.swapappmockserver.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class TradeServiceImpl implements ITradeService {

    @Autowired
    private ITradeRepository tradeRepository;

    @Autowired
    private IUserService userService;

    @Override
    public List<TradeRequest> getAllTradeRequestsByUser(String email) {
        return tradeRepository.getAllTradeRequestsByUser(email);
    }

    @Override
    public void createTradeRequest(PossibleTrade possibleTrade, String token) {
        tradeRepository.createTradeRequestFromPossibleTrade(possibleTrade, userService.getCurrentUser(token));
    }

    @Override
    public void rejectTradeRequest(UUID tradeId) {
        TradeRequest tradeRequest = tradeRepository.getTradeRequestById(tradeId);
        tradeRequest.setStatus(TradeRequestStatus.REJECTED);
    }

    @Override
    public void acceptTradeRequest(TradeRequest tradeRequestToAccept) {
        UUID tradeId = tradeRequestToAccept.getId();
        Integer albumId = tradeRequestToAccept.getAlbum();
        
        UserDto sourceUserFromRequest = tradeRequestToAccept.getFrom();
        UserDto endUserFromRequest = tradeRequestToAccept.getTo();

        UserDto sourceUser = userService.getUserByEmail(sourceUserFromRequest.getEmail());
        UserDto endUser = userService.getUserByEmail(endUserFromRequest.getEmail());

        List<Integer> sourceUserGivingStickers = tradeRequestToAccept.getToGive();
        List<Integer> endUserGivingStickers = tradeRequestToAccept.getStickers();

        userService.removeStickersFromAlbum(sourceUser, sourceUserGivingStickers, albumId);
        userService.removeStickersFromAlbum(endUser, endUserGivingStickers, albumId);

        userService.addStickersToAlbum(sourceUser, endUserGivingStickers, albumId);
        userService.addStickersToAlbum(endUser, sourceUserGivingStickers, albumId);

        TradeRequest tradeRequest = tradeRepository.getTradeRequestById(tradeId);
        tradeRequest.setStatus(TradeRequestStatus.ACCEPTED);
    }

}
