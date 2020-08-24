package net.purchase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 購入者情報の管理クラス
 * @author SatoYusuke0228
 */
@Data
public class Checkout {

	@NotBlank(
//			groups = {BuyerInfo.class},
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String firstName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String lastName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-9]{7}", message = "※7桁の数字のみ入力して下さい")
	private String zipcode;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 64, message = "※入力が長すぎます")
	private String mainAddress;

	@Size(max = 64, message = "※入力が長すぎます")
	private String buildingAddress;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 11, message = "※入力が長すぎます")
	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String tell;

//	@NotBlank(
//			groups = { BuyerInfo.class },
//			message = "※入力してください")
//	@Size(max = 128, message = "※入力が長すぎます")
//	@Email
//	private String email;
}
