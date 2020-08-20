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

import lombok.Data;
import net.common.FormatTimestamp;
import net.purchase.Cart;
import net.purchase.Checkout;

/**
 * 販売履歴テーブルのEntity
 *
 * SALES_HISTORY_ID
 * TOTAL_SALES_AMOUNT
 * SETTLEMENT_FLAG
 * CUSTOMER_NAME
 * CUSTOMER_ZIPCODE
 * CUSTOMER_ADDRESS
 * CUSTOMER_TELL
 * CUSTOMER_EMAIL
 * CUSTOMER_CREDITCARD_NUM
 * CUSTOMER_CREDITCARD_NAMECUSTOMER_CREDIT_CARD_MONTH
 * CUSTOMER_CREDITCARD_YEAR
 * CUSTOMER_CREDITCARD_CVS
 * SALES_DATE
 * SETTLEMENT_DATE
 * SETTLEMENT_USER
 * CANCELLETION_DATE
 * CANCELLETION_USER
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
	 * @see  0 == 未決済
	 * @see  1 == 決済完了
	 * @see -1 == キャンセル
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
	 * 購入者クレジットカード情報
	 */
	@Column(name = "CUSTOMER_CREDIT_CARD_NUM", nullable = false)
	private String customerCreditCardNum;

	@Column(name = "CUSTOMER_CREDIT_CARD_NAME", nullable = false)
	private String customerCreditCardName;

	@Column(name = "CUSTOMER_CREDIT_CARD_MONTH", nullable = false)
	private String customerCreditCardMonth;

	@Column(name = "CUSTOMER_CREDIT_CARD_YEAR", nullable = false)
	private String customerCreditCardYear;

	@Column(name = "CUSTOMER_CREDIT_CARD_CVS", nullable = false)
	private String customerCreditCardCvs;

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
	 * @param salesDate 購入日
	 * @param settlementDate & User 決済日とその処理者
	 * @param settlementDate & User キャンセル日とその処理者
	 */
	public TrSalesHistoryEntity(
			Cart cart,
			Checkout checkout,
			Timestamp salesDate) {

		this.totalSalesAmount = cart.getGrandTotal();
		this.settlementFlag = "未決済";

		this.customerName = checkout.getFirstName() + " " + checkout.getLastName();
		this.customerZipcode = checkout.getZipcode();
		this.customerAddress = checkout.getMainAddress() + " " + checkout.getBuildingAddress();
		this.customerTell = checkout.getTell();
		this.customerEmail = checkout.getEmail();

		this.customerCreditCardNum = checkout.getCreditCardNum();
		this.customerCreditCardName = checkout.getCreditCardName();
		this.customerCreditCardMonth = checkout.getCreditCardMonth();
		this.customerCreditCardYear = checkout.getCreditCardYear();
		this.customerCreditCardCvs = checkout.getCreditCardCvs();

		this.salesDate = salesDate;
		this.settlementDate = null;
		this.settlementUser = null;
		this.transactionCancellationDate = null;
		this.transactionCancellationUser = null;
	}
}