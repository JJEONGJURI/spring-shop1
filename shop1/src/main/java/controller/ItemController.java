package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller 
//@Controller = @Component 와 같은 기능(객체 생성) + Controller 기능 (요청 받아줌)
@RequestMapping("item")//http://localhost:8080/shop1/item/*
//item 들어오면 나를 호출해줘
public class ItemController {
	@Autowired	//ShopService 객체 주입.
	private ShopService service;
	//http://localhost:8080/shop1/item/list
	@RequestMapping("list") //list 요청 들어오면 밑에꺼 실행?
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		// ModelAndView : Model + view
		//		  		  view에 전송할 데이터 + view 설정
		// view 설정이 안된 경우 : url 과 동일. item/list 뷰로 설정
		List<Item> itemList = service.itemList(); 
		//@Autowired 없으면 nullPointException 에러 발생됨 > 객체 주입이 안되기 때문
		//itemList : item 테이블의 모든 정보를 Item 객체 List로 저장
		mav.addObject("itemList",itemList);
		//데이터 저장
		//view : item/list
		return mav;
	}
}
