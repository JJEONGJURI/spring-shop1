package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board") // /board 로 오는 모든 요청
public class BoardController {
	@Autowired
	private ShopService service; //shopService 주입

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
	/*
	 * RequestParam : 파라미터값을 객체에 매핑하여 저장하는 기능
	 * 	파라미터값과 현재 나의 변수명을 매핑??
	 * 	파라미터값 저장
	 * 		1.파라미터이름과 매개변수이름이 같은 경우
	 * 		2.Bean 클래스 객체의 프로퍼티 이름과 파라미터 이름이 같은 경우
	 * 		3.Map 객체를 이용하는 경우
	 * 
	 * 검색 기능 추가
	 */
	
	
	//3번째 방식
	@RequestMapping("list") //board/list
	public ModelAndView list(@RequestParam Map<String,String> param, HttpSession session) {
	//public ModelAndView list(Integer pageNum, String boardid, HttpSession session){	
	//	System.out.println(param);
		Integer pageNum = null; 
		if(param.get("pageNum") != null)
				pageNum = Integer.parseInt(param.get("pageNum"));
		String boardid = param.get("boardid");
		String searchtype = param.get("searchtype"); //처음엔 없어서 null 값 들어감
		String searchcontent = param.get("searchcontent"); //처음엔 없어서 null 값 들어감
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(boardid == null || boardid.equals("")) {
			boardid = "1";
		}
		session.setAttribute("boardid", boardid);
		if(searchtype == null || searchcontent == null || searchtype.trim().equals("") || searchcontent.trim().equals("")) {
			//하나라도 null 이거나 공백이라면
			searchtype = null;
			searchcontent = null;
			//둘다 null로 하겠다
		}
		String boardName=null;
		//화면에 나타내기 위해서
		switch(boardid) {
		case "1" : boardName = "공지사항"; break;
		case "2" : boardName = "자유게시판"; break;
		case "3" : boardName = "QNA"; break;
		}
		int limit = 10; //한페이지에 보여줄 게시물 건수
		int listcount = service.boardcount(boardid, searchtype, searchcontent); //등록된 게시물 건수
		List<Board> boardlist = service.boardlist(pageNum,limit,boardid, searchtype, searchcontent);
		//boardlist : 현재 페이지에 보여줄 게시물 목록
		
		//페이징 처리를 위한 값
		int maxpage=(int)((double)listcount/limit + 0.95); 
		//내 게시물이 21 건이면 3 페이지가 된다. // 등록 건수에 따른 최대 페이지
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		//페이지의 시작 번호
		int endpage = startpage + 9;
		//페이지의 끝 번호
		if(endpage > maxpage) endpage = maxpage;
		//페이지의 끝 번호는 최대 페이지보다 클 수 없다.
		int boardno = listcount - (pageNum - 1) * limit;
		//화면 보여지는 게시물 번호
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		//오늘 날짜를 문자열로 저장
		//20230530
		//비교는 list.jsp 에서 해준다.
		mav.addObject("boardid",boardid);
		mav.addObject("boardName", boardName);
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage" , endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno" , boardno);
		mav.addObject("today", today);
		return mav;
	}
	@RequestMapping("detail")
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); //num 게시판 내용 조회
		service.addReadcnt(num); //조회수 1 증가
		mav.addObject("board",board);
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName","공지사항");
		else if (board.getBoardid().equals("2"))
			mav.addObject("boardName","자유게시판");		
		else if (board.getBoardid().equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	/*
	 * 1.유효성 검사하기 - 파라미터값 저장
	 * 	- 원글정보 : num, grp, grplevel, grpstep, boardid
	 *  - 답글정보 : writer, pass, subject, content
	 * 2. db에 insert => service.boardReply()
	 * 	- 원글의 grpstep 보다 큰 이미 등록된 답글의 grpstep 값을 +1
	 * 		=> boardDao.grpStepAdd()
	 * 		num : maxNum() + 1
	 * 	- db에 insert => boardDao.insert()
	 * 		grp : 원글과 동일
	 *   	grplevel : 원글의 grplevel +1
	 *   	grpstep : 원글의 grpstep +1
	 *  3.등록 성공 : list로 페이지 이동
	 *    등록 실패 : "답변 등록시 오류 발생" reply 페이지 이동
	 */
	@GetMapping("reply")
	public ModelAndView getBoard(Integer num, HttpSession session ) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String)session.getAttribute("boardid");
		Board board = service.getBoard(num);
		mav.addObject("board",board);
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName","공지사항");
		else if (board.getBoardid().equals("2"))
			mav.addObject("boardName","자유게시판");		
		else if (board.getBoardid().equals("3"))
			mav.addObject("boardName","QNA");

		return mav;
		}
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
		if(bresult.hasErrors()) {
			//답글에서 RE 추가 안되게 함 시작
			Board dbboard = service.getBoard(board.getNum()); //원글 정보를 db에서 읽기
			Map<String,Object> map = bresult.getModel(); //getModel() 을 넣음
			Board b = (Board)map.get("board"); //화면에서 입력받은 값을 저장한 Board 객체
			b.setTitle(dbboard.getTitle());//원글의 제목으로 변경 
			//답글에서 Re 추가 안되게 함 끝
			mav.getModel().putAll(bresult.getModel());
		return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid="+board.getBoardid());
		} catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("답변등록시 오류 발생","reply?num="+board.getNum());
		}
		return mav;

				
	}
}
		
		
	

