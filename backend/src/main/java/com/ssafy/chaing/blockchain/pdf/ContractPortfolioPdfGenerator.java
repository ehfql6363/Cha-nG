package com.ssafy.chaing.blockchain.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.ssafy.chaing.blockchain.handler.contract.output.PaymentInfoOutput;
import com.ssafy.chaing.blockchain.portfolio.output.ContractPortfolio;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component // Spring Bean으로 등록
@Slf4j
public class ContractPortfolioPdfGenerator implements PDFGenerator<ContractPortfolio> {

    private static final String FONT_PATH_5 = "font/Paperlogy-5Medium.ttf";
    private static final String FONT_PATH_7 = "font/Paperlogy-7Bold.ttf";

    @Override
    public byte[] generate(ContractPortfolio portfolio) {
        if (portfolio == null) {
            throw new BadRequestException(ExceptionCode.PDF_GENERATION_FAILED);
        }

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             PdfWriter writer = new PdfWriter(byteStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            PdfFont paperlogy5 = null;
            PdfFont paperlogy7 = null;
            try {
                // PdfFontFactory.createFont() 는 IO 예외를 던질 수 있음
                // 클래스패스 리소스를 직접 스트림으로 읽어오는 것이 더 안정적일 수 있음
                // 예시: InputStream fontStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH);
                //       if (fontStream == null) throw new IOException("Font file not found in classpath: " + FONT_PATH);
                //       koreanFont = PdfFontFactory.createFont(IOUtils.toByteArray(fontStream), PdfEncodings.IDENTITY_H, true);
                //       fontStream.close();
                // 또는 직접 경로 지정 (개발 환경에 따라 다름)
                // String absoluteFontPath = "C:/path/to/your/fonts/NanumGothic.ttf";
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream(FONT_PATH_5);
                if (fontStream == null) {
                    throw new IOException("Font file not found in classpath: " + FONT_PATH_5);
                }
                paperlogy5 = PdfFontFactory.createFont(FONT_PATH_5, PdfEncodings.IDENTITY_H);
                paperlogy7 = PdfFontFactory.createFont(FONT_PATH_7, PdfEncodings.IDENTITY_H);
                fontStream.close();
            } catch (IOException e) {
                log.error("Failed to load Korean font: {}", FONT_PATH_5, e);
                // 폰트 로드 실패 시 예외 처리 또는 기본 폰트로 대체하는 로직 추가 가능
                throw new BadRequestException(ExceptionCode.PDF_GENERATION_FAILED);
            }
            // 제목 추가
            Paragraph title = new Paragraph("서약서")
                    .setFont(paperlogy7)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16);
            document.add(title);
            document.add(new Paragraph("\n")); // 공백 라인

            // 데이터 추가
            document.add(new Paragraph("<h1>서약서 ID: " + safeToString(portfolio.getId()) + "/<h1>").setFont(paperlogy5));
            document.add(new Paragraph("시작일: " + safeToString(portfolio.getStartDate())).setFont(paperlogy5));
            document.add(new Paragraph("종료일: " + safeToString(portfolio.getEndDate())).setFont(paperlogy5));
            document.add(new Paragraph("총 월세 금액: " + safeToString(portfolio.getRentTotalAmount())).setFont(paperlogy5));
            document.add(new Paragraph("월세 납부 날짜(일): " + safeToString(portfolio.getRentDueDate())).setFont(paperlogy5));
            document.add(
                    new Paragraph("월세/공과금 통장 계좌: " + safeToString(portfolio.getRentAccountNo())).setFont(paperlogy5));
            document.add(
                    new Paragraph("집 주인 통장 계좌: " + safeToString(portfolio.getOwnerAccountNo())).setFont(paperlogy5));
            document.add(
                    new Paragraph("월세 납부 총 비율: " + safeToString(portfolio.getRentTotalRatio())).setFont(paperlogy5));
            document.add(
                    new Paragraph("카드 발급 여부: " + safeToString(portfolio.getIsUtilityEnabled())).setFont(paperlogy5));
            document.add(new Paragraph("공과금 납부 총 비율: " + safeToString(portfolio.getUtilitySplitRatio())).setFont(
                    paperlogy5));
            document.add(new Paragraph("카드 ID: " + safeToString(portfolio.getCardId())).setFont(paperlogy5));

            // Payment Infos 리스트 처리
            if (portfolio.getPaymentInfos() != null && !portfolio.getPaymentInfos().isEmpty()) {
                document.add(new Paragraph("\n그룹원 월세 납부 정보:").setBold().setFont(paperlogy5));
                for (PaymentInfoOutput payment : portfolio.getPaymentInfos()) {
                    document.add(new Paragraph("  - UserId: " + safeToString(payment.getUserId())
                            + ", 월세 금액: " + safeBigIntToString(payment.getAmount()) // BigInteger 처리
                            + ", 월세 비율: " + safeToString(payment.getRatio())
                    ).setMarginLeft(20).setFont(paperlogy5));
                }
            } else {
                document.add(new Paragraph("\n그룹원 월세 납부 정보가 없습니다.").setFont(paperlogy5));
            }

            document.close(); // Document 닫기 (필수)
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new BadRequestException(ExceptionCode.PDF_GENERATION_FAILED);
        }
    }

    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : "N/A";
    }

    private String safeBigIntToString(BigInteger bi) {
        return bi != null ? bi.toString() : "N/A";
    }
}
