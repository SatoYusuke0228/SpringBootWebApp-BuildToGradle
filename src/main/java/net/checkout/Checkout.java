package net.checkout;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 購入者情報の管理クラス
 * @author SatoYusuke0228
 */
@Data
public class Checkout {

	/*
	 * 購入者情報の入力FORM
	 */

	@NotBlank(
//			groups = {BuyerInfo.class},
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String customerFirstName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String customerLastName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-9]{7}", message = "※7桁の数字のみ入力して下さい")
	private String customerZipcode;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 64, message = "※入力が長すぎます")
	private String customerMainAddress;

	@Size(max = 64, message = "※入力が長すぎます")
	private String customerBuildingAddress;

	@NotNull(message = "入力してください")
	@Size(max = 128, message = "入力が長すぎます")
	@Email
	private String customerEmail;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 11, message = "※入力が長すぎます")
	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String customerTell;

	/*
	 * 配送先情報の入力FORM
	 */

	@NotBlank(
//			groups = {BuyerInfo.class},
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String shippingFirstName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	private String shippingLastName;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-9]{7}", message = "※7桁の数字のみ入力して下さい")
	private String shippingZipcode;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 64, message = "※入力が長すぎます")
	private String shippingMainAddress;

	@Size(max = 64, message = "※入力が長すぎます")
	private String shippingBuildingAddress;

	@Size(max = 128, message = "入力が長すぎます")
	@Email
	private String shippingEmail;

	@NotBlank(
//			groups = { BuyerInfo.class },
			message = "※入力してください")
	@Size(max = 11, message = "※入力が長すぎます")
	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String shippingTell;
}
