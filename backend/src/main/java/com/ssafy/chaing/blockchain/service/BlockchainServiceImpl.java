package com.ssafy.chaing.blockchain.service;

import com.ssafy.chaing.blockchain.handler.contract.ContractHandler;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractOutput;
import com.ssafy.chaing.blockchain.handler.rent.RentHandler;
import com.ssafy.chaing.blockchain.handler.rent.output.RentOutput;
import com.ssafy.chaing.blockchain.handler.utility.UtilityHandler;
import com.ssafy.chaing.blockchain.handler.utility.output.UtilityOutput;
import com.ssafy.chaing.blockchain.pdf.ContractPdfGenerator;
import com.ssafy.chaing.blockchain.pdf.PDFGenerator;
import com.ssafy.chaing.blockchain.pdf.TransferPortfolioPdfGenerator;
import com.ssafy.chaing.blockchain.portfolio.output.ContractPortfolio;
import com.ssafy.chaing.blockchain.portfolio.output.TransferPortfolio;
import com.ssafy.chaing.blockchain.portfolio.output.TransferPortfolioResponse;
import com.ssafy.chaing.blockchain.service.dto.PDFPathDTO;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.common.util.S3Util;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.repository.ContractUserRepository;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainServiceImpl implements BlockchainService {

    private final ContractHandler contractHandler;
    private final RentHandler rentHandler;
    private final UtilityHandler utilityHandler;

    private final ContractPdfGenerator contractPDFGenerator;
    private final TransferPortfolioPdfGenerator transferPDFGenerator;

    private final S3Util s3Util;
    private final ContractRepository contractRepository;

    @Override
    public ContractPortfolio getContractPortfolio(
            Long contractId
    ) {
        ContractEntity contractEntity = contractRepository.findById(contractId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.CONTRACT_NOT_FOUND));

        if (!contractEntity.getIsCreatedPdf()) {
            throw new BadRequestException(ExceptionCode.PDF_IS_GENERATING);
        }

        BigInteger cid = BigInteger.valueOf(contractId);
        ContractOutput contract = contractHandler.getContract(cid);
        log.info("Contract: {}", contract.getRentAccountNo());
        return ContractPortfolio.from(contract);
    }

    @Override
    public TransferPortfolioResponse getTransferPortfolio(
            Long contractId
    ) {
        BigInteger cid = BigInteger.valueOf(contractId);

        List<RentOutput> rentOutput = rentHandler.getTransactionsByAccountId(cid);
        List<UtilityOutput> utilityOutputs = utilityHandler.getTransactionsByAccountId(cid);

        return new TransferPortfolioResponse(
                contractId,
                new TransferPortfolio(
                        getMonthlyRent(rentOutput),
                        getMonthlyUtility(utilityOutputs)
                ));
    }

    @Override
    public PDFPathDTO createContractPDF(
            ContractPortfolio portfolio
    ) {
        String pdfUrl = generatePDF(
                portfolio,
                contractPDFGenerator,
                "contract-" + portfolio.getId()
        );

        return new PDFPathDTO(pdfUrl);
    }

    @Override
    public PDFPathDTO createTransferPDF(
            TransferPortfolioResponse portfolioList
    ) {
        String pdfUrl = generatePDF(
                portfolioList,
                transferPDFGenerator,
                "contract-" + portfolioList.getContractId()
        );

        return new PDFPathDTO(pdfUrl);
    }

    private <T> String generatePDF(
            T data,
            PDFGenerator<T> generator,
            String baseFileName
    ) {
        byte[] pdfBytes = generator.generate(data);
        return s3Util.uploadPdf(pdfBytes, "pdf/contracts", baseFileName);
    }

    private Map<String, List<RentOutput>> getMonthlyRent(List<RentOutput> rentOutputList) {
        if (rentOutputList == null) {
            return Map.of(); // 빈 맵 반환 또는 예외 처리
        }

        return rentOutputList.stream()
                .filter(ro -> ro.getTime() != null && ro.getTime().length() >= 4 && ro.getMonth() != null) // Null 및 길이 체크
                .collect(Collectors.groupingBy(
                        // 그룹핑 기준: time 앞 4자리(연도) + month 값
                        rentOutput -> rentOutput.getTime().substring(0, 4) + rentOutput.getMonth().toString()
                ));
    }

    private Map<String, List<UtilityOutput>> getMonthlyUtility(List<UtilityOutput> utilityOutputList) {
        if (utilityOutputList == null) {
            return Map.of();
        }

        return utilityOutputList.stream()
                .filter(ro -> ro.getTime() != null && ro.getTime().length() >= 4 && ro.getMonth() != null) // Null 및 길이 체크
                .collect(Collectors.groupingBy(
                        // 그룹핑 기준: time 앞 4자리(연도) + month 값
                        utility -> utility.getTime().substring(0, 4) + utility.getMonth().toString()
                ));
    }
}
