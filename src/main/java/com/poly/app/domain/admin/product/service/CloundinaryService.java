package com.poly.app.domain.admin.product.service;

import com.poly.app.domain.admin.product.request.color.ColorRequest;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.color.ColorResponseSelect;
import com.poly.app.domain.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CloundinaryService {
    ResponseEntity<?> deleteImage(String publicId);

    ResponseEntity<?> deleteImageAndUpdateDB(String publicId,int productId, int colorId);
}
