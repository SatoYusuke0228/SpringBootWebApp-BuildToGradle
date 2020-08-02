package net.sales_history;

import java.util.List;

public abstract interface TrSalesHistoryService {

	/**
	 * 販売履歴一覧をDBから全取得
	 *
	 * @return List型の販売履歴一覧
	 */
	public List<TrSalesHistoryEntity> findAll();

	/**
	 * 販売履歴に１件の履歴を追加するメソッド
	 *
	 * @param salesHistoryEntity
	 */
	public abstract void saveSalesHistory(TrSalesHistoryEntity salesHistoryEntity);
}