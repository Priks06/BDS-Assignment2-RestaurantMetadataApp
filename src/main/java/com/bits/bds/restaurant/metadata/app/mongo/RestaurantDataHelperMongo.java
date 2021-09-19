package com.bits.bds.restaurant.metadata.app.mongo;

import com.bits.bds.restaurant.metadata.app.data.RestaurantDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantDataHelperMongo {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantDataHelperMongo.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantDataHelperMongo(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void transformAndSaveData(List<RestaurantDetails> rawData) {
        List<RestaurantEntity> transformedData = rawData.stream()
                .filter(restaurantDetails -> !"Not yet rated".equals(restaurantDetails.getRating()))
                .distinct()
                .map(this::transformToEntity)
                .collect(Collectors.toList());
        logger.info("Transformed raw data and found {} records.", transformedData.size());
        restaurantRepository.saveAll(transformedData);
        logger.info("Saved transformed data to MongoDB.");
    }

    private RestaurantEntity transformToEntity(RestaurantDetails restaurantDetails) {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName(restaurantDetails.getName());
        restaurantEntity.setAddress(restaurantDetails.getAddress() + ", " + restaurantDetails.getAddressLine2());
        restaurantEntity.setCuisine(restaurantDetails.getCuisine());
        restaurantEntity.setRating(Double.parseDouble(restaurantDetails.getRating()));
        restaurantEntity.setZipcode(restaurantDetails.getOutcode() + restaurantDetails.getPostcode());
        return restaurantEntity;
    }
}
