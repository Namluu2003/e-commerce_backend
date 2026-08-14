package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.productdetail.FilterProductDetailDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDetailRepositoryDTO {
    @PersistenceContext
    private EntityManager entityManager;
//
//    public List<ProductDetailRepositoryDTO> getFilterProductDetail(String productName, String brandName,
//                                                                    String typeName, String colorName, String materialName,
//                                                                    String sizeName, String soleName, String genderName,
//                                                                    String status, String sortByQuantity, String sortByPrice,
//                                                                    int page, int size) {
//
//        // Tạo thủ tục lưu trữ (stored procedure) query
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_FilterProductDetails", FilterProductDetailDTO.class);
//
//        // Thêm tham số vào thủ tục
//        query.registerStoredProcedureParameter("productName", String.class, ParameterMode.IN);
//        query.setParameter("productName", productName);
//
//        query.registerStoredProcedureParameter("brandName", String.class, ParameterMode.IN);
//        query.setParameter("brandName", brandName);
//
//        query.registerStoredProcedureParameter("typeName", String.class, ParameterMode.IN);
//        query.setParameter("typeName", typeName);
//
//        query.registerStoredProcedureParameter("colorName", String.class, ParameterMode.IN);
//        query.setParameter("colorName", colorName);
//
//        query.registerStoredProcedureParameter("materialName", String.class, ParameterMode.IN);
//        query.setParameter("materialName", materialName);
//
//        query.registerStoredProcedureParameter("sizeName", String.class, ParameterMode.IN);
//        query.setParameter("sizeName", sizeName);
//
//        query.registerStoredProcedureParameter("soleName", String.class, ParameterMode.IN);
//        query.setParameter("soleName", soleName);
//
//        query.registerStoredProcedureParameter("genderName", String.class, ParameterMode.IN);
//        query.setParameter("genderName", genderName);
//
//        query.registerStoredProcedureParameter("status", String.class, ParameterMode.IN);
//        query.setParameter("status", status);
//
//        query.registerStoredProcedureParameter("sortByQuantity", String.class, ParameterMode.IN);
//        query.setParameter("sortByQuantity", sortByQuantity);
//
//        query.registerStoredProcedureParameter("sortByPrice", String.class, ParameterMode.IN);
//        query.setParameter("sortByPrice", sortByPrice);
//
//        query.registerStoredProcedureParameter("page", Integer.class, ParameterMode.IN);
//        query.setParameter("page", page);
//
//        query.registerStoredProcedureParameter("size", Integer.class, ParameterMode.IN);
//        query.setParameter("size", size);
//
//        // Thực thi thủ tục và trả về kết quả
//        return query.getResultList();
//    }

}
