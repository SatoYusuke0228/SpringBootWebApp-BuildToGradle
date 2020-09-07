package net.common;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import net.sales_history.TrSalesProductHistoryEntity;

@Service
public class ChangeProductHistoryStatus {

	/**
	 * 1つの販売履歴の全ての販売商品履歴の配送ステータスを『引数１の文字列』に変更してDBに保存するメソッド
	 * @param tatusParam
	 * @param salesHistoryEntity
	 */
	public List<TrSalesProductHistoryEntity> changeSippingStatus(String statusParam, List<TrSalesProductHistoryEntity> salesProductHistoryEntity) {

		for (int i = salesProductHistoryEntity.size() - 1; 0 <= i; i--) {

			salesProductHistoryEntity.get(i).setShippingStatus(statusParam);
		}

		return salesProductHistoryEntity;
	}

	/**
	 * 1つの販売履歴の全ての販売商品履歴の返金日とその処理者名を入力してDBに保存するメソッド
	 * @param loginUserName
	 * @param timestamp
	 * @param salesProductHistoryEntity
	 */
	public List<TrSalesProductHistoryEntity> updateProductRefundData(String loginUserName, Timestamp timestamp, List<TrSalesProductHistoryEntity> salesProductHistoryEntity) {

		for (int i = salesProductHistoryEntity.size() - 1; 0 <= i; i--) {

			salesProductHistoryEntity.get(i).setProductCancellationUser(loginUserName);
			salesProductHistoryEntity.get(i).setProductCancellationDate(timestamp);
		}

		return salesProductHistoryEntity;
	}
}
