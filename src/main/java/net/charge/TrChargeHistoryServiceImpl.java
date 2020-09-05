package net.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrChargeHistoryServiceImpl implements TrChargeHistoryService {

	@Autowired
	TrChargeHistoryRepository chargeHistoryRepository;

	@Override
	public TrChargeHistoryEntity getOne(Long id) {
		return chargeHistoryRepository.getOne(id);
	}

	@Override
	public void save(TrChargeHistoryEntity chargeHistoryEntity) {
		chargeHistoryRepository.save(chargeHistoryEntity);
	}

	@Override
	public void saveAndFlash(TrChargeHistoryEntity chargeHistoryEntity) {
		chargeHistoryRepository.saveAndFlush(chargeHistoryEntity);
	}
}
