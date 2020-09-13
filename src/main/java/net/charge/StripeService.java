package net.charge;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;

import net.checkout.Checkout;

@Service
public class StripeService {

	@Value("${STRIPE__SECRET__KEY}")
	private String secretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = secretKey;
	}

//	public Map<String, Object> setBillingDetails(Checkout checkout) {
//
//		//購入者住所の情報をまとめる
//		Map<String, Object> billingAddress = new HashMap<>();
//		billingAddress.put("line1", checkout.getBillingBuildingAddress());
//		billingAddress.put("city", checkout.getBillingMainAddress());
//		billingAddress.put("postal_code", checkout.getBillingZipcode());
//
//		//購入者情報をまとめる
//		Map<String, Object> billingDetails = new HashMap<>();
//		billingDetails.put("name", checkout.getBillingFirstName() + " " + checkout.getBillingLastName());
//		billingDetails.put("phone", checkout.getBillingTell());
////		billingDetails.put("email", chargeRequest.getStripeEmail());
//		billingDetails.put("address", billingAddress);
//
//		return billingDetails;
//	}

	/**
	 * StripeAPIのChargeオブジェクトを作成するメソッド
	 * 決済することができる。
	 *
	 * @param checkout
	 * @param chargeRequest
	 * @return
	 * @throws StripeException
	 */
	public Charge charge(
			Checkout checkout,
			ChargeRequest chargeRequest) throws StripeException {

		//配送先住所の情報をまとめる
		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("line1", checkout.getShippingBuildingAddress());
		shippingAddress.put("city", checkout.getShippingMainAddress());
		shippingAddress.put("postal_code", checkout.getShippingZipcode());

		//配送先情報をまとめる
		Map<String, Object> shipping = new HashMap<>();
		shipping.put("name", checkout.getShippingFirstName() + " " + checkout.getShippingLastName());
		shipping.put("phone", checkout.getShippingTell());
		shipping.put("address", shippingAddress);

		//Charge.create()の引数に設定するchargeParamsを全てまとめる
		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put("amount", chargeRequest.getAmount());
		chargeParams.put("currency", chargeRequest.getCurrency());
		chargeParams.put("description", chargeRequest.getDescription());
		chargeParams.put("source", chargeRequest.getStripeToken());
		chargeParams.put("shipping", shipping);

		return Charge.create(chargeParams);
	}

	/**
	 * StripeAPIのRefundオブジェクトを作成するメソッド
	 * 返金することができる。
	 *
	 * @param stripeChargeId
	 * @param salesPrice
	 * @return
	 * @throws StripeException
	 */
	public Refund refund(
			String stripeChargeId,
			long refundPrice) throws StripeException {

		//チャージIDと返金額セット
		Map<String, Object> params = new HashMap<>();
		params.put("charge", stripeChargeId);
		params.put("amount", refundPrice);

//		//リクエスト情報にシークレットキーをセットする
//		RequestOptions request = RequestOptions
//				.builder()
//				.setApiKey(secretKey)
//				.build();

		return Refund.create(params);
	}
}