package com.ssafy.chaing.blockchain.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ssafy.chaing.blockchain.handler.contract.output.PaymentInfoOutput;
import com.ssafy.chaing.blockchain.portfolio.output.ContractPortfolio;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractPdfGenerator implements PDFGenerator<ContractPortfolio> {

    private final UserRepository userRepository;

    private static final DateTimeFormatter KST_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy년 M월 d일").withZone(ZoneId.of("Asia/Seoul"));

    @Override
    public byte[] generate(ContractPortfolio portfolio) {
        if (portfolio == null) {
            throw new BadRequestException(ExceptionCode.PDF_GENERATION_FAILED);
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            String html = buildHtml(portfolio);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFont(() -> getClass().getResourceAsStream("/font/Paperlogy-7Bold.ttf"), "Paperlogy7");
            builder.useFont(() -> getClass().getResourceAsStream("/font/Paperlogy-5Medium.ttf"), "Paperlogy5");
            URL resourceUrl = getClass().getClassLoader().getResource("logo/ChainG_Sign.png");
            if (resourceUrl == null) {
                throw new RuntimeException("로고 이미지 경로를 찾을 수 없습니다!");
            }
            String baseUri = resourceUrl.toString().replace("ChainG_Sign.png", "");
            builder.withHtmlContent(html, baseUri);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            log.error("PDF 생성 실패", e);
            throw new BadRequestException(ExceptionCode.PDF_GENERATION_FAILED);
        }
    }

    private String buildHtml(ContractPortfolio portfolio) {
        String inlineCss = """
                    <style>
                        body {
                            font-family: 'Paperlogy5', sans-serif;
                            margin: 0;
                            padding: 0;
                        }
                        h1 {
                            font-family: 'Paperlogy7', sans-serif;
                            font-size: 40px;
                        }
                        .subtitle,
                        .description,
                        .section-row {
                            margin: 0;
                            padding: 2px 0; 
                        }
                        .agreement-list,
                        .page-number {
                            font-family: 'Paperlogy5', sans-serif;
                        }
                        .user-title,
                        .total {
                            padding-top: 6px; 
                            margin: 0;         
                        }
                        .agreement-first-title {
                            font-family: 'Paperlogy7', sans-serif;
                            vertical-align: top;
                            padding-top: 12px;
                        }
                        .container {
                            width: 210mm;
                            background-color: white;
                            box-sizing: border-box;
                            padding: 30px 36px 6px 36px;
                        }
                        .page {
                            width: 100%;
                            height: 240mm;
                            position: relative;
                            padding-bottom: 20mm; 
                            page-break-after: always;
                        }
                        .user,
                        .user-title,
                        .user-total {
                            display: table;
                            width: 100%;
                            table-layout: fixed;
                            border-collapse: collapse;
                            margin-bottom: 10px;
                        }
                        .user > div,
                        .user-title > div {
                            display: table-cell;
                            vertical-align: middle;
                            text-align: left;
                            padding: 10px;
                            border-bottom: 1px solid #ccc;
                        }
                        .user-total > div {
                            display: table-cell;
                            vertical-align: middle;
                            text-align: left;
                            padding: 10px;
                            border-top: 1px solid #000;
                        }
                        .agreement-section {
                            border-top: 1px solid #000;
                            border-bottom: 1px solid #000;
                            padding-bottom: 20px;
                        }
                        .agreement-section > div {
                            border-top: 1px solid #ccc;
                            padding-top: 10px;
                        }
                        .agreement-list {
                            list-style-type: decimal;
                            margin-left: 20px;
                            font-size: 14px;
                            width: 92%;
                        }
                        .signature-section {
                            text-align: center;
                            margin-top: 40px;
                        }
                        .signature-date {
                            margin-bottom: 20px;
                            font-size: 16px;
                            padding-bottom: 100px;
                        }
                        .signature-box {
                            margin-top: 40px;
                        }
                        .signature-table {
                            table-layout: fixed;
                            border-collapse: collapse;
                            margin: 0 auto;
                            border: 1px solid #000;
                        }
                        .signature-name-row td {
                            font-family: 'Paperlogy5', sans-serif;
                            font-size: 16px;
                            text-align: center;
                            padding-bottom: 8px;
                        }
                        .signature-stamp-row td {
                            text-align: center;
                            padding: 10px;
                        }
                        .stamp {
                            width: 60px;
                            height: 60px;
                            line-height: 60px;
                            border-radius: 50%;
                            background-color: transparent;
                            color: #ff0000;
                            border: 1px solid;
                            font-size: 12px;
                            text-align: center;
                            vertical-align: middle;
                            margin: 12px auto;
                        }
                        .user-title {
                            background-color: #edf0f4;
                            padding: 18px 10px;
                            border-bottom: 1px solid #000;
                        }
                        .bottom-logo {
                            position: absolute;
                            bottom: 10mm;
                            font-size: 30px;
                            font-family: 'Paperlogy7', sans-serif;
                            width: 85%;
                            text-align: right;
                            padding-top: 0;
                            margin:0;
                        }
                        .page-number {
                            position: absolute;
                            bottom: 0mm;
                            width: 100%;
                            text-align: center;
                            font-size: 12px;
                        }
                        .inline-block {
                            display: inline-block;
                            vertical-align: top;
                        }
                        .width-49 {
                            width: 49%;
                        }
                        .width-100 {
                            width: 100%;
                        }
                        .width-25 {
                            width: 25%;
                        }
                        .width-63 {
                            width: 63%;
                        }
                        .page:last-child {
                            page-break-after: auto !important;
                        }
                        .payment-table {
                             width: 98%; /* ← 100%에서 살짝 줄임 */
                             border-collapse: collapse;
                             margin: 10px auto 20px auto; /* 가운데 정렬 */
                             font-size: 14px;
                             table-layout: fixed;
                             box-sizing: border-box; 
                         }
                
                         .payment-table th,
                         .payment-table td {
                             border: 1px solid #ccc;
                             padding: 10px 12px;
                             word-break: break-word; /* 긴 단어 줄바꿈 */
                         }
                
                         .payment-table th {
                             background-color: #edf0f4;
                             font-weight: bold;
                         }
                
                         .payment-table th:nth-child(1),
                         .payment-table td:nth-child(1) {
                             width: 45%;
                         }
                
                         .payment-table th:nth-child(2),
                         .payment-table td:nth-child(2) {
                             width: 25%;
                         }
                
                         .payment-table th:nth-child(3),
                         .payment-table td:nth-child(3) {
                             width: 30%;
                         }
                    </style>
                """;

        StringBuilder paymentInfoHtml = new StringBuilder();
        BigInteger totalAmount = BigInteger.ZERO;
        int totalRatio = 0;
        List<PaymentInfoOutput> infos = portfolio.getPaymentInfos();

        paymentInfoHtml.append("""
                    <table class='payment-table'>
                        <thead>
                            <tr>
                                <th class='align-left'>월세 지불 정보</th>
                                <th class='align-center'>분배 비율(%)</th>
                                <th class='align-right'>지불액(원)</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        if (infos != null) {
            for (PaymentInfoOutput info : infos) {
                paymentInfoHtml.append("<tr>")
                        .append("<td class='align-left'>").append(getUserName(info.getUserId())).append("</td>")
                        .append("<td class='align-center'>").append(safe(info.getRatio())).append("</td>")
                        .append("<td class='align-right'>").append(safe(info.getAmount())).append("원</td>")
                        .append("</tr>");
                if (info.getAmount() != null) {
                    totalAmount = totalAmount.add(info.getAmount());
                }
                if (info.getRatio() != null) {
                    totalRatio += info.getRatio().intValue();
                }
            }
        }

        paymentInfoHtml.append("</tbody></table>");
        String base64Image = encodeImageToBase64();
        String signatureTableHtml = """
                    <div class='signature-area'>
                        <table class='signature-table'>
                            <tr class='signature-name-row'>
                """ + infos.stream()
                .map(info -> "<td class='signature-name'>" + getUserName(info.getUserId()) + "</td>")
                .collect(Collectors.joining()) +
                """
                                    </tr>
                                    <tr class='signature-stamp-row'>
                        """ + infos.stream()
                .map(i -> "<td class='stamp'><img src='data:image/png;base64," + base64Image
                        + "' alt='도장' style='max-width:60px; max-height:60px; display:block; margin:0 auto;'/></td>")
                .collect(Collectors.joining()) +
                """
                                </tr>
                            </table>
                        </div>
                        """;

        String currentDateKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

        ZonedDateTime startZoned = ZonedDateTime.parse(portfolio.getStartDate())
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime endZoned = ZonedDateTime.parse(portfolio.getEndDate())
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        String startDateKST = KST_DATE_FORMAT.format(startZoned);
        String endDateKST = KST_DATE_FORMAT.format(endZoned);

        return String.format("""
                        <!DOCTYPE html>
                        <html lang="ko">
                        <head>
                            <meta charset="UTF-8" />
                            <title>cha:n G 서약서</title>
                            %s
                        </head>
                        <body>
                            <div class="container">
                                <div class="page">
                                    <div class="subtitle">블록체인 기반</div>
                                    <h1>cha:n G 서약서</h1>
                                    <div class="description">서약서 ID: %s</div>
                                    <div class="description">해당 서약서는 모든 사람의 승인 하에 생성된 서약서 입니다.</div>
                        
                                    <div class="inline-block width-49">
                                        <div class="total">서약 기간</div>
                                        <div class="section-row">시작일: %s</div>
                                        <div class="section-row">종료일: %s</div>
                                    </div>
                                    <div class="inline-block width-49">
                                        <div class="total">월세</div>
                                        <div class="section-row">월세 총액: %s</div>
                                        <div class="section-row">월세 지불일: 매월 %s일</div>
                                    </div>
                        
                                    %s
                                    <div class="user user-total">
                                        <div class="total">총</div>
                                        <div>%d</div>
                                        <div>%s원</div>
                                    </div>
                        
                                    <div class="inline-block width-49">
                                        <div class="total">계좌</div>
                                        <div class="section-row">cha:nG 계좌: %s</div>
                                        <div class="section-row">집주인 계좌: %s</div>
                                    </div>
                                    <div class="inline-block width-49">
                                        <div class="total">공과금</div>
                                        <div class="section-row">공과금 분배 비율: %s</div>
                                        <div class="section-row">공과금 카드 ID: %s</div>
                                    </div>
                        
                                    <div class="bottom-logo">cha:n G</div>
                                    <div class="page-number">- 1 -</div>
                                </div>
                            </div>
                        
                            <div class="container">
                                <div class="page">
                                    <div class="agreement-section">
                                        <div>
                                            <div class="agreement-first-title inline-block width-25">서약해지 및 수정규정</div>
                                            <div class="inline-block width-63">
                                                <ol class="agreement-list">
                                                    <li>서약 해재를 원할 경우, 일정 기간의 예고와 상호협의가 필요합니다.</li>
                                                    <li>변동사항이 있을 경우 기존 서약서는 파기하고, 새로운 서약서를 생성해야 됩니다.</li>
                                                </ol>
                                            </div>
                                        </div>
                        
                                        <div>
                                            <div class="agreement-first-title inline-block width-25">블록체인 기반 서약서</div>
                                            <div class="inline-block width-63">
                                                <ol class="agreement-list">
                                                    <li>서약서는 블록체인 기술을 활용하여 기록되며, 투명하게 관리됩니다.</li>
                                                    <li>임의로 수정, 변경이 불가능합니다.</li>
                                                    <li>블록체인에 저장되어 폴라곤스캔에서 상시 확인가능합니다.</li>
                                                </ol>
                                            </div>
                                        </div>
                        
                                        <div>
                                            <div class="agreement-first-title inline-block width-25">법적 효력</div>
                                            <div class="inline-block width-63">
                                                <ol class="agreement-list">
                                                    <li>참여자 간의 합의로 효력을 발생하며, 모든 참여자는 해당 서약을 준수할 의무가 있습니다.</li>
                                                </ol>
                                            </div>
                                        </div>
                                    </div>
                        
                                    <div class="signature-section">
                                        <div class="signature-date">서약서 최종 승인일: %s</div>
                                        <div class="signature-box">
                                            %s
                                        </div>
                                        <div class="agreement-list">이 계약서는 서명일로부터 유효하며, 계약 종료 후 자동으로 종료됩니다.</div>
                                    </div>
                        
                                    <div class="bottom-logo">cha:n G</div>
                                    <div class="page-number">- 2 -</div>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                inlineCss,
                safe(portfolio.getId()),
                startDateKST,
                endDateKST,
                safe(portfolio.getRentTotalAmount()),
                safe(portfolio.getRentDueDate()),
                paymentInfoHtml.toString(),
                totalRatio,
                totalAmount.toString(),
                safe(portfolio.getRentAccountNo()),
                safe(portfolio.getOwnerAccountNo()),
                safe(portfolio.getUtilitySplitRatio()),
                safe(portfolio.getCardId()),
                currentDateKST,
                signatureTableHtml
        );
    }

    private String safe(Object o) {
        if (o == null) {
            return "N/A";
        }
        String value = o.toString();
        if ("0".equals(value)) {
            return "N/A";  // 0도 표시 안 하려면 유지
        }
        return value;
    }

    private String getUserName(BigInteger userId) {
        return userRepository.findById(userId.longValue())
                .map(UserEntity::getName)
                .orElse("알 수 없음");
    }

    private String encodeImageToBase64() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("logo/ChainG_Sign.png")) {
            if (is == null) {
                throw new RuntimeException("로고 이미지를 읽을 수 없습니다.");
            }
            byte[] imageBytes = is.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("로고 이미지 인코딩 실패", e);
        }
    }

}
