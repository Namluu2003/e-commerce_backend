package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.sole.SoleResponseSelect;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.admin.product.response.sole.SoleResponse;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.model.Sole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoleRepository extends JpaRepository<Sole,Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.sole.SoleResponse(c.id,c.code,c.soleName,c.updatedAt,c.status) from Sole c where c.soleName like :name order by c.createdAt desc")
    Page<SoleResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.sole.SoleResponse(c.id,c.code,c.soleName,c.updatedAt,c.status) from Sole c order by c.createdAt desc")
    Page<SoleResponse> getAll(Pageable pageable);

    boolean existsBySoleName(String name);

    boolean existsBySoleNameAndIdNot(String name, Integer id);
    @Query(value = "select new com.poly.app.domain.admin.product.response.sole.SoleResponseSelect(b.id,b.soleName,b.status) from Sole b order by b.createdAt desc ")
    List<SoleResponseSelect> dataSelect();
    @Query(value = "select new com.poly.app.domain.admin.product.response.sole.SoleResponseSelect(b.id,b.soleName,b.status) from Sole b where  b.status = 0 order by b.createdAt desc ")
    List<SoleResponseSelect> dataSelecthd();

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.sole.SoleResponse(s.id,s.code, s.soleName,s.updatedAt,s.status) " +
           "FROM Sole s JOIN ProductDetail pd ON pd.sole.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0  order by s.soleName ")
    List<SoleResponse> findSolesByProductId(@Param("productId") int productId);
    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.sole.SoleResponse(s.id,s.code, s.soleName,s.updatedAt,s.status) " +
           "FROM Sole s JOIN ProductDetail pd ON pd.sole.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0 and pd.material.id = :materialId and pd.gender.id = :genderId order by s.soleName ")
    List<SoleResponse> findSolesByProductIdAndMaterialId(@Param("productId") int productId,@Param("materialId") int materialId,@Param("genderId") int genderId);
}
