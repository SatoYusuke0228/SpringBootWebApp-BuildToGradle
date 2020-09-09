package net.charge;

import lombok.Data;

@Data
public class ChargeRequest {

	//通貨単位
	public enum Currency {
		JPY;
	}

	//購入合計金額
	private int amount;

	//通貨
	private Currency currency;

	//Stripeの取引ごとの説明文のようなモノ
	private String description;

	//Stripe アクセストークン
	private String stripeToken;

	//Stripe 請求先のEmail
	//こちら側で処理しなければ"receipt_email"には設定されないことに注意
	private String stripeEmail;
}