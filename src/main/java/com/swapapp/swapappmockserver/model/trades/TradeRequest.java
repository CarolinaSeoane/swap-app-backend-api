package com.swapapp.swapappmockserver.model.trades;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private UUID id;
    private Integer album;
    private String albumName;
    private UserDto from;
    private UserDto to;
    private List<Integer> stickers;
    private List<Integer> toGive;
    private TradeRequestStatus status;
}
