package net.sales_history;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.cart.CartItem;
import net.common.FormatTimestamp;

/*******************************
 * 販売商品履歴テーブルのEntity *
 *******************************
 *@see src/main/resources/TrSalesProductHistory.sql
 *@author SatoYusuke0228
 */
@Entity
@Data
@Table(name = "TR_SALES_PRODUCT_HISTORY")
public class TrSalesProductHistoryEntity {

	@Id
	@Column(name = "SALES_PRODUCT_HISTORY_ID")
	private String salesProductHistoryId;

	@Column(name = "SALES_HISTORY_ID")
	private long salesHistoryId;

	@Column(name = "SHIPPING_STATUS")
	@Convert(converter = ShippingStatusConverter.class)
	private String shippingStatus;

	/*
	 * 商品関係
	 */

	@Column(name = "SALES_PRODUCT_ID", unique = true, nullable = false)
	private String salesProductId;

	@Column(name = "SALES_PRODUCT_NAME", nullable = false)
	private String salesProductName;

	@Column(name = "SALES_PRODUCT_QUANTITY", nullable = false)
	private int salesProductQuantity;

	@Column(name = "SALES_PRODUCT_PRICE", nullable = false)
	private int salesProductPrice;

	/*
	 * 商品ごとの発送日とキャンセル日と処理をした管理者名
	 */

	@Column(name = "PRODUCT_SHIPPING_DATE", nullable = true)
	private Timestamp productShippingDate;

	@Column(name = "PRODUCT_SHIPPING_USER", nullable = true)
	private String productShippingUser;

	@Column(name = "PRODUCT_CANCELLETION_DATE", nullable = true)
	private Timestamp productCancellationDate;

	@Column(name = "PRODUCT_CANCELLETION_USER", nullable = true)
	private String productCancellationUser;

	/**
	 * 販売商品の発送日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatProductShippingDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.productShippingDate);
	}

	/**
	 * 販売商品のキャンセル日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatProductCancellationDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.productCancellationDate);
	}

	/**
	 * コンストラクタ
	 */
	public TrSalesProductHistoryEntity() {
	};

	/**
	 * コンストラクタ
	 *
	 * @param salesHistoryId 販売履歴ID
	 * @param cartItem       購入されたカート内の商品
	 */
	public TrSalesProductHistoryEntity(long salesHistoryId, CartItem cartItem) {

		//コンストラクタの引数を元に商品の種類ごとに一意のIDを生成
		this.salesProductHistoryId = salesHistoryId + "(" + cartItem.getId() + ")";

		this.salesHistoryId = salesHistoryId;
		this.shippingStatus = "発送待ち";

		this.salesProductId = cartItem.getId();
		this.salesProductName = cartItem.getName();
		this.salesProductQuantity = cartItem.getQuantity();
		this.salesProductPrice = cartItem.getPrice() * cartItem.getQuantity();

		this.productShippingDate = null;
		this.productShippingUser = null;
		this.productCancellationDate = null;
		this.productCancellationUser = null;
	}
}