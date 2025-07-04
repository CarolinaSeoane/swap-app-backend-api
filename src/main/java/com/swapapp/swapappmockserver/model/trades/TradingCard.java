package com.swapapp.swapappmockserver.model.trades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradingCard {
    private Integer number;
    private String albumId;
    private Boolean obtained = false;
    private Integer repeatedQuantity;
}
