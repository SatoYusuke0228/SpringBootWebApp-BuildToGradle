package net.sales_history;

import java.util.List;

public abstract interface TrSalesHistoryService {

	/**
	 * 販売履歴一覧を販売履歴テーブルから全取得
	 *
	 * @return List型の販売履歴一覧
	 */
	public List<TrSalesHistoryEntity> findAll();

	/**
	 * 販売履歴テーブルから販売履歴IDを元に１件の販売履歴を取得
	 *
	 * @return
	 */
	TrSalesHistoryEntity getOne(long id);

	/**
	 * 販売履歴テーブルに１件の履歴を追加するメソッド
	 *
	 * @param salesHistoryEntity
	 */
	public abstract void saveSalesHistory(TrSalesHistoryEntity salesHistoryEntity);

	/**
	 * 販売履歴テーブルの１件の販売履歴を更新するメソッド
	 *
	 * @param salesHistoryEntity
	 */
	public void saveAndFlushSalesHistory(TrSalesHistoryEntity salesHistoryEntity);

	/**
	 * 『検索ワード』を元に売履歴一覧をDBから取得
	 *
	 * @param keyword 検索ワード
	 */
	public abstract List<TrSalesHistoryEntity> findByKeyword(String keyword);

}