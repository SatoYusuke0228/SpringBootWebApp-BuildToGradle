package net.sales_history;

import java.util.List;

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
	 * @param id 販売履歴ID + 商品ID
	 * @return 販売履歴IDに応じた商品販売履歴
	 */
	public abstract TrSalesProductHistoryEntity getOne(String id);

	/**
	 * 1件の販売商品履歴のUPDATEクエリ実行
	 */
	public abstract void saveAndFlush(TrSalesProductHistoryEntity salesProductHistory);

	/**
	 *販売商品履歴ListのUPDATEクエリ実行
	 */
	public abstract void saveAndFlusheSalesProductHistoryList(List<TrSalesProductHistoryEntity> salesProductHistoryEntity);

	/**
	 * １件の販売商品履歴をDBに新規登録する
	 */
	public abstract void save(TrSalesProductHistoryEntity salesProductHistory);

	/**
	 * 販売商品履歴ListをDBに新規登録する
	 */
	public abstract void saveSalesProductHistoryList(List<TrSalesProductHistoryEntity> salesProductHistoryEntity);

}