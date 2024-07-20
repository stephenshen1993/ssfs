package com.stephenshen.ssfs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

import static com.stephenshen.ssfs.HttpSyncer.X_FILE_NAME;

/**
 * file download and upload controller.
 * @author stephenshen
 * @date 2024/7/20 15:45:16
 */
@RestController
public class FileController {

    @Value("${ssfs.path}")
    private String uploadPath;

    @Value("${ssfs.backupUrl}")
    private String backupUrl;

    @Autowired
    private HttpSyncer httpSyncer;

    @SneakyThrows
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request) {
        boolean needSync = false;
        String fileName = request.getHeader(X_FILE_NAME);
        if (fileName == null || fileName.isEmpty()) {
            needSync = true;
            // fileName = file.getOriginalFilename();
            fileName = FileUtils.getUUIDFile(file.getOriginalFilename());
        }
        String subDir = FileUtils.getSubDir(fileName);
        File dest = new File(uploadPath + "/" + subDir + "/" + fileName);
        file.transferTo(dest);

        // 同步文件到backup
        if (needSync) {
            httpSyncer.sync(dest, backupUrl);
        }

        return fileName;
    }

    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) {
        String subDir = FileUtils.getSubDir(name);
        String path = uploadPath + "/" + subDir + "/" + name;
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStream inputStream = new BufferedInputStream(fis);
            byte[] buffer = new byte[16 * 1024];

            // 加一些response的头
            response.setCharacterEncoding("UTF-8");
            response.setContentType(FileUtils.getMimeType(name));
            // response.setContentType("application/octet-stream");
            // response.setHeader("Content-Disposition", "attachment;filename=" + name);
            response.setHeader("Content-Length", String.valueOf(file.length()));

            // 读取文件信息并逐段输出
            OutputStream outputStream = response.getOutputStream();
            while(inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
