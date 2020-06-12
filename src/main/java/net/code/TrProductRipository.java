package net.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 商品テーブルRepositoryインターフェース
 * ジェネリクスの第１引数にはEntity
 * 第２引数にはEntityでIdアノテーションを付けたプロパティの型(主キーの型)を指定
 * @author SatoYusuke0228
 */
@Repository
public interface TrProductRipository extends
		JpaRepository<TrProductEntity, String> ,
		JpaSpecificationExecutor<TrProductEntity> {}