package net.charge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.stripe.model.Charge;

import lombok.Data;
import net.sales_history.TrSalesHistoryEntity;

@Table(name = "TR_CHARGE_HISTORY")
@Entity
@Data
public class TrChargeHistoryEntity {

	@Id
	@Column(name = "SALES_HISTORY_ID", nullable = false)
	private long salesHistoryId;

	@Column(name = "STRIPE_CHARGE_ID", nullable = false)
	private String stripeChargeId;

	@Column(name = "STRIPE_BALANCE_TRANSACTION_ID", nullable = false)
	private String stripeBalanceTransactionId;

	@Column(name = "STRIPE_CARD_ID", nullable = false)
	private String stripeCardId;

	/**
	 * 決済者情報
	 */

	@Column(name = "BILLING_NAME", nullable = false)
	private String billingName;

	@Column(name = "BILLING_ZIPCODE", nullable = false)
	private String billingZipcode;

	@Column(name = "BILLING_ADDRESS", nullable = false)
	private String billingAddress;

	@Column(name = "BILLING_E_MAIL", nullable = false)
	private String billingEmail;

	@OneToOne(mappedBy = "chargeHistoryEntity")
	private TrSalesHistoryEntity salesHistoryEntity;

	public TrChargeHistoryEntity() {
	}

	/**
	 * コンストラクタ
	 *
	 * @param salesHistoryId
	 * @param stripeChargeId
	 * @param stripeBalanceTransactionId
	 * @param stripeCardId
	 * @param billingName
	 * @param billingZipcode
	 * @param billingAddress
	 * @param billingEmail
	 */
	public TrChargeHistoryEntity(long salesHistoryId, Charge charge, ChargeRequest request) {

		this.salesHistoryId = salesHistoryId;
		this.stripeChargeId = charge.getId();
		this.stripeBalanceTransactionId = charge.getBalanceTransaction();
		this.stripeCardId = charge.getSource().getId();

		this.billingName = charge.getBillingDetails().getName();
		this.billingZipcode = charge.getBillingDetails().getAddress().getPostalCode();
		this.billingAddress = charge.getBillingDetails().getAddress().getCity()
				+ charge.getBillingDetails().getAddress().getLine1();
		this.billingEmail = request.getStripeEmail();
	}
}
