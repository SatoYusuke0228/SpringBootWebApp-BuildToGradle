package net.sales_history;

import javax.persistence.AttributeConverter;

public class ShippingStatusConverter implements AttributeConverter<String, Integer> {

	@Override
	public Integer convertToDatabaseColumn(String convertedShippingStatus) {

		if (convertedShippingStatus == null || convertedShippingStatus.isEmpty()) {
			return null;
		}

		int shippingStatusInDB = 0;

		if ("発送済み".equals(convertedShippingStatus)) {
			shippingStatusInDB = 1;
		} else if ("キャンセル".equals(convertedShippingStatus)) {
			shippingStatusInDB = -1;
		}

		return shippingStatusInDB;
	}

	@Override
	public String convertToEntityAttribute(Integer shippingStatusInDB) {

		String convertedShippingStatus = new String();

		switch (shippingStatusInDB) {

		case (0):
			convertedShippingStatus = "発送待ち";
			break;

		case (1):
			convertedShippingStatus = "発送済み";
			break;

		case (-1):
			convertedShippingStatus = "キャンセル";
			break;
		}

		if (convertedShippingStatus.isEmpty()) {
			convertedShippingStatus = "不明";
		}

		return convertedShippingStatus;
	}
}
