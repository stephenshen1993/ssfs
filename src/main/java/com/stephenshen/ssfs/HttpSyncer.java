package com.stephenshen.ssfs;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * sync file to backup server.
 *
 * @author stephenshen
 * @date 2024/7/20 16:32:17
 */
@Component
public class HttpSyncer {

    public static String X_FILE_NAME = "X-Filename";

    public String sync(File file, String url) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(X_FILE_NAME, file.getName());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(builder.build(), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        String result = responseEntity.getBody();
        System.out.println(" sync result = " + result);
        return result;
    }

}
