package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class Item {
	private int id;
	@NotEmpty(message="상품명을 입력하세요") 
	//@NotEmpty : 값이 없거나 빈 문자열일 때 message 뜨게해줌
	private String name;
	@Min(value=10,message="10원이상 가능합니다")
	//@Min : 10 보다 작게 적으면 message 뜸
	@Max(value=100000,message="10만원이하만 가능합니다")
	private int price;
	@NotEmpty(message="상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	private MultipartFile picture;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public MultipartFile getPicture() {
		return picture;
	}
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", pictureUrl=" + pictureUrl + "]";
	}
	
	

}
