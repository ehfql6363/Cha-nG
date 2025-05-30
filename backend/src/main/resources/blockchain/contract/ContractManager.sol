// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ContractManager {
    // 그룹원 결제 정보 구조체
    struct PaymentInfo {
        uint userId;
        uint amount;
        uint ratio;
    }

    // 월세 정보 구조체
    struct Rent {
        uint totalAmount;
        uint dueDate;
        string rentAccountNo;
        string ownerAccountNo;
        uint totalRatio;
        PaymentInfo[] userPaymentInfo;
    }

    // 공과금 정보 구조체
    struct Utility {
        bool isEnabled;
        uint splitRatio;
        uint cardId;
    }

    // 계약서 데이터 구조체
    struct ContractData {
        uint id;
        string startDate;
        string endDate;
        Rent rent;
        string liveAccountNo; // 생활비 계좌 (nullable)
        Utility utility;
    }

    // 계약서 저장: id 기준으로 저장 및 id 목록 관리
    mapping(uint => ContractData) private contracts;
    uint[] private contractIds;

    // 새로운 계약서를 추가하는 함수
    // PaymentInfo 배열은 calldata로 입력받습니다.
    function addContract(
        uint _id,
        string calldata _startDate,
        string calldata _endDate,
        uint _rentTotalAmount,
        uint _rentDueDate,
        string calldata _rentAccountNo,
        string calldata _ownerAccountNo,
        uint _rentTotalRatio,
        PaymentInfo[] calldata _paymentInfos,
        string calldata _liveAccountNo, // 별도 필드
        bool _isUtilityEnabled,
        uint _utilitySplitRatio,
        uint _cardId
    ) external {
        require(contracts[_id].id == 0, "existing contract manager");

        // 새로운 ContractData 저장
        ContractData storage newContract = contracts[_id];
        newContract.id = _id;
        newContract.startDate = _startDate;
        newContract.endDate = _endDate;

        // 월세 정보 설정
        newContract.rent.totalAmount = _rentTotalAmount;
        newContract.rent.dueDate = _rentDueDate;
        newContract.rent.rentAccountNo = _rentAccountNo;
        newContract.rent.ownerAccountNo = _ownerAccountNo;
        newContract.rent.totalRatio = _rentTotalRatio;

        // 전달받은 그룹원 결제 정보를 복사
        for (uint i = 0; i < _paymentInfos.length; i++) {
            newContract.rent.userPaymentInfo.push(
                PaymentInfo({
                    userId: _paymentInfos[i].userId,
                    amount: _paymentInfos[i].amount,
                    ratio: _paymentInfos[i].ratio
                })
            );
        }

        // 생활비 계좌 설정 (top-level)
        newContract.liveAccountNo = _liveAccountNo;

        // 공과금 정보 설정
        newContract.utility.isEnabled = _isUtilityEnabled;
        newContract.utility.splitRatio = _utilitySplitRatio;
        newContract.utility.cardId = _cardId;

        // 추가된 계약 id 기록
        contractIds.push(_id);
    }

    // 특정 계약 id의 기본 정보를 조회하는 함수
    function getContractOverview(uint _id)
    external
    view
    returns (uint id, string memory startDate, string memory endDate)
    {
        require(contracts[_id].id != 0, "not found contract");
        ContractData storage c = contracts[_id];
        return (c.id, c.startDate, c.endDate);
    }

    // 전체 계약 정보를 조회하는 함수 예시
    function getFullContractData(uint _id)
    external
    view
    returns (
        uint id,
        string memory startDate,
        string memory endDate,
        uint rentTotalAmount,
        uint rentDueDate,
        string memory rentAccountNo,
        string memory ownerAccountNo,
        uint rentTotalRatio,
        PaymentInfo[] memory paymentInfos,
        string memory liveAccountNo,
        bool isUtilityEnabled,
        uint utilitySplitRatio,
        uint cardId
    )
    {
        require(contracts[_id].id != 0, "not found contract");
        ContractData storage c = contracts[_id];

        // ContractData 내의 값을 각각 반환합니다.
        id = c.id;
        startDate = c.startDate;
        endDate = c.endDate;
        rentTotalAmount = c.rent.totalAmount;
        rentDueDate = c.rent.dueDate;
        rentAccountNo = c.rent.rentAccountNo;
        ownerAccountNo = c.rent.ownerAccountNo;
        rentTotalRatio = c.rent.totalRatio;
        paymentInfos = c.rent.userPaymentInfo;
        liveAccountNo = c.liveAccountNo;
        isUtilityEnabled = c.utility.isEnabled;
        utilitySplitRatio = c.utility.splitRatio;
        cardId = c.utility.cardId;
    }

    // 특정 계약 id의 월세 정보를 조회하는 함수
    function getRentData(uint _id)
    external
    view
    returns (
        uint totalAmount,
        uint dueDate,
        string memory rentAccountNo,
        string memory ownerAccountNo,
        uint totalRatio,
        uint paymentInfoCount
    )
    {
        require(contracts[_id].id != 0, "not found contract");
        Rent storage r = contracts[_id].rent;
        return (
            r.totalAmount,
            r.dueDate,
            r.rentAccountNo,
            r.ownerAccountNo,
            r.totalRatio,
            r.userPaymentInfo.length
        );
    }

    // 특정 계약 id의 생활비 계좌(liveAccountNo)를 조회하는 함수
    function getLiveAccountNo(uint _id)
    external
    view
    returns (string memory)
    {
        require(contracts[_id].id != 0, "not found contract");
        return contracts[_id].liveAccountNo;
    }

    // 특정 계약 id의 공과금 정보를 조회하는 함수
    function getUtilityData(uint _id)
    external
    view
    returns (bool isEnabled, uint splitRatio, uint cardId)
    {
        require(contracts[_id].id != 0, "not found contract");
        Utility storage u = contracts[_id].utility;
        return (u.isEnabled, u.splitRatio, u.cardId);
    }

    // 특정 계약 id의 그룹원 결제 정보 개수를 반환하는 함수
    function getPaymentInfoCount(uint _id) external view returns (uint) {
        require(contracts[_id].id != 0, "not found contract");
        return contracts[_id].rent.userPaymentInfo.length;
    }

    // 특정 계약 id와 인덱스를 기반으로 그룹원 결제 정보를 조회하는 함수
    function getPaymentInfoByIndex(uint _id, uint index)
    external
    view
    returns (uint userId, uint amount, uint ratio)
    {
        require(contracts[_id].id != 0, "not found contract");
        require(index < contracts[_id].rent.userPaymentInfo.length, "index overflow");
        PaymentInfo storage info = contracts[_id].rent.userPaymentInfo[index];
        return (info.userId, info.amount, info.ratio);
    }

    // 저장된 전체 계약서를 조회하는 함수
    function getAllContracts() external view returns (ContractData[] memory) {
        ContractData[] memory allContracts = new ContractData[](contractIds.length);
        for (uint i = 0; i < contractIds.length; i++) {
            ContractData storage c = contracts[contractIds[i]];

            // PaymentInfo 배열을 메모리로 복사
            PaymentInfo[] memory paymentInfos = new PaymentInfo[](c.rent.userPaymentInfo.length);
            for (uint j = 0; j < c.rent.userPaymentInfo.length; j++) {
                PaymentInfo storage p = c.rent.userPaymentInfo[j];
                paymentInfos[j] = PaymentInfo({
                    userId: p.userId,
                    amount: p.amount,
                    ratio: p.ratio
                });
            }

            // 메모리 구조체에 복사 (liveAccountNo는 ContractData 최상위 필드)
            allContracts[i] = ContractData({
                id: c.id,
                startDate: c.startDate,
                endDate: c.endDate,
                rent: Rent({
                totalAmount: c.rent.totalAmount,
                dueDate: c.rent.dueDate,
                rentAccountNo: c.rent.rentAccountNo,
                ownerAccountNo: c.rent.ownerAccountNo,
                totalRatio: c.rent.totalRatio,
                userPaymentInfo: paymentInfos
            }),
                liveAccountNo: c.liveAccountNo,
                utility: Utility({
                isEnabled: c.utility.isEnabled,
                splitRatio: c.utility.splitRatio,
                cardId: c.utility.cardId
            })
            });
        }
        return allContracts;
    }

    // 특정 계약서의 생활비 계좌(liveAccountNo)를 업데이트하는 함수
    function updateLiveAccountNo(uint _id, string calldata _newLiveAccountNo) external {
        require(contracts[_id].id != 0, "not found contract");
        contracts[_id].liveAccountNo = _newLiveAccountNo;
    }
}