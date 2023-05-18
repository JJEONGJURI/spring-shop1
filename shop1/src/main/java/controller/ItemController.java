package controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
	@RequestMapping("detail")
	//http://localhost:8080/shop1/item/detail?id=1
	public ModelAndView detail(Integer id) { 
		//id = id 파라미터의 값
		//파라미터 이름과 매개변수 이름이 같아야함
		
		/*
		 * 매개 변수 자체가 하나의 파라미터임
		 * 위에 id 랑 밑에 id 같아야함
		 */
		System.out.println(id);
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	@RequestMapping("register") //유효성 검증
	public ModelAndView register(@Valid Item item, BindingResult bresult) {
		//@Valid : item 객체에 입력된 내용을 유효성 검사 진행
		//item의 프로퍼티와 파라미터 값을 비교하여 같은 이름의 값을 item 객체에 저장한다.
		//name, price, picture, description
		ModelAndView  mav = new ModelAndView("item/create");
		//view 이름 설정
		if(bresult.hasErrors()) {
			//@Valid 프로세스에 의해서 입력 데이터 오류가 있는 경우
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		return mav;
	}
}
