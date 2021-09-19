package com.bits.bds.restaurant.metadata.app.mongo;

import com.bits.bds.restaurant.metadata.app.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantDataHelperMongo {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantDataHelperMongo.class);

    private final RestaurantRepository restaurantRepository;

    private final MongoTemplate mongoTemplate;

    public RestaurantDataHelperMongo(RestaurantRepository restaurantRepository, MongoTemplate mongoTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * This method transforms the raw data first, filters our records who don't have a rating, filter our duplicates,
     * ignore the URL field, combine outcode + postcode into a single field zipcode,
     * combine address and address line 2 into a single field and finally load the transformed data into MongoDB
     * @param rawData Raw data read which needs to be transformed and loaded into MongoDB
     */
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

    /**
     * This method returns the list of top 5 restaurants sorted in descending order of their rating
     * @param cuisine Filter by cuisine
     * @param zipcode Filter by zipcode
     * @return returns a list of top 5 restaurant names with full address and rating
     * with results sorted descending order based on rating
     */
    public List<RestaurantTop5Details> findRestaurantsByCuisineAndZipcode(String cuisine, String zipcode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("cuisine").is(cuisine));
        query.addCriteria(Criteria.where("zipcode").is(zipcode));
        query.with(Sort.by(Sort.Direction.DESC, "rating"));
        query.limit(5);
        query.fields().include("name", "address", "rating").exclude("recordId");
        List<RestaurantEntity> restaurantEntities = mongoTemplate.find(query, RestaurantEntity.class);
        logger.info("Found {} restaurants for the given cuisine and zipcode.", restaurantEntities.size());
        return restaurantEntities.stream()
                .map(entity -> new RestaurantTop5Details(entity.getName(), entity.getAddress(), entity.getRating()))
                .collect(Collectors.toList());
    }

    /**
     * This method returns the list of restaurants in the given address with the given minimum rating
     * @param address search query for address
     * @param minRating minimum rating of restaurant
     * @return all restaurant names and cuisine that match the inputs provided along with the rating sorted by descending score.
     */
    public List<RestaurantAddressSearchDetails> findRestaurantsByAddressAndMinRating(String address, double minRating) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").regex(".*" + address + ".*"));
        query.addCriteria(Criteria.where("rating").gte(minRating));
        query.with(Sort.by(Sort.Direction.DESC, "rating"));
        query.fields().include("name", "cuisine", "rating").exclude("recordId");
        List<RestaurantEntity> restaurantEntities = mongoTemplate.find(query, RestaurantEntity.class);
        logger.info("Found {} restaurants for the given address and minimum rating.", restaurantEntities.size());
        return restaurantEntities.stream()
                .map(entity -> new RestaurantAddressSearchDetails(entity.getName(), entity.getCuisine(), entity.getRating()))
                .collect(Collectors.toList());

    }

    /**
     * This method returns the count of all restaurants in each zipcode for the given cuisine
     * @param cuisine filter by cuisine
     * @return the count of matching restaurants per zipcode
     */
    public List<RestaurantZipcodeCountDetails> findCountOfCuisinePerZipcode(String cuisine) {
        MatchOperation filterByCuisine = Aggregation.match(Criteria.where("cuisine").is(cuisine));
        GroupOperation groupByZipcode = Aggregation.group("zipcode").count().as("count");
        SortOperation sortByCount = new SortOperation(Sort.by(Sort.Direction.DESC, "count"));
        Aggregation aggregation = Aggregation.newAggregation(
                filterByCuisine,
                groupByZipcode,
                sortByCount
        );
        List<RestaurantZipcodeCountEntity> restaurantEntities = mongoTemplate.aggregate(
                aggregation, "restaurant_details", RestaurantZipcodeCountEntity.class).getMappedResults();
        logger.info("Found {} restaurants of the given cuisine per zipcode.", restaurantEntities.size());
        return restaurantEntities.stream()
                .map(entity -> new RestaurantZipcodeCountDetails(entity.getZipcode(), entity.getCount()))
                .collect(Collectors.toList());
    }

    /**
     * This method returns the average rating of restaurants for each cuisine
     * @return the average rating per type of food sorted in ascending order of average rating
     */
    public List<RestaurantAvgRatingDetails> findAvgRatingPerCuisine() {
        GroupOperation groupByCuisine = Aggregation.group("cuisine").avg("rating").as("avgRating");
        SortOperation sortByRating = new SortOperation(Sort.by(Sort.Direction.ASC, "avgRating"));
        Aggregation aggregation = Aggregation.newAggregation(
                groupByCuisine,
                sortByRating
        );
        List<RestaurantAvgRatingEntity> restaurantEntities = mongoTemplate.aggregate(
                aggregation, "restaurant_details", RestaurantAvgRatingEntity.class).getMappedResults();
        logger.info("Found {} cuisines.", restaurantEntities.size());
        return restaurantEntities.stream()
                .map(entity -> new RestaurantAvgRatingDetails(entity.getCuisine(), entity.getAvgRating()))
                .collect(Collectors.toList());
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
