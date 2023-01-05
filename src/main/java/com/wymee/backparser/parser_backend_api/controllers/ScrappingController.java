package com.wymee.backparser.parser_backend_api.controllers;

import com.google.gson.Gson;
import com.wymee.backparser.parser_backend_api.classes.Utilitaires;
import com.wymee.backparser.parser_backend_api.model.Job;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/api")
public class ScrappingController {


    public static ArrayList<Job> jobsList = new ArrayList<>();
    public static JSONObject listOfJobs = new JSONObject();

    @GetMapping("/scrap-old")
    //@EventListener(ApplicationReadyEvent.class)
    public static String scrapping(@RequestParam(name = "data") String data) throws JSONException {

        JSONArray allData = new JSONArray(data);

        StringBuilder jobs = new StringBuilder();
        try {
            //if (!allData.isEmpty()) {
            for (int i = 0; i < allData.length(); i++) {

                JSONObject obj = (JSONObject) allData.get(i);
                //doScrapping(obj.getString("job"), obj.getString("location"));
                jobs.append(obj.getString("job")).append(",");
            }
            jobs.deleteCharAt(jobs.length()-1);
            System.out.println("jobs.toString()) = " + jobs);
            doScrapping(jobs.toString().replace(" ", "+"));

        }catch (org.json.JSONException e){
            e.printStackTrace();
        }
        System.out.println("Jobs ==> " + jobsList.toString());

        return new Gson().toJson(jobsList);  //response.toString();

    }

    public static void doScrapping(String job) {

        final String urlPoleEmploi = "https://candidat.pole-emploi.fr/offres/recherche?motsCles="+job+"&offresPartenaires=true&rayon=10&tri=0";
//        final String urlLinkedIn = "https://www.linkedin.com/jobs/search/?keywords="+ job +"&location="+ location;

        try{
            scrappPoleEmploi(urlPoleEmploi);
//            scrappLinkedIn(urlLinkedIn);

        } catch (org.json.JSONException jsonException){
            jsonException.printStackTrace();
        }

    }

    public static void scrappPoleEmploi(String url) throws JSONException{
        //Document document3 = null;
        try{

            url = url.replace("é","e");
            url = url.replace("ô","o");
            url = url.replace("à","a");
            Document document3 = Jsoup.connect(url.replace(" ", "+")).get();
            System.out.println("url = " + url);
            if(document3 != null){
                for ( Element element : document3.select("div.zone-resultats ul.result-list.list-unstyled")) {

                    if(element.children().size() > 0) {

                        for(Element element1 : element.children()) {

                            String jobTitle = element1.select("h2.t4.media-heading").text();
                            String companyName = element1.select("div.media-body p.subtext").text();
                            String jobLocation = " ";
                            String jobPostedAt = element1.select("div.media-body p.date").text();
                            String jobDescription = element1.select("div.media-body p.description").text();
                            String jobContrat = element1.select("div.media-body p.contrat").text();

                            Job taff = new Job(jobTitle, jobDescription, companyName, " ",
                                    jobLocation, " ", " ", jobPostedAt, jobContrat, "Pole Emploi");

                            jobsList.add(taff);

                            listOfJobs.append("job", Utilitaires.jSONifyJob(taff));
                        }
                    }
                }
            }else {
                System.out.println("No data got from "+ url);
            }

        }catch (HttpStatusException ex){
            System.out.println("Erreur "+ ex.getStatusCode() +" Probleme de connexion à Indeed dûe à : "+ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}