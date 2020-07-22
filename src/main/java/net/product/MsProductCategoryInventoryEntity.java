package net.product;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 商品カテゴリーテーブルのフィールドの宣言及びカプセル化
 *
 * ◇商品カテゴリーテーブルの作成SQL文
 *
 * CREATE TABLE MS_PRODUCT_CATEGORY_INVENTORY (
 * PRODUCT_CATEGORY_ID INTEGER NOT NULL PRIMARY KEY,
 * PRODUCT_CATEGORY_NAME VARCHAR(64) NOT NULL,
 * INSERT_DATE TIMESTAMP DEFAULT now() NOT NULL,
 * INSERT_USER VARCHAR(64) NOT NULL,
 * UPDATE_DATE TIMESTAMP DEFAULT now() NOT NULL,
 * UPDATE_USER VARCHAR(64) NOT NULL,
 * DELETE_DATE TIMESTAMP,
 * DELETE_USER VARCHAR(64)
 * );
 *
 * ◇セクションの作成とオートインクリメントの設定
 *
 * CREATE SEQUENCE MS_PRODUCT_CATEGORY_INVENTORY_ID_SEQ
 * ;
 *
 * ALTER TABLE MS_PRODUCT_CATEGORY_INVENTORY
 * ALTER COLUMN PRODUCT_CATEGORY_ID
 * SET DEFAULT nextval('MS_PRODUCT_CATEGORY_INVENTORY_ID_SEQ')
 * ;
 *
 * ALTER SEQUENCE MS_PRODUCT_CATEGORY_INVENTORY_ID_SEQ
 * INCREMENT 1 MINVALUE 0 MAXVALUE 2147483647 RESTART 0
 * ;
 *
  ********************************************************************
 *   PRODUCT_CATEGORY_ID is 0 == PRODUCT_CATEGORY_NAME is 'コーヒー豆'     *
 *   PRODUCT_CATEGORY_ID is 1 == PRODUCT_CATEGORY_NAME is '抽出器具' *
 *   PRODUCT_CATEGORY_ID is 2 == PRODUCT_CATEGORY_NAME is 'その他'     *
  ********************************************************************
 *
 * @author SatoYusuke0228
 */

@Entity
@Table(name = "MS_PRODUCT_CATEGORY_INVENTORY")
public class MsProductCategoryInventoryEntity {

	@Id
	@Column(name = "PRODUCT_CATEGORY_ID", nullable = false)
	@Getter
	@Setter
	private int productCategoryId;

	@Column(name = "PRODUCT_CATEGORY_NAME", unique = true, nullable = false, length = 64)
	@Getter
	@Setter
	private String productCategoryName;

	@Column(name = "INSERT_DATE", nullable = false)
	@Getter
	@Setter
	private Timestamp insertDate;

	@Column(name = "INSERT_USER", nullable = false, length = 64)
	@Getter
	@Setter
	private String insertUser;

	@Column(name = "UPDATE_DATE", nullable = false)
	@Getter
	@Setter
	private Timestamp updateDate;

	@Column(name = "UPDATE_USER", nullable = false, length = 64)
	@Getter
	@Setter
	private String updateUser;

	@Column(name = "DELETE_DATE", nullable = true)
	@Getter
	@Setter
	private Timestamp deleteDate;

	@Column(name = "DELETE_USER", nullable = true, length = 64)
	@Getter
	@Setter
	private String deleteUser;

	/**
	 * @OneToMany(
	 * 		cascade  = 元が消えたら関連テーブルはどうするか,
	 * 		fetch    = 一緒に取り出すか
	 * 		mappedBy = 関連ドメインクラス
	 * )
	 */
	@OneToMany
	@JoinColumn(name="PRODUCT_CATEGORY_ID")
	@Getter
	@Setter
	private List<TrProductEntity> trProductEntity;
}
