package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service	
//@Service = @Component + Service(controller 기능과 (직접 dao 에 접근하지 않도록) dao 기능의 중간 역할 기능)
public class ShopService {
	@Autowired //객체 주입
	private ItemDao itemDao;
	
	@Autowired //객체 주입
	private UserDao userDao;
	
	@Autowired //객체 주입
	private SaleDao saleDao;
	
	@Autowired //객체 주입
	private SaleItemDao saleItemDao;
	
	@Autowired
	private BoardDao boardDao;
	
	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { 
			//(업로드가 안됐거나 업로드는 했으나 데이터가 없는 경우) 가 아닌 경우
			// == 업로드 해야하는 파일의 내용이 있는 경우
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			//item 에는 파라미터 값이 들어옴?
			//업로드된 파일 이름
		}
		//db에 내용 저장
		int maxid = itemDao.maxId(); //item 테이블에 저장됨 최대 id 값
		item.setId(maxid+1);
		itemDao.insert(item); //db에 데이터 추가
		
	}

	public void uploadFileCreate(MultipartFile file, String path) {
		//file : 파일의 내용
		//path : 업로드 할 폴더 위치 절대경로로 가져옴
		String orgFile = file.getOriginalFilename(); //파일 이름
		File f = new File(path);
		if(!f.exists()) f.mkdirs(); // 파일이 존재 하지 않으면 폴더 만들어줘
		try {
			//file에 저장된 내용을 파일로 저장
			file.transferTo(new File(path+orgFile));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { 
			//(업로드가 안됐거나 업로드는 했으나 데이터가 없는 경우) 가 아닌 경우
			// == 업로드 해야하는 파일의 내용이 있는 경우
			String path = request.getServletContext().getRealPath("/") + "img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			//item 에는 파라미터 값이 들어옴?
			//업로드된 파일 이름
		}

		itemDao.update(item); //db에 데이터 추가
		
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);
		
	}

	public void userInsert(@Valid User user) {
		userDao.insert(user);
		
	}

	public User selectUserOne(String userid) {
		return userDao.selectOne(userid);
	}

	public User selectPassOne(String userid, String password) {
		return userDao.selectOne(userid,password);
	}
	/*
	 * 1.로그인정보, 장바구니정보 => sale, saleitem 테이블의 데이터 저장
	 * 2.결과는 Sale 객체에 저장
	 * 		-sale 테이블 저장 : saleid값 구하기. 최대값 +1
	 * 		-saleitem 테이블 저장 : Cart 데이터를 이용하여 저장
	 */
	public Sale checkend(User loginUser, Cart cart) {
		int maxsaleid = saleDao.getMaxSaleId(); //saleid 최대값 조회
		Sale sale = new Sale(); //데이터 아직 없는 sale객체임 / 현재 최대값0
		sale.setSaleid(maxsaleid+1); // 하나 큰 값을 saleid 에 전달
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		//빠져있는 날짜는 db에서 now로 전달
		saleDao.insert(sale); //sale 테이블에 데이터 추가
		int seq = 0;
		for(ItemSet is : cart.getItemSetList()) { //cart에 있는itemSetList 가져와서 itemset에 등록?
			SaleItem saleItem = new SaleItem(sale.getSaleid(),++seq,is);//seq ++0 이라 1 로 들어감
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem); //saleitem 테이블에 데이터 추가
			//saleitem 객체를 db에 등록하자
		}
		return sale; //주문정보, 주문상품정보, 상품정보, 사용자정보
	}

	public List<Sale> salelist(String userid) {
		List<Sale> list = saleDao.list(userid); //id 사용자가 주문 정보목록
		for(Sale sa : list) {
			List<SaleItem> saleitemlist = saleItemDao.list(sa.getSaleid());
			//saleitemlist : 하나의 주문에 해당하는 주문상품 목록
			for(SaleItem si : saleitemlist) {
				Item item = itemDao.getItem(si.getItemid()); //상품정보
				si.setItem(item);
			}
			sa.setItemList(saleitemlist);
		}
		return list;
	}

	public void userUpdate(@Valid User user) {
		userDao.update(user); 
		
	}

	public void userDelete(@Valid User user) {
		userDao.delete(user); 
		
	}

	public void userDelete(String userid) {
		userDao.delete(userid); 
		
	}



	public void userChgpass(String userid, String chgpass) {
		userDao.update(userid, chgpass);
		
	}




	public List<User> userList() {
		return userDao.list(); //회원목록
	}



	public List<User> getUserList(String[] idchks) {
		return userDao.list(idchks);
	}


	public String getSearch(User user) {

			return userDao.search(user); 
		
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum(); // 등록된 게시물의 최대 num값 리턴
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path= request.getServletContext().getRealPath("/") + "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.insert(board);
	}

	public int boardcount(String boardid, String searchtype, String searchcontent) {
		return boardDao.count(boardid, searchtype, searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		return boardDao.list(pageNum,limit,boardid,searchtype, searchcontent);
	}

	public Board getBoard(Integer num) {
		return boardDao.selectOne(num); //board레코드 조회
	}

	public void addReadcnt(Integer num) {
		boardDao.addReadcnt(num);
		
	}


//	public void boardReply(Board board, HttpServletRequest request) {
//		int grpstep = boardDao.grpStepAdd();
//		board.setGrpstep(++grpstep);
//		int maxnum = boardDao.maxNum();
//		board.setNum(++maxnum);
//		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
//			String path= request.getServletContext().getRealPath("/") + "board/file/";
//			this.uploadFileCreate(board.getFile1(), path);
//			board.setFileurl(board.getFile1().getOriginalFilename());
//		}
//		boardDao.rinsert(board); 
//	}
	@Transactional //서비스를 transaction 처리함.
	//거래를 원자화 한다.(all or nothing) -> 하나라도 잘못되면 전부 취소해
	public void boardReply(@Valid Board board) {
		boardDao.updateGrpStep(board); //이미 등록된 grpstep값 1씩 증가
		//board 에 원글과 답변글 정보 섞여있다
		
		//원글에 있는 정보 답변글로 바꿔주기 시작
		int max = boardDao.maxNum(); //최대 num 조회
		board.setNum(++max); //원글의 num => 답변글의 num 값으로 변경
							//원글의 grp => 답변글의 grp 값을 동일, 설정 필요 없음
							//원글의 boardid => 답변글의 boardid 값을 동일. 설정 필요 없음
		board.setGrplevel(board.getGrplevel() + 1); //원글의 grplevel => +1 답변글의 grplevel값으로 설정
		board.setGrpstep(board.getGrpstep() + 1); //원글의 grpstep => +1 답변글의 grpstep값으로 설정
		//원글에 있는 정보 답변글로 바꿔주기 끝
		
		boardDao.insert(board);
		
	}


	public void boardUpdatd(@Valid Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path= request.getServletContext().getRealPath("/") + "board/file/";
			this.uploadFileCreate(board.getFile1(), path); //파일 업로드 : board.getFile1()의 내용을 파일로 생성
			board.setFileurl(board.getFile1().getOriginalFilename()); //파일 URL 값 변경
		}
		boardDao.update(board);
		
	}


	public void boardDelete(Integer num) {
		boardDao.delete(num);
	}



}
