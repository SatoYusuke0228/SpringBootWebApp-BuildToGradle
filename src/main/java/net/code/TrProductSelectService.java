package net.code;

import java.util.List;

/**
 * TrProductServiceの
 * 「Select」文に関する
 * Abstract Interface
 *
 * @author SatoYusuke0228
 */
abstract interface TrProductSelectService {

	abstract List<TrProductEntity> findAll();

	abstract TrProductEntity getItemInfo(String id);

	abstract List<TrProductEntity> findByKeyword(String keyword);
}