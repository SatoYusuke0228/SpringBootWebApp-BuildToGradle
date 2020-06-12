package net.code;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *Repositoryインターフェースに実行させたいメソッドを記述していく
 * @author SatoYusuke0228
 */
@Service
public class TrProductService {

	@Autowired
	private TrProductRipository productRepository;

	/**
	 * 商品テーブルの中身を全て取得するメソッド
	 */
	public List<TrProductEntity> findAll() {
		return productRepository.findAll();
	}

	/**
	 * 商品テーブルの中身をProductId別で取得するメソッド
	 */
	public TrProductEntity getItemInfo(String id) {
		return productRepository.getOne(id);
	}

	/**
	 * 商品検索をAND検索する為のヘルパー関数①
	 * 単一キーワードによる絞り込み条件を表すSpecificationを返す
	 * @param 入力キーワード
	 * @return 絞り込み条件
	 */
	private Specification<TrProductEntity> nameContains(String name) {
		return new Specification<TrProductEntity>() {
			@Override
			public Predicate toPredicate(Root<TrProductEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				return cb.like(root.get(TrProductEntity_.productName), "%" + name + "%");
			}
		};
	}

	/*
	 * 商品検索をAND検索する為のヘルパー関数②
	 * "word1 word2 word3"のようなクエリ文をばらして
	 * ["word1", "word2", "word3"]にするのに使用するヘルパー関数
	 */
	private List<String> splitQuery(String name) {
		final String space = " ";
		// 半角スペースと全角スペースの組み合わせのパターンを表す
		final String spacesPattern = "[\\s　]+";
		// 以上のパターンにマッチした部分を単一の半角スペースに変換する
		final String monoSpaceQuery = name.replaceAll(spacesPattern, space);
		// splitするとき、余分な空要素が生成されるのを防ぐため、先頭と末尾のスペースを削除する
		final String trimmedMonoSpaceQuery = monoSpaceQuery.trim();
		// 半角スペースでクエリをsplitする
		return Arrays.asList(trimmedMonoSpaceQuery.split("\\s"));
	}

	/*
	 * 商品検索をAND検索する為の検索条件を返す関数
	 * @param nameQuery 引数
	 * 検索ボックスの検索ワード
s	 * @return 検索条件
	 */
	public List<TrProductEntity> findByKeyword(String keyword) {
		// 複数キーワードに分割する
		final List<String> splittedKeyword = splitQuery(keyword);
		//何もしないSpecificationを生成する。
		//reduceの初期値として利用する
		//Specification.where()にnullを渡せば、何もしないSpecificationが生成される
		final Specification<TrProductEntity> zero = Specification
				.where((Specification<TrProductEntity>) null);
		// キーワードのリストをそれぞれSpecificationにマッピングして、andで結合する
		final Specification<TrProductEntity> spec = splittedKeyword
				.stream()
				.map(this::nameContains)
				.reduce(zero, Specification<TrProductEntity>::and);

		return productRepository.findAll(spec);
	}
}
