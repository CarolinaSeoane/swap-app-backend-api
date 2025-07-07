package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import com.swapapp.swapappmockserver.service.trade.TradeServiceImpl;
import com.swapapp.swapappmockserver.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trade-requests")
public class TradeRequestController {

    @Autowired
    private TradeServiceImpl tradeService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/get-all")
    public ResponseEntity<List<TradeRequest>> getAllRequests(
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

    @PostMapping("/new")
    public ResponseEntity<Void> tradeRequest(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PossibleTrade body
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            tradeService.createTradeRequest(body, token);
            return ResponseEntity.status(201).body(null);
        } catch (RuntimeException e) {
            System.out.println("Error creating trade request: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/reject/{tradeId}")
    public ResponseEntity<Void> rejectTradeRequest(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID tradeId
            ) {
        try {
            tradeService.rejectTradeRequest(tradeId);
            return ResponseEntity.status(200).body(null);
        } catch (RuntimeException e) {
            System.out.println("Error rejecting trade request: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/accept")
    public ResponseEntity<Void> acceptTradeRequest(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TradeRequest tradeRequest
    ) {
        try {
            tradeService.acceptTradeRequest(tradeRequest);
            return ResponseEntity.status(200).body(null);
        } catch (RuntimeException e) {
            System.out.println("Error accepting trade request: " + e);
            return ResponseEntity.status(500).body(null);
        }
    }

}
