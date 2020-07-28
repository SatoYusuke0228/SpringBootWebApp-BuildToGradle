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
//import net.purchase.CartItem;
//
///**
// * 販売履歴テーブルのEntity
// *
// * @author SatoYusuke0228
// */
//@Entity
//@Table(name = "TR_SALES_PRODUCT_HISTORY")
//public class TrSalesProductHistoryEntity {
//
//	@Id
//	@Column(name = "SALES_HISTORY_ID", nullable = false)
//	@Getter
//	private int salesHistoryId;
//
//	@Column(name = "SALES_DATE", nullable = false)
//	@Getter
//	private Timestamp salesDate;
//
//	@Column(name = "", nullable = false)
//	@Getter
//	private String salesProductId;
//
//	@Column(name = "", nullable = false)
//	@Getter
//	private String salesProductName;
//
//	@Column(name = "", nullable = false)
//	@Getter
//	private int salesProductQuantity;
//
//	@Column(name = "", nullable = false)
//	@Getter
//	private int salesProductPrice;
//
//	/**
//	 * ここからSetter
//	 */
//
//	public void setSalesHistoryId(TrSalesHistoryEntity salesHistoryEntity) {
//		this.salesHistoryId = salesHistoryEntity.getSalesHistoryId();
//	}
//
//	public void setSalesDate(TrSalesHistoryEntity salesHistoryEntity) {
//		this.salesDate = salesHistoryEntity.getSalesDate();
//	}
//
//	public void setSalesProductId(CartItem cartItem) {
//		this.salesProductId = cartItem.getId();
//	}
//
//	public void setSalesProductName(CartItem cartItem) {
//		this.salesProductName = cartItem.getName();
//	}
//
//	public void setSalesProductQuantity(CartItem cartItem) {
//		this.salesProductQuantity = cartItem.getQuantity();
//	}
//
//	public void setSalesProductPrice(CartItem cartItem) {
//		this.salesProductPrice = cartItem.getPrice();
//	}
//
//	/**
//	 * コンストラクタ
//	 */
//	public TrSalesProductHistoryEntity() {
//	}
//
//	/**
//	 * コンストラクタ
//	 */
//	public TrSalesProductHistoryEntity(
//			TrSalesHistoryEntity salesHistoryEntity,
//			CartItem cartItem) {
//
//		this.salesHistoryId = salesHistoryEntity.getSalesHistoryId();
//		this.salesDate = salesHistoryEntity.getSalesDate();
//		this.salesProductId = cartItem.getId();
//		this.salesProductName = cartItem.getName();
//		this.salesProductQuantity = cartItem.getQuantity();
//		this.salesProductPrice = cartItem.getPrice();
//	}
//}