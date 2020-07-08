package net.code;

import java.sql.Timestamp;

abstract interface TrProductInsertService {

	abstract public TrProductEntity insert(
			String productId,
			String productName,
			int productPrice,
			int productCategoryId,
			int productStock,
			String productComment,
			String productPhotoFileName1,
			String productPhotoFileName2,
			String productPhotoFileName3,
			int productShowFlag,
			Timestamp insertDate,
			String insertUser,
			Timestamp updateDate,
			String updateUser,
			Timestamp deleteDate,
			String deleteUser);
}