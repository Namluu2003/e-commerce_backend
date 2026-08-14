package com.poly.app.domain.admin.product.controller;

import com.poly.app.domain.admin.product.request.Cloundinary;
import com.poly.app.domain.admin.product.service.CloundinaryService;
import com.poly.app.infrastructure.util.CloudinaryUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryController {
    CloundinaryService cloundinaryService;

    @PostMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestBody Map<String, String> request) {
        String publicId = request.get("public_id");
        return cloundinaryService.deleteImage(publicId);
    }

    @PostMapping("/deleteandupdatedb")
    public ResponseEntity<?> deleteImageAndUpdateDB(@RequestBody Cloundinary reqCloundinary) {
        return cloundinaryService.deleteImageAndUpdateDB(reqCloundinary.getPublicId(), reqCloundinary.getProductId(), reqCloundinary.getColorId());
    }


}

