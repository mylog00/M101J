package com.homework;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry
 * @since 01.05.2016
 */
public class FinExam_7 {


    public static void main(String[] args) {
        final MongoClient client = new MongoClient();
        final MongoDatabase database = client.getDatabase("test");
        final Set<Integer> usedImages = new HashSet<>();
        final MongoCollection<Document> albums = database.getCollection("albums");

        for (final Document album : albums.find()) {
            List<Integer> imageIds = (List<Integer>) album.get("images");
            imageIds.forEach(usedImages::add);
        }
        final MongoCollection<Document> images = database.getCollection("images");
        for (final Document image : images.find()) {
            final Integer id = image.getInteger("_id");
            if (!usedImages.contains(id)) {
                images.deleteOne(image);
            }
        }
    }
}
