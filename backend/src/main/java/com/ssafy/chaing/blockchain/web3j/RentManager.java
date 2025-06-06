package com.ssafy.chaing.blockchain.web3j;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.2.
 */
@SuppressWarnings("rawtypes")
public class RentManager extends Contract {
    public static final String BINARY = "60808060405234601557610ce6908161001a8239f35b5f80fdfe6080806040526004361015610012575f80fd5b5f3560e01c90816327506f531461095a575080633923dec9146101475763400ebdde1461003d575f80fd5b34610143576020366003190112610143576004355f525f60205260405f2080546001600160401b03811161012f576040519161007f60208360051b0184610b62565b81835260208301905f5260205f205f915b8383106100a957604051806100a58782610a79565b0390f35b600860206001926040516100bc81610b46565b85548152848601548382015260ff60028701541660408201526100e160038701610c10565b60608201526100f260048701610c10565b6080820152600586015460a082015260ff600687015416151560c082015261011c60078701610c10565b60e0820152815201920192019190610090565b634e487b7160e01b5f52604160045260245ffd5b5f80fd5b34610143576101003660031901126101435760243560443560ff8116809103610143576064356001600160401b03811161014357610189903690600401610b83565b906084356001600160401b038111610143576101a9903690600401610b83565b9260c435908115158092036101435760e4356001600160401b038111610143576101d7903690600401610b83565b93604051936101e585610b46565b60043585526020850183815260408601918252606086019283526080860197885260a086019360a435855260c0870195865260e087019788525f525f60205260405f20805490600160401b82101561012f5760018201808255821015610713575f5260205f209060031b0186518155815160018201556002810160ff84511660ff198254161790556003810184518051906001600160401b03821161012f5781906102908454610bd8565b601f811161090a575b50602090601f83116001146108a7575f9261089c575b50508160011b915f199060031b1c19161790555b6004810189518051906001600160401b03821161012f576102e48354610bd8565b601f8111610857575b50602090601f83116001146107f0576007949392915f91836107e5575b50508160011b915f199060031b1c19161790555b85516005820155600681018751151560ff801983541691161790550187518051906001600160401b03821161012f5781906103598454610bd8565b601f8111610795575b50602090601f8311600114610732575f92610727575b50508160011b915f199060031b1c19161790555b600154600160401b81101561012f5760018101806001558110156107135760015f5260205f209060031b019551865551600186015560ff6002860191511660ff198254161790556003840190518051906001600160401b03821161012f5781906103f68454610bd8565b601f81116106c3575b50602090601f8311600114610660575f92610655575b50508160011b915f199060031b1c19161790555b6004830194519485516001600160401b03811161012f5761044a8254610bd8565b601f8111610610575b506020601f82116001146105ab578190600797985f926105a0575b50508160011b915f199060031b1c19161790555b516005830155600682019051151560ff801983541691161790550190519081516001600160401b03811161012f576104ba8254610bd8565b601f811161055b575b50602092601f82116001146104ff57928192935f926104f4575b50505f19600383901b1c191660019190911b179055005b0151905083806104dd565b601f19821693835f52805f20915f5b868110610543575083600195961061052b575b505050811b019055005b01515f1960f88460031b161c19169055838080610521565b9192602060018192868501518155019401920161050e565b825f5260205f20601f830160051c81019160208410610596575b601f0160051c01905b81811061058b57506104c3565b5f815560010161057e565b9091508190610575565b01519050888061046e565b601f19821697835f52815f20985f5b8181106105f857509160079899918460019594106105e0575b505050811b019055610482565b01515f1960f88460031b161c191690558880806105d3565b838301518b556001909a0199602093840193016105ba565b825f5260205f20601f830160051c8101916020841061064b575b601f0160051c01905b8181106106405750610453565b5f8155600101610633565b909150819061062a565b015190508880610415565b5f8581528281209350601f198516905b8181106106ab5750908460019594939210610693575b505050811b019055610429565b01515f1960f88460031b161c19169055888080610686565b92936020600181928786015181550195019301610670565b909150835f5260205f20601f840160051c81019160208510610709575b90601f859493920160051c01905b8181106106fb57506103ff565b5f81558493506001016106ee565b90915081906106e0565b634e487b7160e01b5f52603260045260245ffd5b015190508b80610378565b5f8581528281209350601f198516905b81811061077d5750908460019594939210610765575b505050811b01905561038c565b01515f1960f88460031b161c191690558b8080610758565b92936020600181928786015181550195019301610742565b909150835f5260205f20601f840160051c810191602085106107db575b90601f859493920160051c01905b8181106107cd5750610362565b5f81558493506001016107c0565b90915081906107b2565b015190508d8061030a565b90601f19831691845f52815f20925f5b81811061083f575091600193918560079897969410610827575b505050811b01905561031e565b01515f1960f88460031b161c191690558d808061081a565b92936020600181928786015181550195019301610800565b835f5260205f20601f840160051c81019160208510610892575b601f0160051c01905b81811061088757506102ed565b5f815560010161087a565b9091508190610871565b015190508c806102af565b5f8581528281209350601f198516905b8181106108f257509084600195949392106108da575b505050811b0190556102c3565b01515f1960f88460031b161c191690558c80806108cd565b929360206001819287860151815501950193016108b7565b909150835f5260205f20601f840160051c81019160208510610950575b90601f859493920160051c01905b8181106109425750610299565b5f8155849350600101610935565b9091508190610927565b34610143575f366003190112610143576001546001600160401b03811161012f5761098b60208260051b0183610b62565b8082526020820160015f527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf65f915b8383106109cf57604051806100a58782610a79565b600860206001926040516109e281610b46565b85548152848601548382015260ff6002870154166040820152610a0760038701610c10565b6060820152610a1860048701610c10565b6080820152600586015460a082015260ff600687015416151560c0820152610a4260078701610c10565b60e08201528152019201920191906109ba565b805180835260209291819084018484015e5f828201840152601f01601f1916010190565b602081016020825282518091526040820191602060408360051b8301019401925f915b838310610aab57505050505090565b9091929394602080610b37600193603f1986820301875289519081518152838201518482015260ff604083015116604082015260e0610b10610afe60608501516101006060860152610100850190610a55565b60808501518482036080860152610a55565b9260a081015160a084015260c0810151151560c084015201519060e0818403910152610a55565b97019301930191939290610a9c565b61010081019081106001600160401b0382111761012f57604052565b90601f801991011681019081106001600160401b0382111761012f57604052565b81601f82011215610143578035906001600160401b03821161012f5760405192610bb7601f8401601f191660200185610b62565b8284526020838301011161014357815f926020809301838601378301015290565b90600182811c92168015610c06575b6020831014610bf257565b634e487b7160e01b5f52602260045260245ffd5b91607f1691610be7565b9060405191825f825492610c2384610bd8565b8084529360018116908115610c8e5750600114610c4a575b50610c4892500383610b62565b565b90505f9291925260205f20905f915b818310610c72575050906020610c48928201015f610c3b565b6020919350806001915483858901015201910190918492610c59565b905060209250610c4894915060ff191682840152151560051b8201015f610c3b56fea26469706673582212201633aae9aadbaea8dc01a3b19cdcd8e4ba2ea292067023dfe744f5ef26c0380464736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDTRANSACTION = "addTransaction";

