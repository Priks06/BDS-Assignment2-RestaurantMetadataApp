package com.bits.bds.restaurant.metadata.app.boot;

import com.bits.bds.restaurant.metadata.app.data.RestaurantDataLoader;
import com.bits.bds.restaurant.metadata.app.data.RestaurantDetails;
import com.bits.bds.restaurant.metadata.app.mongo.RestaurantDataHelperMongo;
import com.bits.bds.restaurant.metadata.app.mongo.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.bits.bds.restaurant.metadata.app")
@EnableMongoRepositories(basePackageClasses = RestaurantRepository.class)
public class RestaurantMetadataSpringBootApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantMetadataSpringBootApp.class);

    private final RestaurantDataLoader restaurantDataLoader;

    private final RestaurantDataHelperMongo restaurantDataHelperMongo;

    public RestaurantMetadataSpringBootApp(RestaurantDataLoader restaurantDataLoader, RestaurantDataHelperMongo restaurantDataHelperMongo) {
        this.restaurantDataLoader = restaurantDataLoader;
        this.restaurantDataHelperMongo = restaurantDataHelperMongo;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantMetadataSpringBootApp.class, args).close();
    }

    @Override
    public void run(String... args) {
        List<RestaurantDetails> rawData =  restaurantDataLoader.loadRawData();
        logger.info("Restaurant data received of size: {}", rawData.size());
        restaurantDataHelperMongo.transformAndSaveData(rawData);
    }
}
