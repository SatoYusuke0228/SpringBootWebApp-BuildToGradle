package net.sales_history;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TrSalesHistoryServiceImpl implements TrSalesHistoryService {

	@Autowired
	TrSalesHistoryRepository salesHistoryRepository;

	@Override
	public List<TrSalesHistoryEntity> findAll() {

		List<TrSalesHistoryEntity> salesHistoryEntity = salesHistoryRepository.findAll();

		return salesHistoryEntity;
	}

	@Override
	public TrSalesHistoryEntity getOne(long id) {

		return salesHistoryRepository.getOne(id);
	}

	@Override
	public void saveSalesHistory(TrSalesHistoryEntity salesHistoryEntity) {

		salesHistoryRepository.save(salesHistoryEntity);
	}

	@Override
	public void saveAndFlushSalesHistory(TrSalesHistoryEntity salesHistoryEntity) {

		salesHistoryRepository.saveAndFlush(salesHistoryEntity);
	}

	/**
	 * 販売商品履歴をAND検索する為の検索条件を返す関数
	 * @param nameQuery 引数
	 * 検索ボックスの検索ワード
	 * @return 検索条件
	 */
	@Override
	public List<TrSalesHistoryEntity> findByKeyword(String keyword) {

		// 複数キーワードに分割する
		final List<String> splittedKeyword = splitQuery(keyword);

		//何もしないSpecificationを生成する。
		//reduceの初期値として利用する
		//Specification.where()にnullを渡せば、何もしないSpecificationが生成される
		final Specification<TrSalesHistoryEntity> zero = Specification
				.where((Specification<TrSalesHistoryEntity>) null);

		// キーワードのリストをそれぞれSpecificationにマッピングして、andで結合する
		final Specification<TrSalesHistoryEntity> spec = splittedKeyword
				.stream()
				.map(this::nameContains)
				.reduce(zero, Specification<TrSalesHistoryEntity>::and);

		return salesHistoryRepository.findAll(spec);
	}

	/**
	 * 販売商品履歴をAND検索する為のヘルパー関数①
	 * 単一キーワードによる絞り込み条件を表すSpecificationを返す
	 * @param 入力キーワード
	 * @return 絞り込み条件
	 */
	public Specification<TrSalesHistoryEntity> nameContains(String keyword) {

		return Specification
				//販売履歴IDをequal検索
				.where(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						try { //Long型にパースする
							Long.parseLong(keyword);
						} catch (NumberFormatException e) {
							return cb.like(root.get("customerName"), "%" + keyword + "%");
						}
						return cb.equal(root.get("salesHistoryId"), keyword);
					}
				})
				//購入者名をlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.like(root.get("customerName"), "%" + keyword.toLowerCase() + "%");
					}
				})
				//郵便番号をequal検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.equal(root.get("customerZipcode"), keyword);
					}
				})
				//住所からlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.like(root.get("customerAddress"), "%" + keyword.toLowerCase() + "%");
					}
				})
				//電話番号をequal検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.equal(root.get("customerTell"), keyword);
					}
				})
				//Emailアドレスをlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.like(root.get("customerEmail"), "%" + keyword.toLowerCase() + "%");
					}
				})
				//決済処理者をlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.like(root.get("settlementUser"), "%" + keyword.toLowerCase() + "%");
					}
				})
				//キャンセル処理者をlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						return cb.like(root.get("transactionCancellationUser"), "%" + keyword.toLowerCase() + "%");
					}
				})
				//商品キャンセル処理者をlike検索
				.or(new Specification<TrSalesHistoryEntity>() {
					@Override
					public Predicate toPredicate(Root<TrSalesHistoryEntity> root,
							CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						query.distinct(true); //同一IDで複数データある場合は１つだけ抽出
						return cb.like(root.join("salesProductHistoryEntity", JoinType.LEFT)
								.get("productCancellationUser"), "%" + keyword.toLowerCase() + "%");
					}
				});
	}

	/**
	 * 販売商品履歴をAND検索する為のヘルパー関数②
	 * "word1 word2 word3"のようなクエリ文をばらして
	 * ["word1", "word2", "word3"]にするのに使用するヘルパー関数
	 */
	private List<String> splitQuery(String keyword) {

		// 以上のパターンにマッチした部分を単一の半角スペースに変換する
		final String monoSpaceQuery = keyword.replaceAll("[\\s　]+", " ");

		// splitするとき、余分な空要素が生成されるのを防ぐため、先頭と末尾のスペースを削除する
		final String trimmedMonoSpaceQuery = monoSpaceQuery.trim();

		// 半角スペースでクエリをsplitする
		return Arrays.asList(trimmedMonoSpaceQuery.split("\\s"));
	}

}
