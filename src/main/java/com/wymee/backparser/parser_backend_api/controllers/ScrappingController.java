package com.wymee.backparser.parser_backend_api.controllers;

import com.google.gson.Gson;
import com.wymee.backparser.parser_backend_api.classes.Utilitaires;
import com.wymee.backparser.parser_backend_api.model.Job;
import com.wymee.backparser.parser_backend_api.repository.JobRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/api")
public class ScrappingController {

    @Autowired
    private static JobRepository jobRepository;


    public static ArrayList<Job> jobsList = new ArrayList<>();
    public static JSONObject listOfJobs = new JSONObject();

    @GetMapping("/scrapp")
    //@EventListener(ApplicationReadyEvent.class)
    public static String scrapping(@RequestParam(name = "data") String data) throws IOException, JSONException {

        JSONObject response = new JSONObject();
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
              System.out.println("jobs.toString()) = " + jobs.toString());
              doScrapping(jobs.toString().replace(" ", "+"), "");
          //} else {
              //response.append("meta", Utilitaires.makeMeta("Error", "Bad credential, no data submitted"));
          //}
      }catch (org.json.JSONException e){
          e.printStackTrace();
      }
        System.out.println("Jobs ==> " + jobsList.toString());

        return new Gson().toJson(jobsList);  //response.toString();

