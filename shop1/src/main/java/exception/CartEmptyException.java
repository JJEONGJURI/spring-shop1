package exception;

//예외처리 생략 가능 예외클래스
public class CartEmptyException extends RuntimeException{
	private String url;
	//생성자 : 생성자를 보면 객체를 만드는 법을 알 수 있다.
	public CartEmptyException(String msg, String url) {
		super(msg); //getMessage() 메서드로 조회 가능
		this.url = url; //getUrl() 메서드로 조회 가능
	}
	public String getUrl() {
		return url;
	}
}
