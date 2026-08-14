package com.poly.app.domain.repository;

import com.poly.app.domain.admin.product.response.type.TypeResponseSelect;
import com.poly.app.domain.admin.product.response.type.TypeResponse;
import com.poly.app.domain.admin.product.response.type.TypeResponse;
import com.poly.app.domain.model.Type;
import com.poly.app.domain.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type,Integer> {
    @Query(value = "select new com.poly.app.domain.admin.product.response.type.TypeResponse(c.id,c.code,c.typeName,c.updatedAt,c.status) from Type c where c.typeName like :name order by c.createdAt desc")
    Page<TypeResponse> fillbyname(String name, Pageable pageable);

    @Query(value = "select new com.poly.app.domain.admin.product.response.type.TypeResponse(c.id,c.code,c.typeName,c.updatedAt,c.status) from Type c order by c.createdAt desc")
    Page<TypeResponse> getAll(Pageable pageable);

    boolean existsByTypeName(String name);

    boolean existsByTypeNameAndIdNot(String name, Integer id);
    @Query(value = "select new com.poly.app.domain.admin.product.response.type.TypeResponseSelect(b.id,b.typeName,b.status) from Type b order by b.createdAt desc ")
    List<TypeResponseSelect> dataSelect();
    @Query(value = "select new com.poly.app.domain.admin.product.response.type.TypeResponseSelect(b.id,b.typeName,b.status) from Type b where b.status = 0 order by b.createdAt desc ")
    List<TypeResponseSelect> dataSelecthd();
}
