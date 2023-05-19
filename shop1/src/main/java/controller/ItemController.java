package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;
//POJO 방식 : 순수자바소스. 다른 클래스(인터페이스)와 연관이 없음.
@Controller 
//@Controller = @Component 와 같은 기능(객체 생성) + Controller 기능 (요청 받아줌)
@RequestMapping("item")//http://localhost:8080/shop1/item/*
//item 들어오면 나를 호출해줘
public class ItemController {
	@Autowired	//ShopService 객체 주입.
	private ShopService service;
	//http://localhost:8080/shop1/item/list
	@RequestMapping("list") //list 요청 들어오면 밑에꺼 실행?
	//@RequestMapping: get방식, post 방식 상관없이 호출
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
	@GetMapping({"detail","update","delete"})
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
		//요청 url 로 가야하기 때문에 view 를 설정하면 안된다.
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	@GetMapping("create")	//Get 방식 요청
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	@PostMapping("create") //Post 방식 요청
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		// request : 요청객체 주입. <HttpServletSessopn session => 하면 session 객체 넣어줌>
		//@Valid : item 객체에 입력된 내용을 유효성 검사 진행 =>bresult 에 검사 결과 저장
		//item의 프로퍼티와 파라미터 값을 비교하여 같은 이름의 값을 item 객체에 저장한다.
		//name, price, picture, description
		ModelAndView  mav = new ModelAndView("item/create");
		//view 이름 설정
		if(bresult.hasErrors()) {
			//@Valid 프로세스에 의해서 입력 데이터 오류가 있는 경우
			mav.getModel().putAll(bresult.getModel());
			return mav; //item객체 + 에러메세지
		}
		service.itemCreate(item, request); //db추가, 이미지 업로드위치 가져오려고 request 객체 씀
		mav.setViewName("redirect:list"); //list 요청
		return mav;
	}
	/*
	 * 위에 detail 과 같은 역할이라 하나로 합침
	@GetMapping("update")
	public ModelAndView update(Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	*/
	/*
	 * 1.입력값 유효성 검증
	 * 2.db의 내용 수정. 파일 업로드
	 * 3.update 완료 후 list로 요청
	 */
	@PostMapping("update") 
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item, request);
		mav.setViewName("redirect:list");
		return mav;
	}
	@PostMapping("delete") 
	public String delete(Integer id){
		service.itemDelete(id);
		return "redirect:list";
	}
	
}
