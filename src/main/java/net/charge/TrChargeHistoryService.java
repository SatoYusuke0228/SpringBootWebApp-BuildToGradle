package net.charge;

public abstract interface TrChargeHistoryService {

	public abstract TrChargeHistoryEntity getOne(Long id);

	public abstract void save(TrChargeHistoryEntity chargeHistoryEntity);

	public abstract void saveAndFlash(TrChargeHistoryEntity chargeHistoryEntity);

}
