package com.swapapp.swapappmockserver.dto.User;

import com.swapapp.swapappmockserver.model.trades.StickerTrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAlbumDto {
    private Integer id;
    private List<StickerTrade> stickers;

    public void addSticker(StickerTrade newSticker) {
        stickers.add(newSticker);
    }
}
