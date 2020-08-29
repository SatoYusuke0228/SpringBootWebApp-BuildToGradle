package net.sales_history;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stripe.model.Charge;

import lombok.Data;
import net.charge.ChargeRequest;
import net.checkout.Checkout;
import net.common.FormatTimestamp;

/***************************
 * 販売履歴テーブルのEntity *
 ***************************
 * @see src/main/resources/TrSalesHistory.sql
 * @author SatoYusuke0228
 */
@Table(name = "TR_SALES_HISTORY")
@Entity
@Data
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public class TrSalesHistoryEntity {

	@Id
	@Column(name = "SALES_HISTORY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーを自動生成する
	private long salesHistoryId;

	@Column(name = "TOTAL_SALES_AMOUNT")
	private long totalSalesAmount;

	/**
	 * 販売履歴テーブルの決済Flag
	 *
	 * @see SettlementFlagConverter
	 */
	@Column(name = "SETTLEMENT_FLAG")
	@Convert(converter = SettlementFlagConverter.class)
	private String settlementFlag;

	/**
	 * 購入者情報
	 */
	@Column(name = "CUSTOMER_NAME", nullable = false)
	private String customerName;

	@Column(name = "CUSTOMER_ZIPCODE", nullable = false)
	private String customerZipcode;

	@Column(name = "CUSTOMER_ADDRESS", nullable = false)
	private String customerAddress;

	@Column(name = "CUSTOMER_E_MAIL", nullable = false)
	private String customerEmail;

	/**
	 * 配送先情報
	 */
	@Column(name = "SHIPPING_NAME", nullable = false)
	private String shippingName;

	@Column(name = "SHIPPING_ZIPCODE", nullable = false)
	private String shippingZipcode;

	@Column(name = "SHIPPING_ADDRESS", nullable = false)
	private String shippingAddress;

	@Column(name = "SHIPPING_TELL", nullable = false)
	private String shippingTell;

	/**
	 * 決済情報
	 */

	@Column(name = "STRIPE_CHARGE_ID", nullable = true)
	private String stripeChargeId;

	@Column(name = "STRIPE_BALANCE_TRANSACTION_ID", nullable = true)
	private String stripeBalanceTransactionId;

	/**
	 * 販売日と決済日とキャンセル日と、その処理者名
	 */
	@Column(name = "SALES_DATE", nullable = false)
	private Timestamp salesDate;

	@Column(name = "SETTLEMENT_DATE", nullable = true)
	private Timestamp settlementDate;

	@Column(name = "SETTLEMENT_USER", nullable = true)
	private String settlementUser;

	@Column(name = "CANCELLETION_DATE", nullable = true)
	private Timestamp transactionCancellationDate;

	@Column(name = "CANCELLETION_USER", nullable = true)
	private String transactionCancellationUser;

	/**
	 * 販売した商品
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SALES_HISTORY_ID")
	private List<TrSalesProductHistoryEntity> salesProductHistoryEntity;

	/**
	 * 取引日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatSalesDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.salesDate);
	}

	/**
	 * 取引の決済完了日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatSettlementDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.settlementDate);
	}

	/**
	 * 取引のキャンセル日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatTransactionCancellationDate() {
		FormatTimestamp ft = new FormatTimestamp();
		return ft.formatTimestamp(this.transactionCancellationDate);
	}

	/**
	 * コンストラクタ
	 */
	public TrSalesHistoryEntity() {
	};

	/**
	 * コンストラクタ
	 *
	 * @param totalSalesAmount １つの取引の合計金額
	 * @param checkout 購入時の入力FORM
	 * @param chargeRequest 決済準備の情報
	 * @param charge 決済完了の情報
	 * @param salesDate 購入日
	 * @param settlementDate & User 決済日とその処理者
	 * @param settlementDate & User キャンセル日とその処理者
	 */
	public TrSalesHistoryEntity(
			String stripePaymentStatus,
			Charge charge,
			ChargeRequest chargeRequest,
			Checkout checkout,
			Timestamp salesDate) {

		this.totalSalesAmount = charge.getAmount();
		this.settlementFlag = stripePaymentStatus;

		this.customerName = charge.getBillingDetails().getName();
		this.customerZipcode = charge.getBillingDetails().getAddress().getPostalCode();
		this.customerAddress = charge.getBillingDetails().getAddress().getCity() + " "
									+ charge.getBillingDetails().getAddress().getLine1();
		this.customerEmail = chargeRequest.getStripeEmail();

		this.shippingName = checkout.getShippingFirstName() + checkout.getShippingLastName();
		this.shippingZipcode = checkout.getShippingZipcode();
		this.shippingAddress = checkout.getShippingMainAddress() + " " + checkout.getShippingBuildingAddress();
		this.shippingTell = checkout.getShippingTell();

		this.stripeChargeId = charge.getId();
		this.stripeBalanceTransactionId = charge.getBalanceTransaction();

		this.salesDate = salesDate;
		this.settlementDate = null;
		this.settlementUser = null;
		this.transactionCancellationDate = null;
		this.transactionCancellationUser = null;
	}
}