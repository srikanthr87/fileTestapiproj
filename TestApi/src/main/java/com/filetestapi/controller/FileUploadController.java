package com.filetestapi.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.filetestapi.model.FileMetaData;

@Controller
public class FileUploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://uploads";

    @RequestMapping("/")
    public String index() {
        return "uploadFile";
    }

    @RequestMapping("/fileName/{name:.+}/")
    @ResponseBody
    public FileMetaData getFileDetails(@PathVariable("name") String name){
    	
    	System.out.println("fileName is called " + name);
    	File file = new File(UPLOADED_FOLDER + File.separator +  name);
    	FileMetaData details = new FileMetaData();
    	if(file.exists()){
    		details.setName(file.getName());
    		details.setSize(file.length());
    		details.setAbsolutePath(file.getAbsolutePath());
    		details.setExists(file.exists());
    	}
    	else{
    		details.setExists(false);
    	}
    	return details;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload..");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it to uploaded folder
            byte[] bytes = file.getBytes();
            Path location = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(location, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "File is uploaded successfully at '" + UPLOADED_FOLDER + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}