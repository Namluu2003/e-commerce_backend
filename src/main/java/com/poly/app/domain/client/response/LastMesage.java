package com.poly.app.domain.client.response;

import com.poly.app.infrastructure.constant.MessageStatus;
import com.poly.app.infrastructure.constant.SenderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public  class LastMesage {
    private SenderType senderType;
    private String lastMesage;


}