package com.swapapp.swapappmockserver.model.trades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sticker {
    private String album;
    private String name;
    private String number;
}
