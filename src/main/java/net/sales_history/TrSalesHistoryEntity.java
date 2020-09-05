package net.sales_history;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import net.cart.Cart;
import net.charge.TrChargeHistoryEntity;
import net.checkout.Checkout;
import net.common.FormatTimestamp;

/***************************
 * 販売履歴テーブルのEntity *
 ***************************
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

	@Column(name = "SALES_AMOUNT")
	private long salesAmount;

	@Column(name = "REFUND_AMOUNT")
	private long refundAmount;

	/**
	 * 販売履歴テーブルの決済Flag
	 *
	 * @see SettlementFlagConverter
	 */
	@Column(name = "SETTLEMENT_FLAG", nullable = false)
	@Convert(converter = SettlementFlagConverter.class)
	private String settlementFlag;

	/*
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

	@Column(name = "CUSTOMER_TELL", nullable = false)
	private String customerTell;

	/*
	 * 配送先情報
	 */

	@Column(name = "SHIPPING_NAME", nullable = false)
	private String shippingName;

	@Column(name = "SHIPPING_ZIPCODE", nullable = false)
	private String shippingZipcode;

	@Column(name = "SHIPPING_ADDRESS", nullable = false)
	private String shippingAddress;

	@Column(name = "SHIPPING_E_MAIL", nullable = true)
	private String shippingEmail;

	@Column(name = "SHIPPING_TELL", nullable = true)
	private String shippingTell;

	/*
	 * 販売日と決済日とキャンセル日と返金日と、その処理者名
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

	@Column(name = "REFUND_DATE", nullable = true)
	private Timestamp refundDate;

	@Column(name = "REFUND_USER", nullable = true)
	private String refundUser;

	/**
	 * 決済者情報
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "TR_CHARGE_HISTORY", joinColumns = {
			@JoinColumn(name = "SALES_HISTORY_ID", referencedColumnName = "SALES_HISTORY_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "SALES_HISTORY_ID", referencedColumnName = "SALES_HISTORY_ID") })
	private TrChargeHistoryEntity chargeHistoryEntity;

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
		return new FormatTimestamp().formatTimestamp(this.salesDate);
	}

	/**
	 * 取引の決済完了日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatSettlementDate() {
		return new FormatTimestamp().formatTimestamp(this.settlementDate);
	}

	/**
	 * 取引のキャンセル日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatTransactionCancellationDate() {
		return new FormatTimestamp().formatTimestamp(this.transactionCancellationDate);
	}

	/**
	 * 返金処理日時を取得
	 * ※ Timestamp → String に変換
	 */
	public String getFormatRefundDate() {
		return new FormatTimestamp().formatTimestamp(this.refundDate);
	}

	/**
	 * コンストラクタ
	 */
	public TrSalesHistoryEntity() {
	}

	/**
	 * コンストラクタ
	 *
	 * @param salesAmount １つの取引の合計金額
	 * @param checkout 購入時の入力FORM
	 * @param chargeRequest 決済準備の情報
	 * @param charge 決済完了の情報
	 * @param salesDate 購入日
	 * @param settlementDate & User 決済日とその処理者
	 * @param settlementDate & User キャンセル日とその処理者
	 */
	public TrSalesHistoryEntity(
			Cart cart,
			Checkout checkout,
			Timestamp salesDate) {

		this.salesAmount = cart.getGrandTotal();
		this.settlementFlag = "決済待ち";

		//購入者住所ビル名のnullを排除
		String custamerAddress = Stream.of(checkout.getCustomerMainAddress(), checkout.getCustomerBuildingAddress())
				.filter(s -> s != null)
				.collect(Collectors.joining());

		this.customerName = checkout.getCustomerFirstName() + " " + checkout.getCustomerLastName();
		this.customerZipcode = checkout.getCustomerZipcode();
		this.customerAddress = custamerAddress;
		this.customerTell = checkout.getCustomerTell();
		this.customerEmail = checkout.getCustomerEmail();

		//配送先住所ビル名のnullを排除
		String shippingAddress = Stream.of(checkout.getShippingMainAddress(), checkout.getShippingBuildingAddress())
				.filter(s -> s != null)
				.collect(Collectors.joining());

		this.shippingName = checkout.getShippingFirstName() + " " + checkout.getShippingLastName();
		this.shippingZipcode = checkout.getShippingZipcode();
		this.shippingAddress = shippingAddress;
		this.shippingTell = checkout.getShippingTell();
		this.shippingEmail = checkout.getShippingEmail();

		this.salesDate = salesDate;
		this.settlementDate = null;
		this.settlementUser = null;
		this.transactionCancellationDate = null;
		this.transactionCancellationUser = null;
	}
}