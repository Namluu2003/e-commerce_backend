package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.material.MaterialResponseSelect;
import com.poly.app.domain.admin.product.response.material.MaterialResponse;
import com.poly.app.domain.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.material.MaterialResponse(c.id,c.code,c.materialName,c.updatedAt,c.status) from Material c where c.materialName like :name order by c.createdAt desc")
    Page<MaterialResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.material.MaterialResponse(c.id,c.code,c.materialName,c.updatedAt,c.status) from Material c order by c.createdAt desc")
    Page<MaterialResponse> getAll(Pageable pageable);

    boolean existsByMaterialName(String name);

    boolean existsByMaterialNameAndIdNot(String name, Integer id);
    @Query(value = "select new com.poly.app.domain.admin.product.response.material.MaterialResponseSelect(b.id,b.materialName,b.status) from Material b order by b.createdAt desc ")
    List<MaterialResponseSelect> dataSelect();
    @Query(value = "select new com.poly.app.domain.admin.product.response.material.MaterialResponseSelect(b.id,b.materialName,b.status) from Material b where b.status=0 order by b.createdAt desc ")
    List<MaterialResponseSelect> dataSelecthd();

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.material.MaterialResponse(s.id,s.code, s.materialName,s.updatedAt,s.status) " +
           "FROM Material s JOIN ProductDetail pd ON pd.material.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0  order by s.materialName ")
    List<MaterialResponse> findMaterialsByProductId(@Param("productId") int productId);
    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.material.MaterialResponse(s.id,s.code, s.materialName,s.updatedAt,s.status) " +
           "FROM Material s JOIN ProductDetail pd ON pd.material.id = s.id " +
           "WHERE pd.product.id = :productId and pd.gender.id = :genderId and pd.status=0  order by s.materialName ")
    List<MaterialResponse> findMaterialsByProductIdAndGenderId(@Param("productId") int productId,@Param("genderId") int genderId);
}
