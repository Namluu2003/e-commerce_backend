package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.admin.product.response.color.ColorResponseSelect;
import com.poly.app.domain.admin.product.response.color.ColorResponse;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.color.ColorResponse(c.id,c.code,c.colorName,c.updatedAt,c.status) from Color c where c.colorName like :name order by c.createdAt desc")
    Page<ColorResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.color.ColorResponse(c.id,c.code,c.colorName,c.updatedAt,c.status) from Color c order by c.createdAt desc")
    Page<ColorResponse> getAll(Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.color.ColorResponseSelect(b.id,b.colorName,b.status,b.code) from Color b order by b.createdAt desc ")
    List<ColorResponseSelect> dataSelect();
    @Query(value = "select new com.poly.app.domain.admin.product.response.color.ColorResponseSelect(b.id,b.colorName,b.status,b.code) from Color b where b.status=0 order by b.createdAt desc ")
    List<ColorResponseSelect> dataSelectHD();
    boolean existsByColorName(String name);

    boolean existsByColorNameAndIdNot(String name, Integer id);

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.color.ColorResponse(s.id,s.code, s.colorName,s.updatedAt,s.status) " +
           "FROM Color s JOIN ProductDetail pd ON pd.color.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0  order by s.colorName ")
    List<ColorResponse> findColorsByProductId(@Param("productId") int productId);

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.color.ColorResponse(s.id,s.code, s.colorName,s.updatedAt,s.status) " +
           "FROM Color s JOIN ProductDetail pd ON pd.color.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0 and pd.sole.id = :soleId and pd.material.id = :materialId and pd.gender.id = :genderId  order by s.colorName ")
    List<ColorResponse> findColorsByProductIdAndSoleId(@Param("productId") int productId,
                                                       @Param("soleId") int soleId,
                                                       @Param("materialId") int materialId,
                                                       @Param("genderId") int genderId);




}
