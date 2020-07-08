package net.code;

import java.util.List;
import java.util.Optional;

abstract interface MsProductCategoryInventoryService {
	abstract List<MsProductCategoryInventoryEntity> findAll();
	abstract Optional<MsProductCategoryInventoryEntity> findById(int category);
}