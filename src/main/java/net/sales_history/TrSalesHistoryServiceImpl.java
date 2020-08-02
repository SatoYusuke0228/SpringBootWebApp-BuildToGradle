package net.sales_history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrSalesHistoryServiceImpl implements TrSalesHistoryService {

	@Autowired
	TrSalesHistoryRepository salesHistoryRepository;

	@Override
	public List<TrSalesHistoryEntity> findAll() {

		List<TrSalesHistoryEntity> salesHistoryEntity = salesHistoryRepository.findAll();

		return salesHistoryEntity;
	}

	@Override
	public void saveSalesHistory(TrSalesHistoryEntity salesHistoryEntity) {

		salesHistoryRepository.save(salesHistoryEntity);
	}
}
