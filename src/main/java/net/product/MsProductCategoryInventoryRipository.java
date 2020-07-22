package net.product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsProductCategoryInventoryRipository extends
		JpaRepository<MsProductCategoryInventoryEntity, Integer> {}