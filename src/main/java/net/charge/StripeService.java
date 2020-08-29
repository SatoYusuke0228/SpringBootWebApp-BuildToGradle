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
import com.stripe.net.RequestOptions;

import net.checkout.Checkout;

@Service
public class StripeService {

	@Value("${STRIPE__SECRET__KEY}")
	private String secretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = secretKey;
	}

	public Charge charge(
			Checkout checkout,
			ChargeRequest chargeRequest) throws StripeException {

		Map<String, Object> address = new HashMap<>();
		address.put("line1",checkout.getShippingBuildingAddress());
		address.put("city", checkout.getShippingMainAddress());
		address.put("postal_code", checkout.getShippingZipcode());

		Map<String, Object> shipping = new HashMap<>();
		shipping.put("name", checkout.getShippingFirstName() + checkout.getShippingLastName());
		shipping.put("phone", checkout.getShippingTell());
		shipping.put("address", address);

		Map<String, Object> chargeParams = new HashMap<>();

		chargeParams.put("amount", chargeRequest.getAmount());
		chargeParams.put("currency", chargeRequest.getCurrency());
		chargeParams.put("description", chargeRequest.getDescription());
		chargeParams.put("source", chargeRequest.getStripeToken());
		chargeParams.put("shipping", shipping);

		RequestOptions request = RequestOptions
				.builder()
				.setApiKey(secretKey)
				.build();

		return Charge.create(chargeParams, request);
	}

	public Refund refund(
			String stripeChargeId,
			long salesPrice) throws StripeException {

		Map<String, Object> params = new HashMap<>();

		params.put("charge", stripeChargeId);
		params.put("amount", salesPrice);

		RequestOptions request = RequestOptions
				.builder()
				.setApiKey(secretKey)
				.build();

		return Refund.create(params, request);
	}
}