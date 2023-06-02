package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
		if(si==null && gu==null) { //시도 선택
			try {
				//fr.readLine() : 한줄씩 read
				while((data=fr.readLine()) != null) {
					// \\s+ : \\s(공백) , +(1개이상) => 공백 한개 이상으로 분리해서 배열로 만들어놔
					String[] arr = data.split("\\s+"); 
					if(arr.length >=3) set.add(arr[0].trim());//시도, 구군, 동 까지 3개 이상인 데이터중 첫번째것만 중복제거 후 추가해
				} //끝나면 List로 감
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu == null ) { //si 파라미터 존재 => 시도 선택한 경우 : 시도에 맞는 구군값 전송
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
		} else { //si 파라미터, gu 파라미터 존재 => 구군선택 : 동 값 전송
			si= si.trim();
			gu = gu.trim();
			try {
				while((data= fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu) 
							&& !arr[0].equals(arr[1]) && !arr[2].contains(arr[1])) {
						//!arr[0].equals(arr[1]) && !arr[2].contains(arr[1]) : 같은값 제거
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
	
	//환율정보 크롤링 시작

	@RequestMapping(value="exchange", produces="text/html; charset=utf-8") //text/html 은 사이트매쉬와 연관돼 있어서 막아야함
	public String exchange() {
		Document doc = null; //Document jsoup에 import
		List<List<String>> trlist = new ArrayList<>(); //미국, 중국, 일본, 유로 통화들만 저장 목록
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01#tab1";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			exdate = doc.select("p.table-unit").html(); //조회기준일 : 2023.06.02
			//p.table-unit : class 속성의 값이 table-unit인 p태그
			for(Element tr : trs) { //tr 태그들 안에 있는 td 태그들
				List<String> tdlist = new ArrayList<>();	//tr 태그의 td 태그 목록
				Elements tds = tr.select("td"); //tr태그의 하위 td 태그들
				for(Element td :  tds) {
					tdlist.add(td.html());
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD")	//미달러 통화코드
							|| tdlist.get(0).equals("CNH")	//위안화
							|| tdlist.get(0).equals("JPY(100)")	//엔화
							|| tdlist.get(0).equals("EUR")) {	//유로
						trlist.add(tdlist); //td리스트를 tr리스트에 넣어줌
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();		
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h4 class='w3-center'>수출입은행<br>" + exdate + "</h4>");
		sb.append("<table class='w3-table-all'>");
		sb.append("<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append("<tr><td>"+tds.get(0)+"<br>"+tds.get(1)+"</td><td>"+tds.get(4)+"</td>");
			sb.append("<td>"+tds.get(2)+"</td><td>"+tds.get(3)+"</td><tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	@RequestMapping(value="exchange2") 
	public Map<String,Object> exchange2() { //json 데이터로 전송
//	public List<List<String>> exchange2() {
		Document doc = null; //Document jsoup에 import
		List<List<String>> trlist = new ArrayList<>(); //미국, 중국, 일본, 유로 통화들만 저장 목록 
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01#tab1";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); //tr 태그들
			exdate = doc.select("p.table-unit").html(); //조회기준일 : 2023.06.02
			//p.table-unit : class 속성의 값이 table-unit인 p태그
			for(Element tr : trs) { //tr 태그들 안에 있는 td 태그들
				List<String> tdlist = new ArrayList<>();	//tr 태그의 td 태그 목록
				Elements tds = tr.select("td"); //tr태그의 하위 td 태그들
				for(Element td :  tds) {
					tdlist.add(td.html());
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD")	//미달러 통화코드
							|| tdlist.get(0).equals("CNH")	//위안화
							|| tdlist.get(0).equals("JPY(100)")	//엔화
							|| tdlist.get(0).equals("EUR")) {	//유로
						trlist.add(tdlist); //td리스트를 tr리스트에 넣어줌
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();		
		}
		Map<String,Object> map = new HashMap<>();
		map.put("exdate", exdate); //일자
		map.put("trlist", trlist); //환율 목록
		return map;
//		return trlist;
	}
}
 