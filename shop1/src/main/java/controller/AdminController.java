package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
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
	@RequestMapping("mail")
	public ModelAndView mail (Mail mail, HttpSession session) {
		ModelAndView mav = new ModelAndView("alert");
		Properties prop = new Properties();
		try {
			//mail.properties : resources 폴더에 생성
			//java,resources 폴더의 내용은 : WEB-INF/classes에 복사됨
			FileInputStream fis = new FileInputStream
					(session.getServletContext().getRealPath("/")+"/WEB-INF/classes/mail.properties");
			prop.load(fis);
			prop.put("mail.smtp.user", mail.getNaverid());
		}catch(IOException e) {
			e.printStackTrace();
		}
		mailSend(mail,prop);
		mav.addObject("message","메일 전송이 완료 되었습니다.");
		mav.addObject("url","list");
		return mav;
	}

	private void mailSend(Mail mail,Properties prop) {
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(),mail.getNaverpw());
		Session session = Session.getInstance(prop,auth);
		MimeMessage msg = new MimeMessage(session);
		try {
			//보내는메일
			msg.setFrom(new InternetAddress(mail.getNaverid() + "@naver.com"));
			//받는 메일 정보
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {
					addrs.add(new InternetAddress (new String (email.getBytes("utf-8"), "8859_1")));
				} catch(UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0;i<addrs.size();i++) {
				arr[i]=addrs.get(i);
			}
			msg.setRecipients(Message.RecipientType.TO,arr); //수신메일 설정
			msg.setSentDate(new Date()); //전송일자
			msg.setSubject(mail.getTitle()); //제목
			// ================== 여기까지가 헤더부분
			//================여기서부터 내용
			MimeMultipart multipart = new MimeMultipart(); //내용, 첨부파일...
			//multipart 가 내용 따로 첨부파일 따로 해준다?
			MimeBodyPart message = new MimeBodyPart(); //바디파트 하나만듬 이름이 message인 이유는 text내용이라 ?
			message.setContent(mail.getContents(),mail.getMtype());
			multipart.addBodyPart(message); //multipart에 message 넣어줌
			//첨부파일 파일 추가
			for(MultipartFile mf : mail.getFile1()) {
				//mail.getFile1의 자료형은 List
				//mf :  첨부된 파일의 내용 중 한개
				if((mf != null) && (!mf.isEmpty())) {
					//첨부파일이 들어옴
					multipart.addBodyPart(bodyPart(mf));
					//multipart 에 내용, 첨부파일 들어있음
				}
			}
			msg.setContent(multipart);
			Transport.send(msg);
			//msg : 메일 전체 객체(전송메일주소, 수신메일주소들, 제목, 전송일자, 내용, 첨부파일들)
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

	private BodyPart bodyPart(MultipartFile mf) {
		//mf : 업로드된 내용
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();;
		//파일을 첨부하기 위해 서버에서 임시로 만들어 놓은 파일
		//사용자가 파일을 업로드 하고 그 파일을 메일에 첨부하여 보내짐??
		String path= "c:/mailupload/";
		File f1 = new File(path);
		if(!f1.exists()) f1.mkdirs();
		File f2 = new File(path + orgFile);
		try {
			mf.transferTo(f2); //여러개 파일업로드
			body.attachFile(f2); //이메일 첨부
			body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1"));
			//인식할 수 있도록 8859_1로 바꿔서  fileName 에 넣어준다? 첨부된 파일의 파일명
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	//AdminController 클래스의 내부 클래스로 구현
	private final class MyAuthenticator extends Authenticator {
		//class 앞에는 private 못하는데 붙을 수 있는 이유는 내부 클래스라서
		//이 클래스는 AdminController에서만 사용가능
		//final 이라 상속 못함
		//외부로 만들어도 상관은 없음
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}
		
	}
}
