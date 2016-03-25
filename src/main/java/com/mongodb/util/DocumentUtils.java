package com.mongodb.util;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

import java.io.StringWriter;

/**
 * @author Dmitry
 * @since 24.03.2016
 */
public final class DocumentUtils {
    /**
     * Util class private constructor
     */
    private DocumentUtils() {
    }

    /**
     * Print MongoDB document as JSON.
     *
     * @param document MongoDB document
     */
    public static void printJson(Document document) {
        final JsonWriter jsonWriter = new JsonWriter(
                new StringWriter(),
                new JsonWriterSettings(JsonMode.SHELL, true)
        );
        final EncoderContext encoderContext = EncoderContext.builder()
                .isEncodingCollectibleDocument(true)
                .build();
        new DocumentCodec().encode(jsonWriter, document, encoderContext);
        System.out.println(jsonWriter.getWriter());
        System.out.flush();
    }
}
