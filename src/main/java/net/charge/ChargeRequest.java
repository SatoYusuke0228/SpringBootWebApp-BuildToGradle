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

	//解説(?)
	private String description;

	//Stripe アクセストークン
	private String stripeToken;

	//Stripe Email
	private String stripeEmail;
}