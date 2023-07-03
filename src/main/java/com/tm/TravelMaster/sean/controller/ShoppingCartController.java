package com.tm.TravelMaster.sean.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.sean.model.OrdersBean;
import com.tm.TravelMaster.sean.model.ProductBean;
import com.tm.TravelMaster.sean.service.ShoppingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShoppingCartController {

	private final ShoppingService shoppingService;

	public ShoppingCartController(ShoppingService shoppingService) {
		this.shoppingService = shoppingService;
	}

	// 進購物車-登入判定+查詢
	@GetMapping("/sean/CartLoginStatus")
	public String handleCartLoginStatus(HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("mbsession");

		if (member != null) {
			String memberNum = member.getMemberNum();

			// 加載購物車資訊
			List<ProductBean> cart = shoppingService.loadCartData(memberNum);

			// 將購物車資料添加到Model中
			model.addAttribute("products", cart);

			return "sean/ShoppingCart";
		} else {
			return "redirect:/login.controller";
		}
	}

	// 購物車-加入購物車
	@PostMapping(value = "/sean/addToCart", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> addToCart(@ModelAttribute("product") ProductBean product, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");

		if (member == null) {
			return new ResponseEntity<>("請先登入", HttpStatus.UNAUTHORIZED);
		}

		String memberNum = member.getMemberNum();

		// 加載購物車資訊
		List<ProductBean> cart = shoppingService.loadCartData(memberNum);

		// 在 Service 層進行產品 ID 判定
		boolean isDuplicate = shoppingService.isProductInCart(cart, product.getProductId());

		if (isDuplicate) {
			return new ResponseEntity<>("商品已存在於購物車中", HttpStatus.OK);
		} else {
			// 添加產品到購物車
			cart.add(product);

			// 保存購物車資訊至本地端 JSON
			shoppingService.saveCartData(memberNum, cart);

			return new ResponseEntity<>("已成功加入商品", HttpStatus.OK);
		}
	}

	// 購物車-更新報名人數
	@PutMapping("/sean/updateRegistrations")
	public ResponseEntity<Void> updateRegistrations(@RequestParam("productId") Integer productId,
			@RequestParam("productRegistrations") int productRegistrations, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		String memberNum = member.getMemberNum();

		// 加載購物車資訊
		List<ProductBean> cart = shoppingService.loadCartData(memberNum);

		// 更新指定商品的數量
		shoppingService.updateCartQuantity(memberNum, cart, productId, productRegistrations);

		// 儲存更新後的購物車資訊到本地 JSON 檔案
		shoppingService.saveCartData(memberNum, cart);

		return ResponseEntity.ok().build();
	}

	// 購物車-刪除購物車商品
	@PostMapping("/sean/RemoveCartItem")
	public String removeCartItem(@RequestParam("productId") Integer productId, HttpSession session) {
		try {
			Member member = (Member) session.getAttribute("mbsession");
			String memberNum = member != null ? member.getMemberNum() : "";

			List<ProductBean> cart = shoppingService.loadCartData(memberNum);

			if (cart != null) {
				Iterator<ProductBean> iterator = cart.iterator();
				while (iterator.hasNext()) {
					ProductBean item = iterator.next();
					if (item.getProductId() == productId) {
						iterator.remove();
						break;
					}
				}

				shoppingService.saveCartData(memberNum, cart);
			}

			return "redirect:/sean/CartLoginStatus";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/sean/CartLoginStatus?error=發生錯誤";
		}
	}

	// 購物車-結帳
	@PostMapping("/sean/checkOut")
	public ResponseEntity<Map<String, String>> checkOut(HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		String memberNum = member.getMemberNum();
		if (memberNum != null) {
			List<ProductBean> cart = shoppingService.loadCartData(memberNum);
			if (cart != null && !cart.isEmpty()) {

				// Here you need to interact with Line Pay API to perform the transaction and
				String transactionId = "<Transaction ID from Line Pay API>";
				String paymentStatus = "<Payment Status from Line Pay API>";

				shoppingService.createOrder(member, cart, transactionId, paymentStatus);

				// Clear the cart after checkout
				cart.clear();
				shoppingService.saveCartData(memberNum, cart);
				return new ResponseEntity<>(Collections.singletonMap("message", "Checkout completed successfully."),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Collections.singletonMap("message", "Cart is empty."), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(Collections.singletonMap("message", "Checkout failed. Please try again."),
				HttpStatus.OK);
	}

	// 全部訂單查詢
	@GetMapping("/sean/getOrder")
	public String getOrder(Model model, HttpSession session) {
		List<OrdersBean> orders = shoppingService.getAllOrders();
		model.addAttribute("orders", orders);

		Member member = (Member) session.getAttribute("mbsession");
		// 有登入
		if (member != null) {
			return "sean/checkoutConfirmation";

			// 沒登入
		} else {
			return "redirect:/login.controller";
		}
	}

	// 個人訂單查詢
	@GetMapping("/sean/getPersonalOrder")
	public String getPersonalOrder(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");

		// 有登入
		if (member != null) {
			int memberSeq = member.getMemberSeq();
			List<OrdersBean> orders = shoppingService.getMemberOrders(memberSeq);
			model.addAttribute("orders", orders);
			return "sean/checkoutConfirmation";
		} else {
			// 没登入
			return "redirect:/login.controller";
		}
	}

}