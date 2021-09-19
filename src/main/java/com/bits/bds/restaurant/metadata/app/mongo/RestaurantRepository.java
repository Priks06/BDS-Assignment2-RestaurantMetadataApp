package com.bits.bds.restaurant.metadata.app.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {
}
