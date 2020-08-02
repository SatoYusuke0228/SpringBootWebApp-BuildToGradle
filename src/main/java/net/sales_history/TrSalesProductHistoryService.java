package net.sales_history;

import java.util.List;
import java.util.Optional;

public abstract interface TrSalesProductHistoryService {

	/**
	 * 販売商品履歴一覧をDBから取得
	 *
	 * @return List型の販売商品履歴の一覧
	 */
	public abstract List<TrSalesProductHistoryEntity> findAll();

	/**
	 * 販売履歴IDを元に商品販売履歴一覧をDBから取得
	 *
	 * @param id 販売履歴ID
	 * @return 販売履歴IDに応じた商品販売履歴
	 */
	public Optional<TrSalesProductHistoryEntity> findById(long id);

	/**
	 * 販売商品履歴一覧に新規で登録する
	 */
	public abstract void saveSalesHistory(TrSalesProductHistoryEntity salesProductHistoryEntity);
}