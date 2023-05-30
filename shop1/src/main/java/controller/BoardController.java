package controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;

	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	/*
	 * 1.유효성 검증
	 * 2.db의 board 테이블에 내용 저장, 파일 업로드
	 * 2.등록 성공 : list 재요청
	 * 	 등록 실패 : write 재요청
	 */
	@PostMapping("write") //.write 의 action=write 와 맞아야함
	public ModelAndView wirtePost(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		//request 객체 가져오는 이유 : 파일 업로드 할 때 위치값 가져오기 위해서
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid == null) boardid="1";
		request.getSession().setAttribute("boardid",boardid);
		board.setBoardid(boardid);
		service.boardWrite(board,request); //request 는 업로드 되는 위치 때문에 가져온거임
		mav.setViewName("redirect:list?boardid="+boardid);
		return mav;
	}
}
