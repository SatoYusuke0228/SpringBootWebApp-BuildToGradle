package net.sales_history;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Shipping {

	@NotBlank(message = "※入力してください")
	@Size(max = 64, message = "※入力が長すぎます")
	private String name;

	@NotBlank(message = "※入力してください")
	@Pattern(regexp = "[0-9]{7}", message = "※7桁の数字のみ入力して下さい")
	private String zipcode;

	@NotBlank(message = "※入力してください")
	@Size(max = 128, message = "※入力が長すぎます")
	private String address;

	@Size(max = 128, message = "入力が長すぎます")
	@Email
	private String email;

	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String tell;

	/**
	 * コンストラクタ
	 */
	public Shipping() {}

	/**
	 * @param zipcode
	 * @param address
	 * @param email
	 * @param tell
	 * @param name
	 */
	public Shipping(TrSalesHistoryEntity salesHistoryEntity) {

		this.name = salesHistoryEntity.getShippingName();
		this.zipcode = salesHistoryEntity.getShippingZipcode();
		this.address = salesHistoryEntity.getShippingAddress();
		this.email = salesHistoryEntity.getShippingEmail();
		this.tell = salesHistoryEntity.getShippingTell();
	}
}