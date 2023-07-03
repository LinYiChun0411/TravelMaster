package com.tm.TravelMaster.sean.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orderItems")
@Data
public class OrderItemsBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orderId")
	private OrdersBean orders;

	@Column(name = "productId")
	private Integer productId;

	@Column(name = "productName")
	private String productName;

	@Column(name = "productPrice")
	private Integer productPrice;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "subTotal")
	private Integer subTotal;

}