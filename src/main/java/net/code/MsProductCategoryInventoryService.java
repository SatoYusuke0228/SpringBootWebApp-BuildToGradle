package net.code;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsProductCategoryInventoryService {

	@Autowired
	private MsProductCategoryInventoryRipository categoryRepository;

	/**
	 * カテゴリーテーブルの中身を全て取得するメソッド
	 */
	public List<MsProductCategoryInventoryEntity> findAll() {
		return categoryRepository.findAll();
	}

	/**
	 * カテゴリーテーブルの中身をカテゴリーIDごとに取得するメソッド
	 */
	public Optional<MsProductCategoryInventoryEntity> findById(int category){
		return categoryRepository.findById(category);
	}
}