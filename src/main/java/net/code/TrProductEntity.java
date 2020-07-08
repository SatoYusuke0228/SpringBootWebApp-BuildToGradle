package net.code;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

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
@Data
public class TrProductEntity {

	@Id
	@Column(name = "PRODUCT_ID", unique = true, nullable = false, length = 16)
	@NotBlank(message = "入力してください")
	@Size(max = 16, message = "入力が長すぎます")
	private String productId;

	@Column(name = "PRODUCT_NAME", unique = true, nullable = false, length = 128)
	@NotBlank(message = "入力してください")
	@Size(max = 128, message = "入力が長すぎます")
	private String productName;

	@Column(name = "PRODUCT_SELLING_PRICE", nullable = false)
	@NotBlank(message = "入力してください")
	@Pattern(regexp = "[1-9]", message = "入力内容が間違っています")
	private int productPrice;

	@Column(name = "PRODUCT_CATEGORY_ID", nullable = false)
	@NotBlank
	private int productCategoryId;

	@Column(name = "PRODUCT_STOCK", nullable = false)
	@NotBlank(message = "入力してください")
	@Pattern(regexp = "d\\", message = "入力内容が間違っています")
	private int productStock;

	@Column(name = "PRODUCT_COMMENT", nullable = true, length = 2048)
	@Size(max = 2048, message ="入力が長すぎます")
	private String productComment;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME1", nullable = true, length = 256)
	@Size(max = 256, message ="入力が長すぎます")
	private String productPhotoFileName1;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME2", nullable = true, length = 256)
	@Size(max = 256, message ="入力が長すぎます")
	private String productPhotoFileName2;

	@Column(name = "PRODUCT_PHOTO_FILE_NAME3", nullable = true, length = 256)
	@Size(max = 256, message ="入力が長すぎます")
	private String productPhotoFileName3;

	@Column(name = "PRODUCT_SHOW_FLAG", nullable = false)
	@NotBlank
	private int productShowFlag;

	@Column(name = "INSERT_DATE", nullable = false)
	@NotBlank
	private Timestamp insertDate;

	@Column(name = "INSERT_USER", nullable = false, length = 64)
	@NotBlank(message = "入力してください")
	@Size(max = 64, message ="入力が長すぎます")
	private String insertUser;

	@Column(name = "UPDATE_DATE", nullable = false)
	@NotBlank
	private Timestamp updateDate;

	@Column(name = "UPDATE_USER", nullable = false, length = 64)
	@NotBlank(message = "入力してください")
	@Size(max = 64, message ="入力が長すぎます")
	private String updateUser;

	@Column(name = "DELETE_DATE", nullable = true)
	private Timestamp deleteDate;

	@Column(name = "DELETE_USER", nullable = true, length = 64)
	private String deleteUser;

	public TrProductEntity() {}

	/**
	 * コンストラクタ
	 *
	 * @param productId
	 * @param productName
	 * @param productPrice
	 * @param productCategoryId
	 * @param productStock
	 * @param productComment
	 * @param productPhotoFileName1
	 * @param productPhotoFileName2
	 * @param productPhotoFileName3
	 * @param productShowFlag
	 * @param insertDate
	 * @param insertUser
	 * @param updateDate
	 * @param updateUser
	 * @param deleteDate
	 * @param deleteUser
	 */
	public TrProductEntity(
			String productId,
			String productName,
			int productPrice,
			int productCategoryId,
			int productStock,
			String productComment,
			String productPhotoFileName1,
			String productPhotoFileName2,
			String productPhotoFileName3,
			int productShowFlag,
			Timestamp insertDate,
			String insertUser,
			Timestamp updateDate,
			String updateUser,
			Timestamp deleteDate,
			String deleteUser) {

		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productCategoryId = productCategoryId;
		this.productStock = productStock;
		this.productComment = productComment;
		this.productPhotoFileName1 = productPhotoFileName1;
		this.productPhotoFileName2 = productPhotoFileName2;
		this.productPhotoFileName3 = productPhotoFileName3;
		this.productShowFlag = productShowFlag;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
		this.deleteDate = deleteDate;
		this.deleteUser = deleteUser;
	}
}