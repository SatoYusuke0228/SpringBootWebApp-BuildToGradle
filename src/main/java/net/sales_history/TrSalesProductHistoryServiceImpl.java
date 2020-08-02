package net.sales_history;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrSalesProductHistoryServiceImpl implements TrSalesProductHistoryService {

	@Autowired
	TrSalesProductHistoryRepository salesProductHistoryRepository;

	@Override
	public List<TrSalesProductHistoryEntity> findAll() {

		List<TrSalesProductHistoryEntity> salesProductHistoryEntity = salesProductHistoryRepository.findAll();

		return salesProductHistoryEntity;
	}

	@Override
	public Optional<TrSalesProductHistoryEntity> findById(long id) {

		Optional<TrSalesProductHistoryEntity> salesProductHistoryEntity = salesProductHistoryRepository.findById(id);

		return salesProductHistoryEntity;
	}

	@Override
	public void saveSalesHistory(TrSalesProductHistoryEntity salesProductHistoryEntity) {

		salesProductHistoryRepository.save(salesProductHistoryEntity);
	}
}