package net.code;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TrProductInsertServiceImpl implements TrProductInsertService {

	@Autowired
	private TrProductRipository productRepository;

	@Override
	@Transactional
	public TrProductEntity insert(
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
			String deleteUser) {//引数ここまで

		//新たに商品テーブルにinsertする商品のインスタスを作成
		TrProductEntity insertProduct = new TrProductEntity(
				productId,
				productName,
				productPrice,
				productCategoryId,
				productStock, productComment,
				productPhotoFileName1,
				productPhotoFileName2,
				productPhotoFileName3,
				productShowFlag,
				insertDate,
				insertUser,
				updateDate,
				updateUser,
				deleteDate,
				deleteUser);

		return productRepository.save(insertProduct);
	}
}
