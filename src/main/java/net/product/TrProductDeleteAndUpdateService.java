package net.product;

public abstract interface TrProductDeleteAndUpdateService {

	abstract public void delete(TrProductEntity productEntity);
	abstract public void saveAndFlush(TrProductEntity productEntity);
}
