package com.wymee.backparser.parser_backend_api.controllers;

import com.wymee.backparser.parser_backend_api.data.JobsDAO;
import com.wymee.backparser.parser_backend_api.model.Job;
import org.apache.catalina.connector.Connector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//

@RequestMapping("/api/matching/")
@RestController
@CrossOrigin
public class MatchingController {

    @Autowired
    private static JobsDAO jobsDAO;


//    @GetMapping("/match")
    @GetMapping("/scrapp")
    public static String match(@RequestParam(name = "data") String data) throws IOException, JSONException {

        //Créer ici la recupération des données à macther
        String scrapped = ScrappingController.scrapping(data);
        if(scrapped.isEmpty()){
            Model model = null;
           scrapped = model.addAttribute("list jobs", jobsDAO.findAll()).toString();
        }

        //TODO Getting MySQL data for matching

        return scrapped;
    }

    public static String matches(String candidat) throws JSONException {
        JSONObject candidate = new JSONObject(candidat);

        // TODO getting SQLData and matching it to return it has a json Object to String!

        return candidate.toString();
    }
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedQueryChars", "|{}[]");
            }
        });
        return factory;
    }
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/match").allowedOrigins("http://localhost:8080").maxAge(3000);
                registry.addMapping("/scrapp").allowedOrigins("http://localhost:8090").maxAge(3000);
            }
        };
    }

    @GetMapping("/scrap")
    public static String Matching(@RequestParam(name = "data") String data) throws JSONException, IOException {
        List<ArrayList<Job>> response = new ArrayList<>();
        JSONArray allData;
        System.out.println("data"+data);
        try {
            allData = new JSONArray(data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < allData.length(); i++) {
        JSONObject obj = allData.getJSONObject(i);
            System.out.println("data" + i + " = "  + obj.toString());
            try {
                response.add(ScrappingController.doScrapping(obj.getString("job"), obj.getString("location")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return response.toString();

    }

} 
