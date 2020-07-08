package net.code;

/*商品購入者の入力formのグループ化
 *
 * @BuyerInfo
 * 購入者の名前、住所、電話番号などの個人情報
 *
 * @CeditCard
 * 購入者のクレジットカードに関する個人情報
 */
public interface FormGroups {

	interface BuyerInfo{}
	interface BuyerCeditCard{}
}