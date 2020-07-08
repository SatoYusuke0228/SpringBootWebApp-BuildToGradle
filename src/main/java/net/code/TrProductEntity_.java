package net.code;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * 商品テーブルのメタモデルクラス
 *
 * アプリケーションの商品検索に使用する。
 * メタモデルクラスからカラム名を取得するやり方の方が
 * より変更に強く堅牢な作りになる（らしい）
 *
 * @author 佐藤由佑
 */

@StaticMetamodel(TrProductEntity.class)
public class TrProductEntity_ {

	public static volatile SingularAttribute<TrProductEntity, String> productId;
	public static volatile SingularAttribute<TrProductEntity, String> productName;
	public static volatile SingularAttribute<TrProductEntity, Integer> productPrice;
	public static volatile SingularAttribute<TrProductEntity, Integer> productCategoryId;
	public static volatile SingularAttribute<TrProductEntity, Integer> productStock;
	public static volatile SingularAttribute<TrProductEntity, String> productComment;
	public static volatile SingularAttribute<TrProductEntity, String> productPhotoFileName1;
	public static volatile SingularAttribute<TrProductEntity, String> productPhotoFileName2;
	public static volatile SingularAttribute<TrProductEntity, String> productPhotoFileName3;
	public static volatile SingularAttribute<TrProductEntity, Integer> productShowFlag;
	public static volatile SingularAttribute<TrProductEntity, Timestamp> insertDate;
	public static volatile SingularAttribute<TrProductEntity, String> insertUser;
	public static volatile SingularAttribute<TrProductEntity, Timestamp> updateDate;
	public static volatile SingularAttribute<TrProductEntity, String> updateUser;
	public static volatile SingularAttribute<TrProductEntity, Timestamp> deleteDate;
	public static volatile SingularAttribute<TrProductEntity, String> deleteUser;


	public static final String PRODUCT_ID = "productId";
	public static final String PRODUCT_Name = "productName";
	public static final String PRODUCT_SELLING_PRICE = "productPrice";
	public static final String PRODUCT_CATEGORY_ID = "productCategoryId";
	public static final String PRODUCT_STOCK = "productStock";
	public static final String PRODUCT_COMMENT= "productComment";
	public static final String PRODUCT_PHOTO_FILE_NAME1 = "productPhotoFileName1";
	public static final String PRODUCT_PHOTO_FILE_NAME2 = "productPhotoFileName2";
	public static final String PRODUCT_PHOTO_FILE_NAME3 = "productPhotoFileName3";
	public static final String PRODUCT_SHOW_FLAG = "productShowFlag";
	public static final String INSERT_DATE = "insertDate";
	public static final String INSERT_USER = "insertUser";
	public static final String UPDATE_DATE = "updateDate";
	public static final String UPDATE_USER = "updateUser";
	public static final String DELETE_DATE = "deleteDate";
	public static final String DELETE_USER = "deleteUser";
}