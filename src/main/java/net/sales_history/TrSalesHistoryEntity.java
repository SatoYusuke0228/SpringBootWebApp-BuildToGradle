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
import net.common.FormatTimestamp;
import net.purchase.Cart;
import net.purchase.ChargeRequest;
import net.purchase.Checkout;

/**************************
 * 販売履歴テーブルのEntity *
 * @author SatoYusuke0228  *
 **************************

 CREATE TABLE
	TR_SALES_HISTORY (
		SALES_HISTORY_ID bigserial NOT NULL unique,
		TOTAL_SALES_AMOUNT INTEGER,
		SETTLEMENT_FLAG INTEGER DEFAULT 0,
		CUSTOMER_NAME VARCHAR(256) NOT NULL,
		CUSTOMER_ZIPCODE VARCHAR(7) NOT NULL,
		CUSTOMER_ADDRESS VARCHAR(256) NOT NULL,
		CUSTOMER_TELL VARCHAR(11) NOT NULL,
		CUSTOMER_E_MAIL VARCHAR(512) NOT NULL,
		STRIPE_CHARGE_ID VARCHAR(32),
		STRIPE_BALANCE_TRANSACTION_ID VARCHAR(32),
		SALES_DATE TIMESTAMP DEFAULT now() NOT NULL,
		SETTLEMENT_DATE TIMESTAMP,
		SETTLEMENT_USER VARCHAR(256),
		CANCELLETION_DATE TIMESTAMP,
		CANCELLETION_USER VARCHAR(256),
		PRIMARY KEY (SALES_HISTORY_ID)
 );

 *
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
	private int totalSalesAmount;

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

	@Column(name = "CUSTOMER_TELL", nullable = false)
	private String customerTell;

	@Column(name = "CUSTOMER_E_MAIL", nullable = false)
	private String customerEmail;

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
			Cart cart,
			Checkout checkout,
			ChargeRequest chargeRequest,
			Charge charge,
			Timestamp salesDate) {

		this.totalSalesAmount = cart.getGrandTotal();
		this.settlementFlag = stripePaymentStatus;

		this.customerName = checkout.getFirstName() + " " + checkout.getLastName();
		this.customerZipcode = checkout.getZipcode();
		this.customerAddress = checkout.getMainAddress() + " " + checkout.getBuildingAddress();
		this.customerTell = checkout.getTell();
		this.customerEmail = chargeRequest.getStripeEmail();

		this.stripeChargeId = charge.getId();
		this.stripeBalanceTransactionId = charge.getBalanceTransaction();

		this.salesDate = salesDate;
		this.settlementDate = null;
		this.settlementUser = null;
		this.transactionCancellationDate = null;
		this.transactionCancellationUser = null;
	}
}