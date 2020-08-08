package net.sales_history;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter //(autoApply = true)
public class SettlementFlagConverter
		implements AttributeConverter<String, Integer> {

	@Override
	public Integer convertToDatabaseColumn(String settlementFlag) {

		if (settlementFlag == null || settlementFlag.isEmpty()) {
			return null;
		}

		int flag = 0;

		if ("決済完了".equals(settlementFlag)) {
			flag = 1;
		} else if ("キャンセル".equals(settlementFlag)) {
			flag = 2;
		}

		return flag;
	}

	@Override
	public String convertToEntityAttribute(Integer settlementFlagInDB) {

		String str = new String();

		if (settlementFlagInDB == 0) {
			str = "未決済";
		} else if (settlementFlagInDB == 1) {
			str = "決済完了";
		} else {
			str = "キャンセル";
		}

		return str;
	}

//	@Override
//	public String convertToDatabaseColumn( settlementFlag) {
//SettlementFlag
//		StringBuilder sb = new StringBuilder();
//
//		if (settlementFlag.getSettlementFlag() == 0) {
//			sb.append("未決済");
//		} else if (settlementFlag.getSettlementFlag() == 1) {
//			sb.append("決済完了");
//		} else {
//			sb.append("キャンセル");
//		}
//
//		return sb.toString();
//	}
//
//	@Override
//	public SettlementFlag convertToEntityAttribute(String SettlementFlaginDB) {
//
//		if (SettlementFlaginDB == null || SettlementFlaginDB.isEmpty()) {
//			return null;
//		}
//
//		SettlementFlag settlementFlag = new SettlementFlag();
//
//		if ("未決済".equals(SettlementFlaginDB)) {
//			settlementFlag.setSettlementFlag(0);
//		} else if ("決済完了".equals(SettlementFlaginDB)) {
//			settlementFlag.setSettlementFlag(1);
//		} else {
//			settlementFlag.setSettlementFlag(2);
//		}
//
//		return settlementFlag;
//	}
}
