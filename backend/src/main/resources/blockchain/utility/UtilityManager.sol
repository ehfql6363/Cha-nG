// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract UtilityManager {

    // 거래 내역 구조체
    struct Utility {
        uint256 id;         // 내역 하나에 대한 PK
        uint256 accountId;  // 계좌 PK
        uint8 month;        // 이체가 발생한 달
        string from;        // 이체한 사람
        string to;          // 받는 통장
        uint256 amount;     // 금액
        bool status;        // 이체 성공 여부
        string time;        // 이체한 시간
    }

    // 계좌 ID를 키로 하고 해당 거래 내역 리스트를 값으로 저장하는 매핑
    mapping(uint256 => Utility[]) private accountTransactions;

    // 전체 거래 내역을 저장하기 위한 배열
    Utility[] private allTransactions;

    /**
     * @dev 새로운 거래 내역을 추가하는 메서드입니다.
     * _accountId는 외부 서버에서 관리하는 계좌 ID를 그대로 사용합니다.
     */
    function addTransaction(
        uint256 _id,
        uint256 _accountId,
        uint8 _month,
        string memory _from,
        string memory _to,
        uint256 _amount,
        bool _status,
        string memory _time
    ) public {
        Utility memory newTransaction = Utility({
            id: _id,
            accountId: _accountId,
            month: _month,
            from: _from,
            to: _to,
            amount: _amount,
            status: _status,
            time: _time
        });

        // 해당 계좌의 거래 내역 리스트에 추가
        accountTransactions[_accountId].push(newTransaction);
        // 전체 거래 내역에도 추가
        allTransactions.push(newTransaction);
    }

    /**
     * @dev 특정 계좌의 거래 내역을 조회하는 메서드입니다.
     */
    function getTransactionsByAccount(uint256 _accountId) public view returns (Utility[] memory) {
        return accountTransactions[_accountId];
    }

    /**
     * @dev 전체 거래 내역을 조회하는 메서드입니다.
     */
    function getAllTransactions() public view returns (Utility[] memory) {
        return allTransactions;
    }
}