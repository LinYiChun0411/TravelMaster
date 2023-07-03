package com.tm.TravelMaster.sean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.TravelMaster.sean.model.OrdersBean;

public interface OrdersRepository extends JpaRepository<OrdersBean, Integer> {

	@Query(value = "SELECT * FROM orders WHERE memberSeq = :memberSeq", nativeQuery = true)
	List<OrdersBean> findByMemberSeq(@Param("memberSeq") int memberSeq);

}
