package com.tm.TravelMaster.sean.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class OrdersBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "memberSeq")
	private int memberSeq;
	
	@Column(name = "TotalPrice")
	private int TotalPrice;

	@Column(name = "transactionId")
	private String transactionId;

	@Column(name = "paymentStatus")
	private String paymentStatus;

	@OneToMany(mappedBy = "orders")
	private List<OrderItemsBean> items;

}
