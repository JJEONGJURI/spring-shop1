package controller;

import java.nio.file.attribute.UserPrincipalLookupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController { //POJO 방식
	@Autowired
	private ShopService service;
	
	@GetMapping("*") //설정되지 않은 모든 요청시 호출되는 메서드
	public ModelAndView join() {
		ModelAndView mav= new ModelAndView();
		mav.addObject(new User());
		return mav;
	}
	@PostMapping("join")
	public ModelAndView userAdd(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.input.user");
			bresult.reject("error.input.check");
			//reject 메서드 : global error에 추가
			//messages.properties 에 error.input.user 부분
			return mav;
		}
		//정상 입력값 : 회원 가입 하기 => db의 useraccount 테이블에 저장
		try {        
			service.userInsert(user); //화면에서 user 정보 들어있는 user 객체
			mav.addObject("user",user);
		} catch(DataIntegrityViolationException e) {
			//DataIntegrityViolationException : db에서 중복 키 오류시 발생되는 예외 객체
			e.printStackTrace();
			bresult.reject("error.duplicate.user"); //global 오류 등록
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;		
 	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.login.input");
			return mav;
		}
		
		 // 1.userid 맞는 User를 db에서 조회하기
		try {
			User dbUser = service.selectUserOne(user.getUserid());
			//service에서 userlgetUserid 이용하여 데이터 가져옴
			//템플릿에서는 dbUser 없어도 null 로 가져오지 않는다.
		} catch (EmptyResultDataAccessException e ) {
			//EmptyResultDataAccessException : 조회된 데이터가 없는 경우 발생되는 예외
			e.printStackTrace();
			bresult.reject("error.login.id"); // 아이디를 확인하세요 메세지 출력
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//2. 비밀번호 검증 :
		// 		일치 : session.setAttribute("loginUser",dbUser) => 로그인 정보
		//	   불일치 : 비밀번호를 확인하세요. 출력 (error.login.password)
		
		try {
			User dbUser = service.selectPassOne(user.getUserid(),user.getPassword());
			User loginUser = (User)session.getAttribute("loginUser");
			session.setAttribute("loginUser",dbUser);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			bresult.reject("error.login.password");
			mav.getModel().putAll(bresult.getModel());
			return mav;		
		}
		
		//3. mypage로 페이지 이동 => 현재 페이지 안만들어서 404 오류 발생(임시)
		
		
		mav.setViewName("redirect:mypage");
		return mav;
	}
}
