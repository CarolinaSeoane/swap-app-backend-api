package com.swapapp.swapappmockserver.model.trades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StickerTrade {
    private Integer number;
    private Integer repeatCount;
}
