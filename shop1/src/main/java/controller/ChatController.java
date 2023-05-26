package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {

	
	@RequestMapping("*") // 화면만 나오게
	public String chat() {
		return null;
		
	}
}
