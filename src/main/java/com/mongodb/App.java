package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(App.class, "/");
        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Spark.get("/", (request, response) -> {
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
    }
}
