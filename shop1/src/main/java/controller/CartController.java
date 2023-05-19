package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;

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
	
}
