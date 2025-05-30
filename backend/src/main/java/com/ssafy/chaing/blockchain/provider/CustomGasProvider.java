package com.ssafy.chaing.blockchain.provider;

import java.math.BigInteger;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

public class CustomGasProvider extends DefaultGasProvider implements ContractGasProvider {

    // 네트워크의 최소 가스 팁 캡 요구사항에 맞추어 설정 (25000000000 wei 이상)
    private static final BigInteger CUSTOM_GAS_PRICE = Convert.toWei("500", Convert.Unit.GWEI).toBigInteger();
    private static final BigInteger CUSTOM_GAS_LIMIT = BigInteger.valueOf(4_500_000L);

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return CUSTOM_GAS_PRICE;
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return CUSTOM_GAS_LIMIT;
    }
}
