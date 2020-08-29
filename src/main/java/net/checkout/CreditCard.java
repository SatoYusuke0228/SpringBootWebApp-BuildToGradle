package net.checkout;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreditCard {

	@NotBlank(
//			groups = { BuyerCeditCard.class },
			message = "※入力してください")
	@Size(max = 16, message = "※16桁入力してください")
	@Size(min = 16, message = "※16桁入力してください")
	@Pattern(regexp = "[0-9]*", message = "※数字のみ入力してください")
	private String creditCardNum;

	@NotBlank(
//			groups = { BuyerCeditCard.class },
			message = "※入力してください")
	@Size(max = 32, message = "※入力が長すぎます")
	@Pattern(regexp = "[a-zA-Z]*", message = "※アルファベットと半角スペースのみ入力してください")
	private String creditCardName;

	@NotBlank(
//			groups = { BuyerCeditCard.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-1][0-9]", message = "※数字のみ2桁入力してください")
	private String creditCardMonth;

	@NotBlank(
//			groups = { BuyerCeditCard.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-9][0-9]", message = "※数字のみ2桁入力してください")
	private String creditCardYear;

	@NotBlank(
//			groups = { BuyerCeditCard.class },
			message = "※入力してください")
	@Pattern(regexp = "[0-9]{3}", message = "※数字のみ3桁入力してください")
	private String creditCardCvs;
}
