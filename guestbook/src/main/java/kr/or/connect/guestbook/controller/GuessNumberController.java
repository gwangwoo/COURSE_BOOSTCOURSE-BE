package kr.or.connect.guestbook.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuessNumberController 
{
	@GetMapping("/guess")
	public String guess(@RequestParam(name="number", required=false) Integer number, HttpSession session, ModelMap model)
	{
		String message = null;
		
		// 최초 요청시 number의 값이 없음
		if(number == null)
		{
			session.setAttribute("count", 0);
			session.setAttribute("randomNumber", (int)(Math.random() * 100) + 1);
			message = "내가 생각한 숫자를 맞춰보세요!";
		}
		else
		{
			// 세션이 가진 값과 입력값을 비교
			int count = (Integer) session.getAttribute("count");
			int randomNumber = (Integer) session.getAttribute("randomNumber");
			
			if(number > randomNumber)
			{
				message = "입력한 값은 내가 생각하고 있는 숫자보다 작습니다.";
				session.setAttribute("count", ++count);
			}
			else if(number < randomNumber)
			{
				message = "입력한 값은 내가 생각하고 있는 숫자보다 큽니다.";
				session.setAttribute("count", ++count);
			}
			else
			{
				message = "OK ^^ " + ++count + "번째 맞췄습니다. 내가 생각한 숫자는 " + number + "입니다.";
				// 세션을 없애줌
				session.removeAttribute("count");
				session.removeAttribute("randomNumber");
			}
		}
		
		model.addAttribute("message", message);
		
		return "guess";
	}
}