package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;
import logic.User;

@Controller //controller 쓸거임
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) { //session 에 등록하려고
		//add 객체 부를 때 세션객체도 불러줌
		ModelAndView mav = new ModelAndView("cart/cart");// view 이름은 cart/cart 야~
		Item item = service.getItem(id); //id에 해당하는 item 정보를 item에 넣어줌
		Cart cart = (Cart)session.getAttribute("CART"); //cart 에 Cart 정보 넣는다.
		//cart 는 null임
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
			
		}
		cart.push(new ItemSet(item,quantity));
		//item  : item 정보
		//quantity : 수량정보
		mav.addObject("message",item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet robj = cart.getItemSetList().remove(index);
		mav.addObject("message",robj.getItem().getName()+"가(이) 삭제되었습니다.");
		mav.addObject("cart",cart);
		return mav;
	}
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		mav.addObject("cart",cart);
		return mav;
		
	}
	/*
	 * 주문 전 확인 페이지
	 * 1. 장바구니에 상품 존재해야 함
	 * 		상품이 없는 경우 : CartEmptyException 예외 발생
	 * 2. 로그인 된 상태여야 함.
	 * 		로그아웃 상태 : LoginException 예외 발생
	 * 			-exception.LoginException 예외 클래스 생성.
	 * 			-예외 발생시 exception.jsp 로 페이지 이동
	 */
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) { //=> 주문상품이 없음 
			//cart == null : session에 CART 이름의 속성값이 없는 경우
			//cart.getItemSetList().size() : CART 속성은 존재. CART 내부에 상품정보가 없는 경우
			//cart.getItemSetList() 자료형은 list
			throw new CartEmptyException("장바구니에 상품이 없습니다.","../item/list"); 
			//throw : 강제 예외 발생
			//CartEmptyException 객체 생성 하고 예외 강제 발생
		}
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인하세요.","../item/list");
		}
		return null; 
		//view 의 이름 리턴. null인 경우 url 과 같은 이름을 호출
		//	/WEB-INF/view/cart/checkout.jsp
	}	
}
