package net.sales_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrSalesHistoryRepository extends
		JpaRepository<TrSalesHistoryEntity, Long> {
}