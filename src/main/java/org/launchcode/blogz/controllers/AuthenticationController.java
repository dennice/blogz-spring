package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
			// get parameters from request object
			// validate parameters (username, password, verify)
			// if validated, create new user, and put them in the session
			// access session...   Session thisSession = request.getSession(); (in AbstractController)
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		boolean validUser = User.isValidUsername(username);
		boolean validPass = User.isValidPassword(password);
		boolean verifiedPass = password.equals(verify);
		
		if (!validUser){
			String username_error_message = "That is not a valid username";
			model.addAttribute("username_error", username_error_message);
			model.addAttribute("username", username);
		}
		if (!validPass){
			String password_error_message = "That is not a valid password";
			model.addAttribute("password_error", password_error_message);
			model.addAttribute("username", username);
		}
		if (!verifiedPass){
			String verify_error_message = "Passwords don't match";
			model.addAttribute("verify_error", verify_error_message);
			model.addAttribute("username", username);
		}
		if (validUser && validPass && verifiedPass){
			User user = new User(username, password);
			userDao.save(user);
			
			HttpSession thisSession = request.getSession();
			this.setUserInSession(thisSession, user);
			return "redirect:blog/newpost";
		}
		
		return "signup";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userDao.findByUsername(username);
		String error_message = "invalid username or password";
		
		if(user == null){
			model.addAttribute("error", error_message);
			model.addAttribute("username", username);
			return "login";
		}
		//see if password matched username
		boolean validPassword = user.isMatchingPassword(password); 
		if(!validPassword){
			model.addAttribute("error", error_message);
			model.addAttribute("username", username);
			return "login";
		}
		
		HttpSession thisSession = request.getSession();
		this.setUserInSession(thisSession, user);
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
