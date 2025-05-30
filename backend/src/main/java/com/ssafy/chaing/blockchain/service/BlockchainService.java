package com.ssafy.chaing.blockchain.service;

import com.ssafy.chaing.blockchain.portfolio.output.ContractPortfolio;
import com.ssafy.chaing.blockchain.portfolio.output.TransferPortfolioResponse;
import com.ssafy.chaing.blockchain.service.dto.PDFPathDTO;

public interface BlockchainService {
    ContractPortfolio getContractPortfolio(Long contractId);

    TransferPortfolioResponse getTransferPortfolio(Long contractId);

    PDFPathDTO createContractPDF(ContractPortfolio portfolio);

    PDFPathDTO createTransferPDF(TransferPortfolioResponse portfolioList);

}
