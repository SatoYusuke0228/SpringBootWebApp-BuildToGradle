package net.sales_history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrSalesProductHistoryServiceImpl implements TrSalesProductHistoryService {

	@Autowired
	TrSalesProductHistoryRepository salesProductHistoryRepository;

	@Override
	public List<TrSalesProductHistoryEntity> findAll() {

		return salesProductHistoryRepository.findAll();
	}

	@Override
	public TrSalesProductHistoryEntity getOne(String id) {

		return salesProductHistoryRepository.getOne(id);
	}

	@Override
	public void saveAndFlush(TrSalesProductHistoryEntity salesProductHistory) {

		salesProductHistoryRepository.saveAndFlush(salesProductHistory);
	}

	@Override
	public void saveSalesProductHistory(List<TrSalesProductHistoryEntity> salesProductHistoryEntity) {

			for (TrSalesProductHistoryEntity salesProductHistory : salesProductHistoryEntity) {
				salesProductHistoryRepository.save(salesProductHistory);
			}
	}
}