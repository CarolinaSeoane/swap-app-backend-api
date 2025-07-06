package com.swapapp.swapappmockserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kiosk {
    private String id;
    private String name;
    private String address;
    private String responsible;
    private Coordinates coordinates;
    private double rating;
    private String openFrom;
    private String openUntil;
    private int availableUnits;
    private boolean stock;
    private int price;
}
