package com.wymee.backparser.parser_backend_api.classes;

import com.wymee.backparser.parser_backend_api.model.Job;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class Utilitaires {

    public static JSONObject makeMeta(String state) throws JSONException {

        //JSONObject metaHeader = new JSONObject();
        JSONObject meta = new JSONObject();

        meta.append("statusCode", "200");
        meta.append("state", state);

        return meta.append("meta", meta);
    }


    public static JSONObject makeMeta(String state, String message) throws JSONException {

        //JSONObject metaHeader = new JSONObject();
        JSONObject meta = new JSONObject();

        meta.append("statusCode", "200");
        meta.append("state", state);
        meta.append("message", message);

        return meta.append("meta", meta);
    }

    public static Object jSONifyJob(Job job) throws JSONException{
        JSONObject currentObject = new JSONObject();

        currentObject.put("job-title", job.getTitle());
        currentObject.put("job-description", job.getDescription());
        currentObject.put("job-posted-at", job.getJobPostedAt());
        currentObject.put("company-name", job.getCompany());
        currentObject.put("company-webSite", job.getCompanyWebSite());
        currentObject.put("job-location", job.getLocation());
        currentObject.put("job-contrat", job.getJobContrat());
        currentObject.put("begin-date", job.getBeginDate());
        currentObject.put("salary-range", job.getSalaryRange());
        currentObject.put("source", job.getJobContrat());

        return currentObject;
    }

    @Bean
    public static WebMvcConfigurer corsConfigurer(String pathPattern){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(pathPattern).allowedOrigins("http://localhost:8090");
            }
        };
    }

}