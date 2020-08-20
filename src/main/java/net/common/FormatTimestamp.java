package net.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FormatTimestamp {

	public String formatTimestamp(Timestamp t) {

		String str = null;


		if (t != null && !t.toString().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			str = sdf.format(t);
		}

		return str;
	}
}