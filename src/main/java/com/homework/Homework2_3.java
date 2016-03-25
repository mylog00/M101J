package com.homework;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @author Dmitry
 * @since 25.03.2016
 */
public class Homework2_3 {
    /**
     * <p>
     * Write a program in the language of your choice that will remove
     * the grade of type "homework" with the lowest score for each student
     * from the dataset that you imported in the previous homework.
     * Since each document is one grade, it should remove one document
     * per student.
     * </p>
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final MongoClient client = new MongoClient();
        final MongoDatabase database = client.getDatabase("students");
        final MongoCollection<Document> grades = database.getCollection("grades");

        final Bson filter = Filters.eq("type", "homework");
        final Bson sort = Sorts.orderBy(Sorts.ascending("student_id"), Sorts.ascending("score"));

        final FindIterable<Document> documents = grades.find(filter).sort(sort);

        final MongoCursor<Document> cursor = documents.iterator();
        Document previous = cursor.next();
        while (cursor.hasNext()) {
            Document next = cursor.next();
            if (!previous.getInteger("student_id").equals(next.getInteger("student_id"))) {
                grades.deleteOne(previous);
                previous = next;
            }
        }
        grades.deleteOne(previous);
    }
}
