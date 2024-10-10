package com.sepay.backend.S3.controller;


import com.sepay.backend.S3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam MultipartFile profileImg,  Model model) {
        System.out.println("한글 출력");
        try {
            String fileUrl = s3Service.uploadFile(profileImg, profileImg.getOriginalFilename());
            System.out.println("fileUrl:" + fileUrl);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "File upload failed!");
        }
        return "uploadStatus";
    }
}