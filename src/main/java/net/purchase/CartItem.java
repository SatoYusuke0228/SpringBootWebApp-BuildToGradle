package net.purchase;

import lombok.Getter;
import lombok.Setter;
import net.product.TrProductEntity;

/**
 * カート画面のバックエンド処理に使用するEntity
 *
 * @param id
 *  商品ID
 *
 * @param name
 *  商品名
 *
 * @param quantity
 *  商品個数
 *
 * @param price
 *  商品単価
 *
 * @see net.purchase.Cart
 *
 * @author SatoYusuke0228
 */
public class CartItem {

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private int quantity;

	@Getter
	@Setter
	private int price;

	/**
	 * コンストラクタ
	 */
	public CartItem() {}

	public CartItem(TrProductEntity productEntity) {
		this.id = productEntity.getProductId();
		this.name = productEntity.getProductName();
		this.quantity = 1;
		this.price = productEntity.getProductPrice();
	}
}