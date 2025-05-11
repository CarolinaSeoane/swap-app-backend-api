package com.swapapp.swapappmockserver.dto;

import com.swapapp.swapappmockserver.model.AlbumCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumCategoryCountDto {
    private AlbumCategory category;
    private Integer count;
}
