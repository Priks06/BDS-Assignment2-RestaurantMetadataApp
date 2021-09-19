package com.bits.bds.restaurant.metadata.app.boot;

import com.bits.bds.restaurant.metadata.app.data.*;
import com.bits.bds.restaurant.metadata.app.mongo.RestaurantDataHelperMongo;
import com.bits.bds.restaurant.metadata.app.mongo.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.bits.bds.restaurant.metadata.app")
@EnableMongoRepositories(basePackageClasses = RestaurantRepository.class)
public class RestaurantMetadataLoaderSpringBootApp implements CommandLineRunner {

    private final RestaurantDataLoader restaurantDataLoader;

    private final RestaurantDataHelperMongo restaurantDataHelperMongo;
    private final QueryDisplayHelper queryDisplayHelper;

    public RestaurantMetadataLoaderSpringBootApp(RestaurantDataLoader restaurantDataLoader, RestaurantDataHelperMongo restaurantDataHelperMongo, QueryDisplayHelper queryDisplayHelper) {
        this.restaurantDataLoader = restaurantDataLoader;
        this.restaurantDataHelperMongo = restaurantDataHelperMongo;
        this.queryDisplayHelper = queryDisplayHelper;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantMetadataLoaderSpringBootApp.class, args).close();
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("**************** RESTAURANT APPLICATION ****************");
        System.out.println("Do you wish to load data or query data? Type 'load' to load and 'query' to query");
        String loadOrQueryInput = sc.next();
        if ("load".equalsIgnoreCase(loadOrQueryInput)) {
            System.out.println("Received instruction to LOAD.");
            List<RestaurantDetails> rawData = restaurantDataLoader.loadRawData();
            System.out.format("Restaurant data received of size: %d", rawData.size());
            restaurantDataHelperMongo.transformAndSaveData(rawData);
            System.out.println("\nSuccessfully saved data to MongoDB collection");
        } else if ("query".equalsIgnoreCase(loadOrQueryInput)) {
            System.out.println("Received instruction to QUERY.");
            String action;
            do {
                System.out.println("\n\nSelect one of the following Query operations:");
                System.out.println("Type '1' to: get the list of top 5 restaurant names with full address and rating with results sorted descending order based on rating.");
                System.out.println("Type '2' to: get the list of all restaurant names and their rating and cuisine in the given address and minimum rating");
                System.out.println("Type '3' to: get the count of restaurants in each zipcode for the given cuisine");
                System.out.println("Type '4' to: get the average rating of all restaurants for each cuisine");
                System.out.println("Type '5' to: exit the application");
                action = sc.next();
                switch (action) {
                    case "1":
                        System.out.println("Question 1: User provides a choice of cuisine and zipcode, the query returns a list of top 5 restaurant names \nwith full address and rating with results sorted descending order based on rating.\n");
                        List<RestaurantTop5Details> restaurantsByCuisineAndZipcode = restaurantDataHelperMongo.findRestaurantsByCuisineAndZipcode("Lebanese", "CF243JH");
                        queryDisplayHelper.prettyPrintTop5(restaurantsByCuisineAndZipcode);
                        break;
                    case "2":
                        System.out.println("Question 2: User provides a string to be searched in the address field and a minimum rating. \nThe query returns all restaurant names and cuisine that match the inputs provided along with the match score. \nSort output by descending score.\n");
                        List<RestaurantAddressSearchDetails> restaurantsByAddressAndMinRating = restaurantDataHelperMongo.findRestaurantsByAddressAndMinRating("Bristol", 4.5);
                        queryDisplayHelper.prettyPrintAddressSearch(restaurantsByAddressAndMinRating);
                        break;
                    case "3":
                        System.out.println("Question 3: Give a cuisine, output how many (i.e. count) matching restaurants are there per zip code. \nSort descending the 2 column output (zipcode, count) by the count.\n");
                        List<RestaurantZipcodeCountDetails> restaurantsZipcodeCount = restaurantDataHelperMongo.findCountOfCuisinePerZipcode("Lebanese");
                        queryDisplayHelper.prettyPrintZipcodeCount(restaurantsZipcodeCount);
                        break;
                    case "4":
                        System.out.println("Question 4: Show the average rating per type of food and provide an ascending sorted output (type of food, rating) by rating.\n");
                        List<RestaurantAvgRatingDetails> avgRatingPerCuisine = restaurantDataHelperMongo.findAvgRatingPerCuisine();
                        queryDisplayHelper.prettyPrintAvgRating(avgRatingPerCuisine);
                        break;
                    case "5":
                        System.out.println("Command received to quit. Exiting!!");
                        break;
                    default:
                        System.out.println("Invalid input provided. Try again!!");
                        break;
                }
            } while (!"5".equals(action));
        } else {
            System.out.println("Invalid input provided. Quitting!!");
        }
    }
}