       /* if (!data.isEmpty()){
            return doScrapping(data);
        }else{
            return "Error ! \n Bad Credentials \n No data submitted.";
        }*/
    }

    /*public static String doScrapping(String data){
        JSONArray array =  new JSONArray(data);
        for (int i = 0; i < array.length(); i++) {

            JSONObject obj = (JSONObject) array.get(i);

            String job = obj.getString("job");
            String location = obj.getString("location");

            final String urlIndeed = "https://www.indeed.com/jobs?q="+job+"&l="+location;
            final String urlLinkup = "https://www.linkup.com/search/results/"+ job +"-jobs-in-"+ location;
            final String urlPoleEmploi = "https://candidat.pole-emploi.fr/offres/recherche?" + "motsCles="+ job +
                    "&offresPartenaires=true&range=0-19&rayon=10&tri=0";

            scrappIndeed(urlIndeed);
            //scrappLinkup(urlLinkup);
            scrappPoleEmploi(urlPoleEmploi);
        }

        JSONObject back = new JSONObject();

        if(listOfJobs.isEmpty()){
            back.append("meta", Utilitaires.makeMeta("error", "No job found"));
        }else{

            back.append("meta", Utilitaires.makeMeta("success", "List of scrapped jobs"));
            back.append("data", listOfJobs);
        }

        return back.toString();
    }*/

    /*public static ArrayList<Job> doScrapping(String job, String location) throws IOException {

        final String urlIndeed = "https://www.indeed.com/jobs?q="+job+"&l="+location;
        final String urlLinkup = "https://www.linkup.com/search/results/"+ job +"-jobs-in-"+ location;
        final String urlPoleEmploi = "https://candidat.pole-emploi.fr/offres/recherche?" + "motsCles="+ job +
                                     "&offresPartenaires=true&range=0-19&rayon=10&tri=0";
        final String urlLinkedIn = "https://www.linkedin.com/jobs/search/?keywords="+ job +"&location="+ location;

        try{
            scrappIndeed(urlIndeed);
            scrappLinkup(urlLinkup);
            scrappPoleEmploi(urlPoleEmploi);
            //scrappLinkedIn(urlLinkedIn);

            if(!jobsList.isEmpty()){
                return jobsList;
            }else {
                Iterable<Job> list = jobRepository.findAll();
                for (Job job1 : list) {
                    if((job1.getTitle().compareToIgnoreCase(job) >= 0) && ((job1.getLocation().compareToIgnoreCase(location) >= 0) ||
                            (job1.getDescription().compareToIgnoreCase(location) >= 0))){
                        jobsList.add(job1);
                    }
                }
            }
        }catch (UnknownHostException ex ){
            System.out.println("Impossible de joindre l'hôte : " + ex.getMessage() + ". Vérifiez votre connexion.");
        }catch (org.json.JSONException jsonException){
            jsonException.printStackTrace();
        }

        return jobsList;
    }*/

    public static ArrayList<Job> doScrapping(String job, String location) throws IOException {

        final String urlIndeed = "https://www.indeed.com/jobs?q="+job+"&l="+location;
        final String urlLinkup = "https://www.linkup.com/search/results/"+ job +"-jobs-in-"+ location;
        final String urlPoleEmploi = "https://candidat.pole-emploi.fr/offres/recherche?motsCles="+job+"&offresPartenaires=true&rayon=10&tri=0";
        final String urlLinkedIn = "https://www.linkedin.com/jobs/search/?keywords="+ job +"&location="+ location;

        try{
            scrappIndeed(urlIndeed);
            scrappLinkup(urlLinkup);
            scrappPoleEmploi(urlPoleEmploi);
            scrappLinkedIn(urlLinkedIn);

            /*if(!jobsList.isEmpty()){
                return jobsList;
            }else {
                Iterable<Job> list = jobRepository.findAll();
                for (Job job1 : list) {
                    if((job1.getTitle().compareToIgnoreCase(job) >= 0) && ((job1.getLocation().compareToIgnoreCase(location) >= 0) ||
                            (job1.getDescription().compareToIgnoreCase(location) >= 0))){
                        jobsList.add(job1);
                    }
                }
            }*/
        } catch (org.json.JSONException jsonException){
            jsonException.printStackTrace();
        }

        return jobsList;
    }

    public static void scrappIndeed(String url) throws JSONException{
        //Document document = null;
        int i = 0;
        try {
            Document document = Jsoup.connect(url.replace(" ", "%2C%20")).get();

            if(document != null) {
                for (Element subDoc : document.select("div.job_seen_beacon")) {

                    String jobTitle = subDoc.select("h2.jobTitle").text();
                    String companyName = subDoc.select("span.companyName").text();
                    String companyLocation = subDoc.select("div.companyLocation").text();
                    String companyLink = subDoc.select("a.turnstileLink.companyOverviewLink").text();
                    String jobDescription = subDoc.select("div.job-snippet").text();
                    String jobPostedAt = subDoc.select("span.date").text();

                    Job aJob = new Job(jobTitle, jobDescription, companyName, companyLink,
                            companyLocation, "", "", jobPostedAt, "Indeed");

                    jobsList.add(aJob);

                   listOfJobs.append("job", Utilitaires.jSONifyJob(aJob));

                    i++;
                }
            }else{
                System.out.println("No data got from "+ url);
            }
        }catch (HttpStatusException ex){
            System.out.println("Erreur "+ ex.getStatusCode() +" Probleme de connexion à Indeed dûe à : "+ex.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void scrappLinkup(String url) throws IOException, org.json.JSONException {
        //Document document2 = null;

        try{
            Document document2 = Jsoup.connect(url.replace(" ", "-")).get();

            if (document2 != null){
                for ( Element element : document2.select("div.row.job-listing")) {

                    String jobTitle = element.select("a.organic-link.search-result-link").text();
                    String companyName = element.select("p.f-s-14 span.semi-bold").text();
                    String jobLocation = element.select("span.vertical-bar.semi-bold").text();
                    String jobPostedAt = element.select("div.f-s-14 span.semi-bold").text();
                    String jobDescription = element.select("div.col.s12 p").text();

                    Job aJob = new Job(jobTitle, jobDescription, companyName, "",
                            jobLocation, "", "", jobPostedAt, "Linkup");

                    jobsList.add(aJob);

                   listOfJobs.append("job", Utilitaires.jSONifyJob(aJob));

                }
            }else {
                System.out.println("No data got from : "+url);
            }

        }catch (HttpStatusException ex){
            System.out.println("Erreur "+ ex.getStatusCode() +" Probleme de connexion à Indeed dûe à : " +
                    ""+ex.getMessage());
        }
    }

    public static void scrappPoleEmploi(String url) throws JSONException{
        //Document document3 = null;
        try{
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

    public static void scrappLinkedIn(String url) throws JSONException{
        try{
            Document document = Jsoup.connect(url.replace(" ", "%20")).get();

            if(document != null){
                 Elements element = document.select("div.job-search-results.display-flex.flex-column");

                    for(Element current : element.select("ul.jobs-search-results__list.list-style-none")) {

                            String jobTitle = current.select("a.disabled.ember-view.job-card-container__link.job-card-list__title").text();
                            String companyName = current.select("a.job-card-container__link.job-card-container__company-name.ember-view").text();
                            String jobLocation = " ";
                            String jobPostedAt = current.select("ul.job-card-list__footer-wrapper.job-card-container__footer-wrapper.flex-shrink-zero.display-flex.t-sans.t-12.t-black--light.t-normal.t-roman li.job-card-container__listed-time.job-card-container__footer-item").text();
                            String jobDescription = current.select("div.media-body p.description").text();
                            String jobContrat = current.select("div.media-body p.contrat").text();

                            Job taff = new Job(jobTitle, jobDescription, companyName, " ",
                                    jobLocation, " ", " ", jobPostedAt, jobContrat, "Pole Emploi");

                            jobsList.add(taff);

                           listOfJobs.append("job", Utilitaires.jSONifyJob(taff));

                    }

            }else {
                System.out.println("No data got from "+ url);
            }

        }catch (HttpStatusException ex){
            System.out.println("Erreur "+ ex.getStatusCode() +" Probleme de connexion à LinkedIn dûe à : "+ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/match").allowedOrigins("http://localhost:8080");
            }
        };
    }*/
}
