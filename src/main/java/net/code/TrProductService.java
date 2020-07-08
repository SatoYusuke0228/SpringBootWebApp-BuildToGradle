package net.code;

import java.util.List;

abstract interface TrProductService {
	abstract List<TrProductEntity> findAll();
	abstract TrProductEntity getItemInfo(String id);
	abstract List<TrProductEntity> findByKeyword(String keyword);
}