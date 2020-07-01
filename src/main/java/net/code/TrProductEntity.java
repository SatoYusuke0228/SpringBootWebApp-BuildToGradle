package net.code;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 商品テーブルのフィールドの宣言及びカプセル化
 *
 * ◇商品テーブルの作成SQL文
 *
 * CREATE TABLE TR_PRODUCT (
 * PRODUCT_ID VARCHAR(16) NOT NULL PRIMARY KEY,
 * PRODUCT_NAME VARCHAR(128) NOT NULL,
 * PRODUCT_SELLING_PRICE INTEGER NOT NULL,
 * PRODUCT_CATEGORY_ID INTEGER NOT NULL,
 * PRODUCT_STOCK INTEGER NOT NULL,
 * PRODUCT_COMMENT VARCHAR(2048),
 * PRODUCT_PHOTO_FILE_NAME1 VARCHAR(256),
 * PRODUCT_PHOTO_FILE_NAME2 VARCHAR(256),
 * PRODUCT_PHOTO_FILE_NAME3 VARCHAR(256),
 * PRODUCT_SHOW_FLAG INTEGER NOT NULL,
 * INSERT_DATE TIMESTAMP NOT NULL,
 * INSERT_USER VARCHAR(64) NOT NULL,
 * UPDATE_DATE TIMESTAMP NOT NULL,
 * UPDATE_USER VARCHAR(64) NOT NULL,
 * DELETE_DATE TIMESTAMP,
 * DELETE_USER VARCHAR(64)
 * );
 *
 *
 * ◇外部キー(FK)の設定
 *
 * ALTER TABLE TR_PRODUCT
 * ADD FOREIGN KEY (PRODUCT_CATEGORY_ID)
 * REFERENCES MS_PRODUCT_CATEGORY_INVENTORY (PRODUCT_CATEGORY_ID)
 * ON UPDATE RESTRICT
 * ON DELETE RESTRICT
 * ;
 *
 * @author SatoYusuke0228
 */
@Entity
@Table(name = "TR_PRODUCT")
public class TrProductEntity {

	@Id
	@Column(name = "PRODUCT_ID", nullable = false, length = 16)
	@Getter
	@Setter
	private String productId;

	@Column(name = "PRODUCT_NAME", unique = true, nullable = false, length = 128)
	@Getter
	@Setter
	private String productName;

	@Column(name = "PRODUCT_SELLING_PRICE", nullable = false)
	@Getter
	@Setter
	private int productPrice;

	@Column(name = "PRODUCT_CATEGORY_ID", nullable = false)
	@Getter
	@Setter
	private int productCategoryId;

	@Column(name = "PRODUCT_STOCK", nullable = false)
	@Getter
	@Setter
	private int productStock;

	@Column(name = "PRODUCT_COMMENT", nullable = true, length = 2048)
	@Getter
	@Setter
	private String productComment;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME1", nullable = true, length = 256)
	@Getter
	@Setter
	private String productPhotoFileName1;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME2", nullable = true, length = 256)
	@Getter
	@Setter
	private String productPhotoFileName2;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME3", nullable = true, length = 256)
	@Getter
	@Setter
	private String productPhotoFileName3;

	@Column(name = "PRODUCT_SHOW_FLAG", nullable = false)
	@Getter
	@Setter
	private String productShowFlag;

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
}