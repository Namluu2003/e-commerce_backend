package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.size.SizeResponseSelect;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.domain.admin.product.response.size.SizeResponse;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.size.SizeResponse(c.id,c.code,c.sizeName,c.updatedAt,c.status) from Size c where c.sizeName like :name order by c.createdAt desc")
    Page<SizeResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.size.SizeResponse(c.id,c.code,c.sizeName,c.updatedAt,c.status) from Size c order by c.createdAt desc")
    Page<SizeResponse> getAll(Pageable pageable);

    boolean existsBySizeName(String name);

    boolean existsBySizeNameAndIdNot(String name, Integer id);

    @Query(value = "select new com.poly.app.domain.admin.product.response.size.SizeResponseSelect(b.id,b.sizeName,b.status) from Size b order by b.createdAt desc ")
    List<SizeResponseSelect> dataSelect();

    @Query(value = "select new com.poly.app.domain.admin.product.response.size.SizeResponseSelect(b.id,b.sizeName,b.status) from Size b where b.status = 0 order by b.createdAt desc ")
    List<SizeResponseSelect> dataSelecthd();

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.size.SizeResponse(s.id,s.code, s.sizeName,s.updatedAt,s.status) " +
           "FROM Size s JOIN ProductDetail pd ON pd.size.id = s.id " +
           "WHERE pd.product.id = :productId and pd.status=0  order by s.sizeName ")
    List<SizeResponse> findSizesByProductId(@Param("productId") int productId);

    @Query("SELECT DISTINCT new com.poly.app.domain.admin.product.response.size.SizeResponse(s.id,s.code, s.sizeName,s.updatedAt,s.status) " +
           "FROM Size s JOIN ProductDetail pd ON pd.size.id = s.id " +
           "WHERE pd.product.id = :productId and pd.color.id = :colorId and pd.sole.id = :soleId and pd.material.id = :materialId and pd.gender.id = :genderId and pd.status=0 order by s.sizeName ")
    List<SizeResponse> findSizesByProductIdAndColorId(@Param("productId") int productId,
                                                      @Param("colorId") int colorId,
                                                      @Param("soleId") int soleId,
                                                      @Param("materialId") int materialId,
                                                      @Param("genderId") int genderId
    );

}
