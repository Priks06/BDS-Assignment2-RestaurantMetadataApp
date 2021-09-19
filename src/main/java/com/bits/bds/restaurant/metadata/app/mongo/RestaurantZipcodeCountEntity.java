package com.bits.bds.restaurant.metadata.app.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestaurantZipcodeCountEntity implements Serializable {

    private static final long serialVersionUID = -654127184964950432L;

    @Id
    private String zipcode;

    private Integer count;
}
