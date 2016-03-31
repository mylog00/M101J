package com.homework;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author Dmitry
 * @since 31.03.2016
 */
public class Homework3_1 {
    /**
     * <p>
     * Write a program in the language of your choice that
     * will remove the lowest homework score for each student.
     * Since there is a single document for each student
     * containing an array of scores, you will need to update
     * the scores array and remove the homework.
     * </p>
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final MongoClient client = new MongoClient();
        final MongoDatabase database = client.getDatabase("school");
        final MongoCollection<Document> students = database.getCollection("students");
        final FindIterable<Document> documents = students.find();
        for (final Document studentDocument : documents) {
            @SuppressWarnings("unchecked")
            final List<Document> scoresList = studentDocument.get("scores", List.class);
            double minScore = Double.MAX_VALUE;
            int minScoreIndex = -1;
            for (int i = 0; i < scoresList.size(); i++) {
                final Document score = scoresList.get(i);
                if (score.getString("type").equals("homework") &&
                        score.getDouble("score") < minScore) {
                    minScore = score.getDouble("score");
                    minScoreIndex = i;
                }
            }
            if (minScoreIndex >= 0) {
                final Bson filter = Filters.eq("_id", studentDocument.get("_id"));
                final Bson updater = Updates.pull("scores", scoresList.get(minScoreIndex));
                students.updateOne(filter, updater);
            }
        }
    }
}
