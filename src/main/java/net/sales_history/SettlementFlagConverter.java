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
		} else if ("決済拒否".equals(settlementFlag)) {
			flag = -2;
		}

		return flag;
	}

	@Override
	public String convertToEntityAttribute(Integer settlementFlagInDB) {

		String str = new String();

		switch (settlementFlagInDB) {

		case (0):
			str = "未決済";
			break;
		case (1):
			str = "決済完了";
			break;
		case (-1):
			str = "キャンセル";
			break;
		case (-2):
			str = "決済拒否";
			break;
		}

		return str;
	}
}
