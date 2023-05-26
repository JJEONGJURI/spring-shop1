package controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.ShopService;
import logic.User;


/*
 * AdminController의 모든 메서드는 관리자 로그인
 * =>AOP 설정 AdminLoginAspect. adminCheck() 로 설정
 * 1. 로그아웃 상태 => 로그인 하세요. login 페이지 이동
 * 2. 관리자가 아닌 경우
 * 
 */
@Controller
@RequestMapping("admin") //admin으로 들어오면 admincontroller 실행하ㅐ라
public class AdminController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") // admin/list 로 들어오면 이거 실행해라
	public ModelAndView adminCheckList(String sort, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> userList = service.userList(); //전체 회원목록
		//userList : db에 등록된 모든 회원정보 저장 목록
		if(sort != null) {
			switch(sort) {
			case "10" : 
				//Collections.sort(userList,(u1,u2)->u1.getUserid().compareTo(u2.getUserid()));
				Collections.sort(userList,new Comparator<User>() {
					@Override
					public int compare(User u1, User u2) {
						return u1.getUserid().compareTo(u2.getUserid());
					} //결과가 양수로 나오면 u1이 뒤구나?
				});
				break;

			case "11" : 
				Collections.sort(userList,(u1,u2)->u2.getUserid().compareTo(u1.getUserid()));
				break;
				
			case "20" : 
				Collections.sort(userList,(u1,u2)->u1.getUsername().compareTo(u2.getUsername()));
				break;
				
			case "21" : 
				Collections.sort(userList,(u1,u2)->u2.getUsername().compareTo(u1.getUsername()));
				break;
				
			case "30" : 
				Collections.sort(userList,(u1,u2)->u1.getPhoneno().compareTo(u2.getPhoneno()));
				break;
				
			case "31" : 
				Collections.sort(userList,(u1,u2)->u2.getPhoneno().compareTo(u1.getPhoneno()));
				break;
				
			case "40" : 
				Collections.sort(userList,(u1,u2)->u1.getBirthday().compareTo(u2.getBirthday()));
				break;
				
			case "41" : 
				Collections.sort(userList,(u1,u2)->u2.getBirthday().compareTo(u1.getBirthday()));
				break;
				
			case "50" : 
				Collections.sort(userList,(u1,u2)->u1.getEmail().compareTo(u2.getEmail()));
				break;
			
			case "51" : 
				Collections.sort(userList, (u1,u2)->u2.getEmail().compareTo(u1.getEmail()));
			}
		}
		mav.addObject("list",userList);	//list라는 이름으로 뷰 단에 전달	
		return mav;
	}
	@RequestMapping("mailForm")
	public ModelAndView mailform(String[] idchks, HttpSession session) {
		//String[] idchks : 
		//	String 형 배열로 받는 이유 : idchks 파라미터의 값 여러개 가능. request.getParameterValues("파라미터")
		//String[] 으로 안하고 String 으로 받으면 여러개를 , 로 받고 오류는 나지 않는다.
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length == 0) {
			throw new LoginException("메일을 보낼 대상자를 선택하세요","list"); //선택된게 없는 경우
		}
		List<User> list = service.getUserList(idchks); //선택된게 있으면 목록을 가져와서 뷰단으로 전달
		mav.addObject("list",list);
		return mav;
	}
}
