package com.poly.app.domain.admin.bill.response;

import com.poly.app.domain.model.Brand;
import com.poly.app.domain.model.Color;
import com.poly.app.domain.model.Gender;
import com.poly.app.domain.model.Material;
import com.poly.app.domain.model.Size;
import com.poly.app.domain.model.Sole;
import com.poly.app.domain.model.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillProductResponse {

    private Integer id;
    private String code;
    private String productName;
    private Integer totalQuantity;
    private Set<Color> colors;
    private List<Gender> genders;
    private List<Material> materials;
    private List<Size> sizes;
    private List<Sole> soles;
    private List<Type> types;

    Brand brand;
}
