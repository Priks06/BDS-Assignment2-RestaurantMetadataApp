package com.bits.bds.restaurant.metadata.app.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "name")
public class RestaurantDetails implements Serializable {

    private static final long serialVersionUID = -3146718533531457944L;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("_id")
    private Object id;

    private String name;

    private String address;

    @JsonProperty("address line 2")
    private String addressLine2;

    private String outcode;

    private String postcode;

    private String rating;

    @JsonProperty("type_of_food")
    private String cuisine;
}
