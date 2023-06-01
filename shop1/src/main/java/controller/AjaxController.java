package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Controller : @Component + Controller 기능
 * 	객체 생성하고 기능 부여(Component), 요청이 들어오면 호출해
 * 		호출되는 메서드 리턴타입 : ModelAndView : 뷰이름+데이터
 * 		호출되는 메서드 리턴타입 : String 	   : 뷰이름
 * @RestController : @Component + Controller 기능 + 클라이언트에 데이터를 직접 전달
 * 		호출되는 메서드 리턴타입 : String 	   : 클라이언트에 전달되는 문자열 값
 * 		호출되는 메서드 리턴타입 : Object 	   : 클라이언트에 전달되는 값. (JSON 형태)
 * 
 * 	 	Spring 4.0 이후에 추가
 * 		Spring 4.0 이전 버전에서는 @ResponseBody 기능으로 설정하였음
 * 		@ResponseBody 어노테이션은 메서드에 설정함
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
	
	@RequestMapping("select")
	public List<String> select(String si, String gu, HttpServletRequest request) { //현재 si, gu 는 null임
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path)); //fr이 한줄씩 읽어서 가져옴
		} catch(Exception e) {
			e.printStackTrace();
		}
		//Set : 중복불가
		//LinkedHashSet : 순서유지, 중복불가. 리스트아님(첨자사용안됨).
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					// \\s+ : \\s(공백) , +(1개이상) => 공백 한개 이상으로 분리해서 배열로 만들어놔
					String[] arr = data.split("\\s+"); 
					if(arr.length >=3) set.add(arr[0].trim());//시도, 구군, 동 까지 3개 이상인 데이터중 첫번째 것 중복제거 후 추가해
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu == null ) { //si 파라미터 존재
			si = si.trim();
			try {
				while ((data = fr.readLine()) != null)  {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0])) {
						set.add(arr[1].trim()); //구정보 저장
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { //si 파라미터, gu 파라미터 존재
			si= si.trim();
			gu = gu.trim();
			try {
				while((data= fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu) 
							&& !arr[0].equals(arr[1]) && !arr[2].contains(arr[1])) {
						if(arr.length >3) {
							if(arr[3].contains(arr[1])) continue;
							arr[2] += " " + arr[3];
						}
						set.add(arr[2].trim());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set); //Set 객체 => List 객체로 변경
		return list; //찾은 list 객체 리턴 => 바로 브라우저로 전달됨 (= 뷰가 없다 = 데이터가 직접 내려감)
		//pom.xml 의 fasterxml.jackson .. 의 설정에 의해서 브라우저는 배열로 인식함
	}

//	@RequestMapping("select2") //클라이언트로 문자열 전송. 인코딩 설정 필요
	/*
	 * produces : 클라이언트에 전달되는 데이터 특징을 설정
	 * 		text/plain : 데이터 특징. 순수문자
	 * 		text/html  : HTML 형식의 문자 
	 * 		text/xml   : XML 형식의 문자
	 * 
	 *  	charset=utf-8 : 한글은 utf-8로 인식
	 */
	@RequestMapping(value="select2", produces="text/plain; charset=utf-8") //클라이언트로 문자열 전송. 인코딩 설정 필요
	public String select2(String si, String gu, HttpServletRequest request) { //현재 si, gu 는 null임
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path)); //fr이 한줄씩 읽어서 가져옴
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+"); 
					if(arr.length >=3) set.add(arr[0].trim());
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set); 
		return list.toString(); //[서울특별시,경기도....]
	}
}