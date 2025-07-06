package com.swapapp.swapappmockserver.model.trades;

import com.swapapp.swapappmockserver.dto.User.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PossibleTrade {
    private Integer album;
    private String albumName;
    private UserDto from;
    private List<Integer> stickers;
    private List<Integer> toGive;
}
