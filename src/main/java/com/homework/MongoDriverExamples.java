package com.homework;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.util.DocumentUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Dmitry
 * @since 24.03.2016
 */
public final class MongoDriverExamples {
    /**
     * Mongo driver example
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final MongoClient client = new MongoClient();
        final MongoDatabase courseDb = client.getDatabase("course");
        final MongoCollection<Document> collection = courseDb.getCollection("insertTest");
        collection.drop();

        final Document smith = new Document()
                .append("name", "Smith")
                .append("age", 30)
                .append("profession", "programmer");

        final Document jones = new Document()
                .append("name", "Jones")
                .append("age", 24)
                .append("profession", "hacker");

        //INSERT*************************************
        collection.insertMany(Arrays.asList(smith, jones));

        Stream.of(smith, jones).forEach(DocumentUtils::printJson);

        //FIND*************************************
        //equ findOne()
        collection.find().first();
        //find all with into
        final List<Document> all = collection.find().into(Collections.emptyList());
        for (Document doc : all) {
            DocumentUtils.printJson(doc);
        }
        //find all with iterator
        final MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                final Document nextDoc = cursor.next();
                DocumentUtils.printJson(nextDoc);
            }
        } finally {
            cursor.close();
        }
        //Count
        final long count = collection.count();

        //final Bson filter = new Document("name", "John")
        //.append("age", new Document("$gt", 10));
        final Bson filter = Filters.and(Filters.eq("name", "John"), Filters.gt("age", 10));

        //projection - exclude document columns match filter
        //show only "x" and "y" property
        final Bson projection = Projections.fields(Projections.include("x", "y"), Projections.excludeId());
        //sorting
        final Bson sort = Sorts.orderBy(Sorts.ascending("x"), Sorts.descending("y"));
        final FindIterable<Document> projectionRes = collection.find()
                .projection(projection)
                .sort(sort)
                .skip(2)
                .limit(3);

        //UPDATE*************************************
        collection.replaceOne(Filters.eq("x", 1), new Document("x", 1).append("updated", true));
        collection.updateOne(Filters.eq("x", 1), new Document("$set", new Document("x", 20).append("upadted", true)));

        final Bson update = Updates.combine(Updates.set("x", 20), Updates.set("upadted", true));
        collection.updateOne(Filters.eq("x", 1), update, new UpdateOptions().upsert(true));
        collection.updateMany(Filters.eq("x", 1), Updates.inc("x", 1));

        //DELETE*************************************
        collection.deleteOne(Filters.gt("x", 44));
        collection.deleteMany(Filters.gt("x", 24));

        //AGGREGATION
        List<Bson> pipeline = Arrays.asList(
                //first stage
                Aggregates.group("$x", Accumulators.sum("sum", "$y")),
                //second stage
                Aggregates.match(Filters.gt("$sum", 10))
        );
        AggregateIterable<Document> aggregate = collection.aggregate(pipeline);

        //PARSE DOCUMENT FROM JSON
        Document parsedJson = Document.parse("$group:{_id:\"$state\", population:{$sum:\"$pop\"}}");
    }
}
