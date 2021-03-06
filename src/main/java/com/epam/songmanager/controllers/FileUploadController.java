package com.epam.songmanager.controllers;

import com.epam.songmanager.exceptions.StorageFileNotFoundException;
import com.epam.songmanager.model.entity.StorageType;
import com.epam.songmanager.model.resource.ResourceObj;
import com.epam.songmanager.service.interfaces.ResourceObjectService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class FileUploadController {

    @Autowired
    private ResourceObjectService resourceObjService;

    @SneakyThrows
    @GetMapping("/")
    public String listUploadedFiles(@NotNull Model model, @RequestParam(required=false,name ="st") StorageType storageType) {
        if(storageType == null)
            storageType = StorageType.DISK_FILE_SYSTEM;
        model.addAttribute("storage", storageType);
        model.addAttribute("files", resourceObjService.loadAll(storageType));
        return "uploadForm";
    }

    @GetMapping("/{resId}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String resId,
                                              @RequestParam("st") StorageType storageType) throws IOException {
        ResourceObj resourceObj =resourceObjService.getResource(resId);
        Resource res = new ByteArrayResource(resourceObj.read().readAllBytes());
        return ResponseEntity
                .ok()
                .contentLength(res.contentLength())
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + resourceObj.getFileName() + "\"")
                .body(res);
    }

     @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("st") StorageType storageType,
                                   RedirectAttributes redirectAttributes) throws Exception {

         resourceObjService.store(file.getInputStream(),storageType,file.getOriginalFilename());

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/?st="+storageType.toString();
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}