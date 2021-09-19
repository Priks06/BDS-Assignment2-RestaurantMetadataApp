package com.bits.bds.restaurant.metadata.app.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RestaurantDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantDataLoader.class);

    public List<RestaurantDetails> loadRawData() {
        List<RestaurantDetails> restaurantDetails = new ArrayList<>();
        try {
            String resourceLocation = "classpath:res.json";
            File file = ResourceUtils.getFile(resourceLocation);
            InputStream inputStream = new FileInputStream(file);
            byte[] byteData = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(byteData, StandardCharsets.UTF_8);
            List<String> split = Arrays.asList(data.split("\n"));
            logger.info("Split stuff: {}", split.get(0));
            ObjectMapper mapper = new ObjectMapper();
            for (String record : split) {
                logger.info("Mapping: {}", record);
                restaurantDetails.add(mapper.readValue(record, RestaurantDetails.class));
            }
        } catch (Exception e) {
            logger.error("Failed to read input file. ", e);
        }
        return restaurantDetails;
    }
}
