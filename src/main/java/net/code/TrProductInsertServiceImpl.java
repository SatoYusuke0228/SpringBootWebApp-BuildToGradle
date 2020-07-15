package net.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TrProductInsertServiceImpl implements TrProductInsertService {

	@Autowired
	private TrProductRipository productRepository;

	@Override
	//@Transactional
	public TrProductEntity insert(TrProductEntity productEntity) {

		return productRepository.save(productEntity);
	}
}
