package net.sales_history;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter //(autoApply = true)
public class SettlementFlagConverter
		implements AttributeConverter<String, Integer> {

	@Override
	public Integer convertToDatabaseColumn(String convertedSettlementFlag) {

		if (convertedSettlementFlag == null || convertedSettlementFlag.isEmpty()) {
			return null;
		}

		int settlementFlagInDB = 0;

		if ("決済完了".equals(convertedSettlementFlag)) {
			settlementFlagInDB = 1;
		} else if ("返金済み".equals(convertedSettlementFlag)) {
			settlementFlagInDB = 2;
		} else if ("キャンセル".equals(convertedSettlementFlag)) {
			settlementFlagInDB = -1;
		} else if ("決済拒否".equals(convertedSettlementFlag)) {
			settlementFlagInDB = -2;
		}

		return settlementFlagInDB;
	}

	@Override
	public String convertToEntityAttribute(Integer settlementFlagInDB) {

		String convertedSettlementFlag = new String();

		switch (settlementFlagInDB) {

		case (0):
			convertedSettlementFlag = "決済待ち";
			break;

		case (1):
			convertedSettlementFlag = "決済完了";
			break;

		case (2):
			convertedSettlementFlag = "返金済み";
			break;

		case (-1):
			convertedSettlementFlag = "キャンセル";
			break;

		case (-2):
			convertedSettlementFlag = "決済拒否";
			break;
		}

		if (convertedSettlementFlag.isEmpty()) {
			convertedSettlementFlag = "不明";
		}

		return convertedSettlementFlag;
	}
}
