package com.wymee.backparser.parser_backend_api.controllers;

import com.wymee.backparser.parser_backend_api.data.JobsDAO;
import com.wymee.backparser.parser_backend_api.model.Job;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TestController {

    @Autowired
    private JobsDAO jobsDAO;

    @Autowired
    ServletContext context;
    //private HttpServletRequest request;

    @GetMapping("/test/json")
    public static String testing() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("name", "Pierre");
        object.put("name1", "Dan");
        object.put("name2", "Brel");
        object.put("name3", "Bénaja");
        object.put("name4", "Harvey");

        return object.toString();
    }

    @GetMapping("/test/jobs")
    public String index(Model model){
        model.addAttribute("list jobs", jobsDAO.findAll());

        return model.toString();
    }

    @PostMapping( value = "/test/job", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public static String store(@RequestParam("resume")MultipartFile file) throws IOException {
       /* String destination = "/filestest/" + file.getOriginalFilename();
        File fichier = new File(destination);
        file.transferTo(fichier);

        // Job job = new Job(newJob.);
        Path source = Paths.get(String.valueOf(file));
        Path target = Paths.get("src/main/resources/filestest");
        //Path target = Paths.get("C:\\Users\\Admin KBIM\\Documents\\SpringBootProjects\\parser_backend_api\\src\\main\\resources\\filestest");

        Files.move(source.toAbsolutePath(), target.resolveSibling("myfile1.pdf"));

        //file.transferTo(Paths.get("src/main/resources/filestest"));
        return file.getOriginalFilename() +"."+file.getName();*/
       if (!file.isEmpty()){

               //String uploadsDir = "/webResources/uploads/";
               //String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
                String realPathToUploads = "C:\\Users\\Admin KBIM\\Documents\\SpringBootProjects\\parser_backend_api\\src\\webResources\\uploads\\";
               if (!new File(realPathToUploads).exists()) {
                   new File(realPathToUploads).mkdir();
                   //System.out.println("On est passé là !");
               } /*else {
                   return "File path already exists";
               }*/

               System.out.println("realPathToUploads = " + realPathToUploads);

               String orgName = file.getOriginalFilename();
               String filePath = realPathToUploads + orgName;
               File dest = new File(filePath);
               file.transferTo(dest);


       }else {
           return "No file received!";
       }
        return "Saved successfully !";
    }





}
