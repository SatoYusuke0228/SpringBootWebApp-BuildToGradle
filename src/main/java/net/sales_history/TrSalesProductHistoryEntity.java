package net.sales_history;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.purchase.CartItem;

/**
 * 販売商品履歴テーブルのEntity
 *
 * SALES_HISTORY_ID
 * SALES_PRODUCT_ID
 * SALES_PRODUCT_NAME
 * SALES_PRODUCT_QUANTITY
 * SALES_PRODUCT_PRICE
 * PRODUCT_CANCELLETION_DATE
 * PRODUCT_CANCELLETION_USER
 *
 * @author SatoYusuke0228
 */
@Entity
@Data
@Table(name = "TR_SALES_PRODUCT_HISTORY")
public class TrSalesProductHistoryEntity {

	@Id
	@Column(name = "SALES_HISTORY_ID", nullable = false)
	private long salesHistoryId;

	@Column(name = "SALES_PRODUCT_ID", nullable = false)
	private String salesProductId;

	@Column(name = "SALES_PRODUCT_NAME", nullable = false)
	private String salesProductName;

	@Column(name = "SALES_PRODUCT_QUANTITY", nullable = false)
	private int salesProductQuantity;

	@Column(name = "SALES_PRODUCT_PRICE", nullable = false)
	private int salesProductPrice;

	/**
	 * 商品ごとのキャンセル日と、その処理をした管理者名
	 */

	@Column(name = "PRODUCT_CANCELLETION_DATE", nullable = true)
	private Timestamp productCancellationDate;

	@Column(name = "PRODUCT_CANCELLETION_USER", nullable = true)
	private String productCancellationUser;

	/**
	 * コンストラクタ
	 */
	public TrSalesProductHistoryEntity() {};

	/**
	 * コンストラクタ
	 *
	 * @param salesHistoryEntity 販売履歴テーブルのEntity
	 * @param cartItem カートの内の商品
	 */
	public TrSalesProductHistoryEntity(
			long salesHistoryId,
			CartItem cartItem) {

		this.salesHistoryId = salesHistoryId;
		this.salesProductId = cartItem.getId();
		this.salesProductName = cartItem.getName();
		this.salesProductQuantity = cartItem.getQuantity();
		this.salesProductPrice = cartItem.getPrice();

		this.productCancellationDate = null;
		this.productCancellationUser = null;
	}
}