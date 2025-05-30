package com.ssafy.chaing.blockchain.controller.response;

import com.ssafy.chaing.blockchain.service.dto.PDFPathDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PDFPathResponse {
    private String presignedUrl;

    public static PDFPathResponse from(PDFPathDTO dto) {
        return new PDFPathResponse(dto.getPresignedUrl());
    }
}
