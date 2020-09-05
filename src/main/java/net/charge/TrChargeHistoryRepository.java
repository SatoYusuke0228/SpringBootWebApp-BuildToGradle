package net.charge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrChargeHistoryRepository extends
		JpaRepository<TrChargeHistoryEntity, Long>,
		JpaSpecificationExecutor<TrChargeHistoryEntity> {
}