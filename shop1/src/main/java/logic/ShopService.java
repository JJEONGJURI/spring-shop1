package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;

@Service	
//@Service = @Component + Service(controller 기능과 (직접 dao 에 접근하지 않도록) dao 기능의 중간 역할 기능)
public class ShopService {
	@Autowired //객체 주입
	private ItemDao itemDao;
	
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

	private void uploadFileCreate(MultipartFile file, String path) {
		//file : 파일의 내용
		//path : 업로드 할 폴더
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

}
