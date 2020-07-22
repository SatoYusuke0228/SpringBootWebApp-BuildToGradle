package net.product;

import java.util.List;
import java.util.Optional;

public abstract interface MsProductCategoryInventoryService {
	abstract List<MsProductCategoryInventoryEntity> findAll();
	abstract Optional<MsProductCategoryInventoryEntity> findById(int category);
}