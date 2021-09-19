package com.bits.bds.restaurant.metadata.app.boot;

import com.bits.bds.restaurant.metadata.app.data.*;
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
public class RestaurantMetadataLoaderSpringBootApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantMetadataLoaderSpringBootApp.class);

    private final RestaurantDataLoader restaurantDataLoader;

    private final RestaurantDataHelperMongo restaurantDataHelperMongo;

    public RestaurantMetadataLoaderSpringBootApp(RestaurantDataLoader restaurantDataLoader, RestaurantDataHelperMongo restaurantDataHelperMongo) {
        this.restaurantDataLoader = restaurantDataLoader;
        this.restaurantDataHelperMongo = restaurantDataHelperMongo;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantMetadataLoaderSpringBootApp.class, args).close();
    }

    @Override
    public void run(String... args) {
        /*List<RestaurantDetails> rawData =  restaurantDataLoader.loadRawData();
        logger.info("Restaurant data received of size: {}", rawData.size());
        restaurantDataHelperMongo.transformAndSaveData(rawData);*/
        List<RestaurantTop5Details> restaurantsByCuisineAndZipcode = restaurantDataHelperMongo.findRestaurantsByCuisineAndZipcode("Lebanese", "CF243JH");
        logger.info("Cuisine and zipcode: {}", restaurantsByCuisineAndZipcode);
        List<RestaurantAddressSearchDetails> restaurantsByAddressAndMinRating = restaurantDataHelperMongo.findRestaurantsByAddressAndMinRating("Bristol", 4.5);
        logger.info("Search by address and min rating: {}", restaurantsByAddressAndMinRating);
        List<RestaurantZipcodeCountDetails> restaurantsZipcodeCount = restaurantDataHelperMongo.findCountOfCuisinePerZipcode("Lebanese");
        logger.info("Count of restaurants per zipcode: {}", restaurantsZipcodeCount);
        List<RestaurantAvgRatingDetails> avgRatingPerCuisine = restaurantDataHelperMongo.findAvgRatingPerCuisine();
        logger.info("Average rating per type of food: {}", avgRatingPerCuisine);
    }
}
