package net.product;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.common.FormatTimestamp;

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
 * @category
  ********************************************************************
 *   PRODUCT_CATEGORY_ID is 0 == PRODUCT_CATEGORY_NAME is 'コーヒー豆' *
 *   PRODUCT_CATEGORY_ID is 1 == PRODUCT_CATEGORY_NAME is '抽出器具'   *
 *   PRODUCT_CATEGORY_ID is 2 == PRODUCT_CATEGORY_NAME is 'その他'     *
  ********************************************************************
 *
 * @author SatoYusuke0228
 */

@Entity
@Data
@Table(name = "MS_PRODUCT_CATEGORY_INVENTORY")
public class MsProductCategoryInventoryEntity {

	@Id
	@Column(name = "PRODUCT_CATEGORY_ID", nullable = false)
	private int productCategoryId;

	@Column(name = "PRODUCT_CATEGORY_NAME", unique = true, nullable = false, length = 64)
	private String productCategoryName;

	@Column(name = "INSERT_DATE", nullable = false)
	private Timestamp insertDate;

	@Column(name = "INSERT_USER", nullable = false, length = 64)
	private String insertUser;

	@Column(name = "UPDATE_DATE", nullable = false)
	private Timestamp updateDate;

	@Column(name = "UPDATE_USER", nullable = false, length = 64)
	private String updateUser;

	@Column(name = "DELETE_DATE", nullable = true)
	private Timestamp deleteDate;

	@Column(name = "DELETE_USER", nullable = true, length = 64)
	private String deleteUser;

	/**
	 * INSERT文実行日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatInsertDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.insertDate);
	}

	/**
	 * 最終UPDATE文実行日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatUpdateDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.updateDate);
	}

	/**
	 * delete文実行日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatDaleteDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.deleteDate);
	}

	/**
	 * @OneToMany(
	 * 		mappedBy = 関連ドメインクラス
	 * 		cascade  = 元が消えたら関連テーブルはどうするか,
	 * 		fetch    = 一緒に取り出すか)
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="PRODUCT_CATEGORY_ID")
	@Getter
	@Setter
	private List<TrProductEntity> trProductEntity;
}
