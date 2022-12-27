package com.wymee.backparser.parser_backend_api.controllers;

import com.wymee.backparser.parser_backend_api.classes.Utilitaires;
import okhttp3.*;
import okhttp3.RequestBody;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@RestController
@RequestMapping("/api/parsing")
@CrossOrigin
public class ParsingController {

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public static String parse(@RequestParam(name = "resume") MultipartFile file) throws IOException, JSONException, InterruptedException {
        //TODO

        if (file.isEmpty()) {
            return "Bad credentials";
        } else {
            //String url = "https://harveywymee/prototype/parse";
            //Mettre ici tout le code de construucgtion de la requête
            String[] name = Objects.requireNonNull(file.getOriginalFilename()).split(".");

            //if(name[name.length-1].equalsIgnoreCase("pdf")){
            JSONObject parsingResults = getParsingData("http://127.0.0.1:3333/api/affindas", file);

            String candidateName = getCandidateNameFromParsingData(parsingResults);
            String candidateProfession = getCandidateProfessionFromParsingData(parsingResults);
            String candidateLocation = getCandidateLocationFromParsingData(parsingResults);
            //TODO test de reception correct des data de parsing
            JSONObject response;
            //assert parsingResults != null;
            if (parsingResults != null) {
                response = Utilitaires.makeMeta("Error");
            } else {
                response = Utilitaires.makeMeta("Success");
            }


            response.append("parsed_resume", parsingResults);
            response.append("matched_jobs", getMatchingData(candidateProfession, candidateLocation));

            return response.toString();
        //}else{
           // return "Bad credential \n \t * Unsupported file extension";
        //}

    }
}

    private static String getCandidateLocationFromParsingData(JSONObject parsingResults) throws JSONException {
        return parsingResults.getJSONObject("data").getJSONObject("location").getString("country");
    }

    private static String getCandidateProfessionFromParsingData(JSONObject parsingResults) throws JSONException {
        return parsingResults.getJSONObject("data").getString("profession");
    }

    private static String getCandidateNameFromParsingData(JSONObject parsingResults) throws JSONException {
        return parsingResults.getJSONObject("data").getJSONObject("name").getString("raw").replace(" ", "_");
    }

    private static String getMatchingData(String profession, String location) throws JSONException, IOException {

        return String.valueOf(ScrappingController.doScrapping(profession.replace(" ", "%2C%20"), location));
    }

    public static JSONObject getParsingData(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpPost request = new HttpPost(url);

            request.addHeader("content-type", "application/json");
            request.addHeader("Content-Type", "multipart/form-data");


            CloseableHttpResponse response = httpClient.execute(request);

            try{
                /*System.out.println("\n response.getProtocolVersion() = " + response.getProtocolVersion());
                System.out.println("\n response.getStatusLine().getStatusCode() = " + response.getStatusLine().getStatusCode());
                System.out.println("\n response.getStatusLine().getReasonPhrase() = " + response.getStatusLine().getReasonPhrase());
                System.out.println("\n response.getStatusLine().toString() = " + response.getStatusLine().toString());*/

                HttpEntity entity = response.getEntity();
                if (entity != null){
                    return new JSONObject(EntityUtils.toString(entity));
                    //System.out.println(" content "+EntityUtils.toString(entity));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        }finally {
            httpClient.close();
        }
        return null;
    }

    public static JSONObject getParsingData(String url, MultipartFile file) throws IOException, InterruptedException, JSONException {

        /*HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "multipart/form-data")
                .header("file",file.getOriginalFilename())
                .method("POST", HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("responsebody"+response.body());*/
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).
                addFormDataPart("file", file.getName(),RequestBody.create(okhttp3.MediaType.parse("file/*"), String.valueOf(file.getInputStream()))).build();

        Request request = new Request.Builder().
                url(url).addHeader("Content-Type", "multipart/form-data").post(requestBody).build();

        Response response = client.newCall(request).execute();

        JSONObject myjson = Utilitaires.makeMeta("Success");
        myjson.append("parsing",response.body());
        return myjson;

    }
    

}
