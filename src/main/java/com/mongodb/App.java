package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application
 */
public class App {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(App.class, "/");
        // Sets how errors will appear.
        // During web page *product* TemplateExceptionHandler.RETHROW_HANDLER is better.
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

        Spark.get("/", (request, response) -> {
            try {
                final Template template = configuration.getTemplate("fruitPicker.ftl");

                final Map<String, Object> fruitMap = new HashMap<String, Object>() {{
                    put("fruits", Arrays.asList("apple", "orange", "peach"));
                }};
                final StringWriter writer = new StringWriter();
                template.process(fruitMap, writer);
                return writer;
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                return null;
            }
        });

        Spark.post("/favorite_fruit", (request, response) -> {
            final String fruit = request.queryParams("fruit");
            if (fruit == null)
                return "Why do not your pick one?";
            return "Your favorite fruit is " + fruit;
        });

        Spark.get("/hello", (request, response) -> {
            final StringWriter writer = new StringWriter();
            try {
                final Template template = configuration.getTemplate("hello.ftl");

                final Map<String, Object> helloMap = new HashMap<String, Object>() {{
                    put("name", "Freemarker");
                }};
                template.process(helloMap, writer);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
            }
            return writer;
        });

        Spark.get("/echo/:thing", (request, response) -> request.params(":thing"));
    }
}
