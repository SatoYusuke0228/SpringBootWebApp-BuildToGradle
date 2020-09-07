package net.sales_history;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Customer {

	@NotBlank(message = "※入力してください")
	@Size(max = 64, message = "※入力が長すぎます")
	private String name;

	@NotBlank(message = "※入力してください")
	@Pattern(regexp = "[0-9]{7}", message = "※7桁の数字のみ入力して下さい")
	private String zipcode;

	@NotBlank(message = "※入力してください")
	@Size(max = 128, message = "※入力が長すぎます")
	private String address;

	@NotBlank(message = "※入力してください")
	@Size(max = 128, message = "入力が長すぎます")
	@Email
	private String email;

	@NotBlank(message = "※入力してください")
	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String tell;

	/**
	 * コンストラクタ
	 */
	public Customer() {
	}

	/**
	 * コンストラクタ
	 *
	 * 取得した販売履歴の購入者情報を代入して購入者情報のみのオブジェクトを作成する
	 *
	 * @param customerName
	 * @param customerZipcode
	 * @param customerAddress
	 * @param customerEmail
	 * @param customerTell
	 */
	public Customer(TrSalesHistoryEntity salesHistoryEntity) {

		this.name = salesHistoryEntity.getCustomerName();
		this.zipcode = salesHistoryEntity.getCustomerZipcode();
		this.address = salesHistoryEntity.getCustomerAddress();
		this.email = salesHistoryEntity.getCustomerEmail();
		this.tell = salesHistoryEntity.getCustomerTell();
	}
}
