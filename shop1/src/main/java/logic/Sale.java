package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	private int saleid;
	private String userid;
	private Date saledate;
	private User user;  //db에는 없지만 추가
	private List<SaleItem> itemList = new ArrayList<>(); //주문상품목록
	public int getTotal() { //주문상품 전체 금액 리턴 (정보는 SaleItem에 있음)
		int sum = 0;
		// 리턴값 : 합(상품가격 * 주문수량)
/*		
		for(SaleItem s : itemList) {
			sum += s.getItem().getPrice() * s.getQuantity();
		}
		return sum;
*/
		return itemList.stream()
				//stream() : saleItem
				.mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum();
		//mapToInt  > 정수형으로 바꾼다
		//s 의 자료형은 SaleItem 객체의 한 요소만 IntStream 으로 바꾼다?
		//stream 이란 내부반복자? : 하나씩 꺼내올 필요가 없어서 속도가 빠름
		
	}
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getSaledate() {
		return saledate;
	}
	public void setSaledate(Date saledate) {
		this.saledate = saledate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "Sale [saleid=" + saleid + ", userid=" + userid + ", saledate=" + saledate + ", user=" + user + "]";
	}
	
}
