package net.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrProductDeleteAndUpdateServiceImpl implements TrProductDeleteAndUpdateService {

	@Autowired
	private TrProductRipository productRepository;

	/**
	 * 受け取った商品Objectを引数にDELETE文を実行する
	 *
	 * @author SatoYusuke0228
	 */
	@Override
	public void delete(TrProductEntity productEntity) {

		productRepository.delete(productEntity);
	}

	/**
	 * 受け取った商品Objectを引数にUPDATE文を実行する
	 *
	 * @author SatoYusuke0228
	 */
	@Override
	public void saveAndFlush(TrProductEntity productEntity) {

		productRepository.saveAndFlush(productEntity);
	}
}
