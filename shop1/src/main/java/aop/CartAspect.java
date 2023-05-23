package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component //객체화 spring-mvc.xml 에 aop 추가해야함
@Aspect //pointcut + advice(5가지) = aspect
public class CartAspect {
	//around 써도 상관없음
	@Before("execution(* controller.Cart*.check*(..)) && args(..,session)")
	// 포인트컷 두가지 조건 모두 적합해야함:"execution(* controller.Cart*.check*(..)) && args(..,session)"
	//모든 접근제한자 / 컨트롤러 패키지 / cart로시작하는 모든 클래스에 /check로 시작하는 모든 메서드
	// args의 마지막이 session인 메서드 >> session인 객체를 가지고 오려고 args(..,session) 붙임
	// => CartController 클래스의 매개변수의 마지막이 HttpSession 인 check로 시작하는 메서드
	public void cartCheck(HttpSession session) throws Throwable {
		//cartCheck(HttpSession session) 의 session 객체는 CartController check*(..)에 있는 session 임
		User loginUser = (User)session.getAttribute("loginUser");
		//로그인 정보 검증
		if(loginUser == null) {
			throw new LoginException("회원만 주문 가능합니다.","../user/login");
			
		}
		Cart cart = (Cart)session.getAttribute("CART");
		//session에서 CART 데이터 가져옴
		if(cart == null || cart.getItemSetList().size() == 0)  {
			throw new CartEmptyException ("장바구니에 상품이 없습니다.","../item/list");
		}
	}
}
