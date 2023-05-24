package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
public class UserLoginAspect {
	@Before("execution(* controller.User*.idCheck*(..)) && args(..,userid,session)") //앞으로 아이디 체크해야하는경우 idCheck로 쓰면됨
	public void idCheck(String userid,HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			throw new LoginException("로그인 후 거래하세요","../user/login");
		}
		if ((!loginUser.getUserid().equals("admin")) && (!loginUser.getUserid().equals(userid))) {
			throw new LoginException("본인만 거래 가능합니다.","../item/list");
		}
	}
	//UserController.loginCheck*(..,HttpSession) ->pointcut
	@Before("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	public void loginCheck(HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			throw new LoginException("로그인이 필요합니다.","../user/login");
		}
	}

	/*
	 	@Component
	 	@Aspect
	 	public class UserLoginAspect {
	 	@Around("execution(* controller.User*.idCheck*(..)) && args(..,userid,session)")
	 	public Object userIdCheck(ProceedingJoinPoint joinPoint,String userid, HttpSession session) throws Throwable {
		User loginUser = (User)session.getattribute("loginUser");
		if(loginUser == null) {
		throw new LoginException("[idCheck]로그인이필요합니다","login"); // UserController 라서 login 바로 적어도 됨
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(userid)) {
		throw new LoginException("[idCheck]본인 정보만 거래 가능합니다필요합니다","../item/list"); // UserController 라서 login 바로 적어도 됨
		}
	 	return joinPoint.proceed(); //joinPoint.proceed(): 그 다음으로가
	 	}
	 */
}
