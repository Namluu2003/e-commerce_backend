package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.brand.BrandResponse;
import com.poly.app.domain.admin.product.response.brand.BrandResponseSelect;
import com.poly.app.domain.admin.product.response.productdetail.ProductDetailResponse;
import com.poly.app.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.brand.BrandResponse(b.id,b.code,b.brandName,b.updatedAt,b.status) from Brand b where b.brandName like :name order by b.createdAt desc")
    Page<BrandResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select * from brand order by created_at desc", nativeQuery = true)
    Page<Brand> getAll( Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.brand.BrandResponseSelect(b.id,b.brandName,b.status) from Brand b order by b.createdAt desc ")
    List<BrandResponseSelect> dataSelect();

    @Query(value = "select new com.poly.app.domain.admin.product.response.brand.BrandResponseSelect(b.id,b.brandName,b.status) from Brand b where b.status =0 order by b.createdAt desc ")
    List<BrandResponseSelect> dataSelectHD();

    boolean existsByBrandName(String brandName);

    boolean existsByBrandNameAndIdNot(String brandName, Integer id);
}
