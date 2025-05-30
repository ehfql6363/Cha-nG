package com.ssafy.chaing.fintech.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private boolean success;

    public static TransferDTO success() {
        return new TransferDTO(true);
    }

    public static TransferDTO failure() {
        return new TransferDTO(false);
    }
}
