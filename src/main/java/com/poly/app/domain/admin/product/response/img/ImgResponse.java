package com.poly.app.domain.admin.product.response.img;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImgResponse {
    Integer id;
    String url;
    String publicId;
}
