package com.bits.bds.restaurant.metadata.app.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "restaurant_details")
public class RestaurantEntity implements Serializable {

    private static final long serialVersionUID = -654127184964950432L;

    @Id
    private String recordId;

    private String name;

    private String address;

    private String zipcode;

    private double rating;

    private String cuisine;
}
