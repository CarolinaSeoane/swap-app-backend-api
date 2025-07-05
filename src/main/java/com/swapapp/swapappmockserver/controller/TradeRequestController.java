package com.swapapp.swapappmockserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.TradeRequest.TradeRequestDto;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import com.swapapp.swapappmockserver.service.trade.TradeServiceImpl;
import com.swapapp.swapappmockserver.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;

@RestController
@RequestMapping("/trade-requests")
public class TradeRequestController {

    @Autowired
    private TradeServiceImpl tradeService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllRequests(
            @RequestHeader("Authorization") String authHeader
    ){
        try {
            String token = authHeader.replace("Bearer ", "");
            String userEmail = userService.getCurrentUser(token).getEmail();
            List<TradeRequest> tradeRequests = tradeService.getAllTradeRequestsByUser(userEmail);
            return ResponseEntity.ok(tradeRequests);
        } catch (RuntimeException e) {
            System.out.println("Error getting trade requests: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }

//    @PostMapping("/new")
//    public ResponseEntity<?> tradeRequest(
//            @RequestHeader("Authorization") String authHeader
//    ) {
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            List<PossibleTrade> trades = userService.getPossibleTrades(token);
//            return ResponseEntity.ok(trades);
//        } catch (RuntimeException e) {
//            System.out.println("Error getting possible trades: " + e);
//            return ResponseEntity.status(500).body(null);
//        }
//    }

}
