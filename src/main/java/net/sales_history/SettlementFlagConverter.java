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
			flag = -1;
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
}
