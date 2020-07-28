package net.salesHistory;
//
//import java.sql.Timestamp;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//import org.springframework.data.annotation.Id;
//
//import lombok.Getter;
//import lombok.Setter;
//import net.purchase.Checkout;
//
///**
// * 販売履歴テーブルのEntity
// *
// * @author SatoYusuke0228
// */
//@Entity
//@Table(name = "TR_SALES_HISTORY")
//public class TrSalesHistoryEntity {
//
//	@Id
//	@Column(name = "SALES_HISTORY_ID", nullable = false)
//	@Getter
//	@Setter
//	private int salesHistoryId;
//
//	@Column(name = "SALES_DATE", nullable = false)
//	@Getter
//	@Setter
//	private Timestamp salesDate;
//
//	@Column(name = "TOTAL_SALES_AMOUNT", nullable = false)
//	@Getter
//	@Setter
//	private int totalSalesAmount;
//
//	/**
//	 * 購入者情報
//	 */
//	@Column(name = "CUSTOMER_NAME", nullable = false)
//	@Getter
//	private String name;
//
//	@Column(name = "CUSTOMER_ZIPCODE", nullable = false)
//	@Getter
//	private String zipcode;
//
//	@Column(name = "CUSTOMER_ADDRESS", nullable = false)
//	@Getter
//	private String address;
//
//	@Column(name = "CUSTOMER_TELL", nullable = false)
//	@Getter
//	private String tell;
//
//	@Column(name = "CUSTOMER_E_MAIL", nullable = false)
//	@Getter
//	private String email;
//
//	/**
//	 * 購入者情報クレジットカード情報
//	 */
//	@Column(name = "CUSTOMER_CREDIT_CARD_NUM", nullable = false)
//	private String creditCardNum;
//
//	@Column(name = "CUSTOMER_CREDIT_CARD_NAME", nullable = false)
//	private String creditCardName;
//
//	@Column(name = "CUSTOMER_CREDIT_CARD_MONTH", nullable = false)
//	private String creditCardMonth;
//
//	@Column(name = "CUSTOMER_CREDIT_CARD_TEAR", nullable = false)
//	private String creditCardYear;
//
//	@Column(name = "CUSTOMER_CREDIT_CARD_CVS", nullable = false)
//	private String creditCardCvs;
//
//	/**
//	 * @Setter
//	 *
//	 * Checkoutクラス参照
//	 * 購入者用の入力FORMから値を取得する。
//	 */
//	public void setFirstName(Checkout checkout) {
//		this.name = checkout.getFirstName() + checkout.getLastName();
//	}
//
//	public void setZipcode(Checkout checkout) {
//		this.zipcode = checkout.getZipcode();
//	}
//
//	public void setMainAddress(Checkout checkout) {
//		this.address = checkout.getMainAddress() + checkout.getBuildingAddress();
//	}
//
//	public void setTell(Checkout checkout) {
//		this.tell = checkout.getTell();
//	}
//
//	public void setEmail(Checkout checkout) {
//		this.email = checkout.getEmail();
//	}
//
//	public void setCreditCardNum(Checkout checkout) {
//		this.creditCardNum = checkout.getCreditCardNum();
//	}
//
//	public void setCreditCardName(Checkout checkout) {
//		this.creditCardName = checkout.getCreditCardName();
//	}
//
//	public void setCreditCardMonth(Checkout checkout) {
//		this.creditCardMonth = checkout.getCreditCardMonth();
//	}
//
//	public void setCreditCardYear(Checkout checkout) {
//		this.creditCardYear = checkout.getCreditCardYear();
//	}
//
//	public void setCreditCardCvs(Checkout checkout) {
//		this.creditCardCvs = checkout.getCreditCardCvs();
//	}
//
//	public TrSalesHistoryEntity() {}
//
//	public TrSalesHistoryEntity(Timestamp salesDate, int totalSalesAmount, Checkout checkout) {
//
//		this.salesDate = salesDate;
//		this.totalSalesAmount = totalSalesAmount;
//		this.name = checkout.getFirstName() + checkout.getLastName();
//		this.zipcode = checkout.getZipcode();
//		this.address = checkout.getMainAddress() + checkout.getBuildingAddress();
//		this.tell = checkout.getTell();
//		this.email = checkout.getEmail();
//		this.creditCardNum = checkout.getCreditCardNum();
//		this.creditCardName = checkout.getCreditCardName();
//		this.creditCardMonth = checkout.getCreditCardMonth();
//		this.creditCardYear = checkout.getCreditCardYear();
//		this.creditCardCvs = checkout.getCreditCardCvs();
//	}
//}