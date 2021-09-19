package com.bits.bds.restaurant.metadata.app.data;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RestaurantZipcodeCountDetails implements Serializable {

    private static final long serialVersionUID = -3146718533531457944L;

    private String zipcode;

    private Integer count;

}
