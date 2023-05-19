package logic;

import java.util.ArrayList;
import java.util.List;


public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<>();
	public List<ItemSet> getItemSetList() {
		return itemSetList;
		
	}
	public void push(ItemSet itemSet) {
		//itemSet : 추가될 item
		int count = itemSet.getQuantity();
		for(ItemSet old : itemSetList) {
			if(itemSet.getItem().getId() == old.getItem().getId()) {
			count = old.getQuantity() + itemSet.getQuantity();
			old.setQuantity(count);
			return;
			}
		} 
		//내가가지고 있는 itemSet을 itemSetList 에 넣어줌
		itemSetList.add(itemSet);
	}
	public int getTotal() {
		int sum = 0;
		for(ItemSet s : itemSetList)
			sum += s.getItem().getPrice() * s.getQuantity();
			//ItemSet에 들어있는거 들고와서 가격합계 구한다.
		return sum; //카트에 들어있는 전체금액 리턴
	}
}
