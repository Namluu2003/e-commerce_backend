package com.poly.app.domain.client.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    Integer id;
    String fullName;
    Boolean isOnline;
    LastMesage lastMesage;
}
