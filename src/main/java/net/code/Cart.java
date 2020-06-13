package net.code;

import java.util.HashMap;
import java.util.Map;

/**
 * カート画面のバックエンド処理をまとめたクラス
 *
 * @param cartItems
 *  CartItemクラスのフィールドをマップに所持させる
 *
 * @param grandTotal
 *  合計購入金額
 *
 * @author SatoYusuke0228
 */
public class Cart {

	private Map<String, CartItem> cartItems;
	private int grandTotal;

	/**
	 *コンストラクタ
	 */
	public Cart() {
		cartItems = new HashMap<String, CartItem>();
		grandTotal = 0;
	}

	/**
	 *getter and setter
	 */
	public Map<String, CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<String, CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public int getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(int grandTotal) {
		this.grandTotal = grandTotal;
	}

	/*
	 * カートに商品を追加するメソッド
	 *
	 * @param item
	 *  引数 これからカートに追加する商品情報
	 *
	 * @param id
	 *  商品ID
	 *
	 * @param existingCartItem
	 *  すでにカート内に存在している商品情報
	 */
	public void addCartItem(CartItem item) {

		String id = item.getId();

		//すでに商品がカート内にある場合(containsKey(id) = true)
		if (cartItems.containsKey(id)) {
			//カート内に既にある商品IDを取得
			CartItem existingCartItem = cartItems.get(id);
			//カートに入れられていた商品数＋カートに入れた商品数を合算
			existingCartItem.setQuantity(existingCartItem.getQuantity() + item.getQuantity());
			//カートに反映させる
			cartItems.put(id, existingCartItem);
			//cartItems.put(id, existingCartItem);
		//まだ商品がカート内にない場合(containsKey(id) = false)
		} else {
			//カートに反映させる
			cartItems.put(id, item);
		}
		//カート内の商品の合計金額を計算
		updateGrandTotal();
	}

	/*
	 * カートの商品削除メソッド
	 *
	 * @param id
	 *  String
	 */
	public void removeCartItem(String id) {

		//商品IDを指定して削除
		cartItems.remove(id);

		//カート内の商品の合計金額を計算
		updateGrandTotal();
	}

	/**
	 * カート内の商品の合計金額を計算するメソッド
	 *
	 * @param grandTotal
	 *  商品の合計金額
	 */
	public void updateGrandTotal() {

		//とりあえず初期化
		grandTotal = 0;

		//カート内の商品を個数 * 商品単価で合計金額を算出
		for (CartItem item : cartItems.values()) {
			grandTotal += (item.getQuantity() * item.getPrice());
		}
	}
}
