package com.poly.app.domain.admin.Statistical.Service;

import com.poly.app.domain.admin.Statistical.Repository.MinProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MinProductService {
    private final MinProductRepository minProductRepository;

    public MinProductService(MinProductRepository minProductRepository) {
        this.minProductRepository = minProductRepository;
    }

    public List<MinProductDTO> getProductsWithLowQuantity(int quantity) {
        List<Object[]> results = minProductRepository.findProductsWithQuantityLessThan(quantity);

        if (results == null || results.isEmpty()) {
            return List.of();  // Trả về danh sách rỗng thay vì null
        }

        return results.stream().map(obj -> new MinProductDTO(
                obj[0] != null ? Integer.parseInt(obj[0].toString()) : 0,
                obj[1] != null ? obj[1].toString() : "Unknown",
                obj[2] != null ? obj[2].toString() : "Unknown",
                obj[3] != null ? obj[3].toString() : "Unknown",
                obj[4] != null ? obj[4].toString() : "Unknown",
                obj[5] != null ? obj[5].toString() : "Unknown",
                obj[6] != null ? obj[6].toString() : "Unknown",
                obj[7] != null ? Double.parseDouble(obj[7].toString()) : 0.0,
                obj[8] != null ? Integer.parseInt(obj[8].toString()) : 0
        )).collect(Collectors.toList());
    }
}
