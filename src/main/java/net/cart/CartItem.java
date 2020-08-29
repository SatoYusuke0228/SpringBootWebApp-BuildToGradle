package net.cart;

import lombok.Data;
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
 * @see net.cart.Cart
 *
 * @author SatoYusuke0228
 */
@Data
public class CartItem {

	private String id;

	private String name;

	private int quantity;

	private int price;

	public CartItem(TrProductEntity productEntity) {
		this.id = productEntity.getProductId();
		this.name = productEntity.getProductName();
		this.quantity = 1;
		this.price = productEntity.getProductPrice();
	}
}