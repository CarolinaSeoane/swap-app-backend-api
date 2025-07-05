package com.swapapp.swapappmockserver.service.trade;

import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import com.swapapp.swapappmockserver.repository.trade.ITradeRepository;
import com.swapapp.swapappmockserver.repository.user.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;
import com.swapapp.swapappmockserver.service.album.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TradeServiceImpl implements ITradeService {

    @Autowired
    private ITradeRepository tradeRepository;
//    @Autowired
//    private AlbumServiceImpl albumService;
//    @Autowired
//    private JwtUtil jwtUtil;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public List<TradeRequest> getAllTradeRequestsByUser(String email) {
        return tradeRepository.getAllTradeRequestsByUser(email);
    }

    @Override
    public void createTradeRequest(TradeRequest tradeRequest) {
        String email = "";
    }

}