    public static final String FUNC_GETALLTRANSACTIONS = "getAllTransactions";

    public static final String FUNC_GETTRANSACTIONSBYACCOUNT = "getTransactionsByAccount";

    @Deprecated
    protected RentManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RentManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RentManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RentManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addTransaction(BigInteger _id,
            BigInteger _accountId, BigInteger _month, String _from, String _to, BigInteger _amount,
            Boolean _status, String _time) {
        final Function function = new Function(
                FUNC_ADDTRANSACTION, 
                Arrays.<Type>asList(new Uint256(_id),
                new Uint256(_accountId),
                new Uint8(_month),
                new Utf8String(_from),
                new Utf8String(_to),
                new Uint256(_amount),
                new Bool(_status),
                new Utf8String(_time)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getAllTransactions() {
        final Function function = new Function(FUNC_GETALLTRANSACTIONS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Rent>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getTransactionsByAccount(BigInteger _accountId) {
        final Function function = new Function(FUNC_GETTRANSACTIONSBYACCOUNT, 
                Arrays.<Type>asList(new Uint256(_accountId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Rent>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static RentManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new RentManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RentManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RentManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RentManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new RentManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RentManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RentManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RentManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(RentManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<RentManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(RentManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<RentManager> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(RentManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<RentManager> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(RentManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class Rent extends DynamicStruct {
        public BigInteger id;

        public BigInteger accountId;

        public BigInteger month;

        public String from;

        public String to;

        public BigInteger amount;

        public Boolean status;

        public String time;

        public Rent(BigInteger id, BigInteger accountId, BigInteger month, String from, String to,
                BigInteger amount, Boolean status, String time) {
            super(new Uint256(id),
                    new Uint256(accountId),
                    new Uint8(month),
                    new Utf8String(from),
                    new Utf8String(to),
                    new Uint256(amount),
                    new Bool(status),
                    new Utf8String(time));
            this.id = id;
            this.accountId = accountId;
            this.month = month;
            this.from = from;
            this.to = to;
            this.amount = amount;
            this.status = status;
            this.time = time;
        }

        public Rent(Uint256 id, Uint256 accountId, Uint8 month, Utf8String from, Utf8String to,
                Uint256 amount, Bool status, Utf8String time) {
            super(id, accountId, month, from, to, amount, status, time);
            this.id = id.getValue();
            this.accountId = accountId.getValue();
            this.month = month.getValue();
            this.from = from.getValue();
            this.to = to.getValue();
            this.amount = amount.getValue();
            this.status = status.getValue();
            this.time = time.getValue();
        }
    }
}
