package com.bits.bds.restaurant.metadata.app.boot;

import com.bits.bds.restaurant.metadata.app.data.RestaurantAddressSearchDetails;
import com.bits.bds.restaurant.metadata.app.data.RestaurantAvgRatingDetails;
import com.bits.bds.restaurant.metadata.app.data.RestaurantTop5Details;
import com.bits.bds.restaurant.metadata.app.data.RestaurantZipcodeCountDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryDisplayHelper {

    public void prettyPrintTop5(List<RestaurantTop5Details> restaurants) {
        System.out.println("|---------------------------------|---------------------------------|-----------|");
        System.out.format("| %-32s| %-32s| %-10s|%n", "NAME", "ADDRESS", "RATING");
        System.out.println("|---------------------------------|---------------------------------|-----------|");
        for (RestaurantTop5Details restaurant : restaurants) {
            System.out.format("| %-32s| %-32s| %-10.2f|%n", restaurant.getName(), restaurant.getAddress(), restaurant.getRating());
        }
        System.out.println("|---------------------------------|---------------------------------|-----------|");
    }

    public void prettyPrintAddressSearch(List<RestaurantAddressSearchDetails> restaurants) {
        System.out.println("|---------------------------------|---------------------------------|-----------|");
        System.out.format("| %-32s| %-32s| %-10s|%n", "NAME", "CUISINE", "RATING");
        System.out.println("|---------------------------------|---------------------------------|-----------|");
        for (RestaurantAddressSearchDetails restaurant : restaurants) {
            System.out.format("| %-32s| %-32s| %-10.2f|%n", restaurant.getName(), restaurant.getCuisine(), restaurant.getRating());
        }
        System.out.println("|---------------------------------|---------------------------------|-----------|");
    }

    public void prettyPrintZipcodeCount(List<RestaurantZipcodeCountDetails> restaurants) {
        System.out.println("|-----------|-------|");
        System.out.format("| %-10s| %-5s |%n", "ZIPCODE", "COUNT");
        System.out.println("|-----------|-------|");
        for (RestaurantZipcodeCountDetails record : restaurants) {
            System.out.format("| %-10s| %-5d|%n", record.getZipcode(), record.getCount());
        }
        System.out.println("|-----------|-------|");
    }

    public void prettyPrintAvgRating(List<RestaurantAvgRatingDetails> cuisineDetails) {
        System.out.println("|----------------|------------|");
        System.out.format("| %-15s| %-10s |%n", "CUISINE", "AVG RATING");
        System.out.println("|----------------|------------|");
        for (RestaurantAvgRatingDetails record : cuisineDetails) {
            System.out.format("| %-15s| %-11.2f|%n", record.getCuisine(), record.getAvgRating());
        }
        System.out.println("|----------------|------------|");
    }
}
