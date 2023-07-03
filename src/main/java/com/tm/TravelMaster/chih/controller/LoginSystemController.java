package com.tm.TravelMaster.chih.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tm.TravelMaster.chih.dao.MemberNoutFoundException;
import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import net.bytebuddy.utility.RandomString;

@Controller
public class LoginSystemController {
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping("/login.controller")
	public String processMainAction() {
		return "chih/loginSystem";
	}

	@PostMapping("/checklogin.controller")
	public String processAction(@RequestParam("memberacc") String memberAcc,
			@RequestParam("memberpwd") String memberPwd,HttpSession session, Model m) {
		boolean result = mService.checkLogin(memberAcc,memberPwd);
		if (result) {
			Member mb = mService.returnByMemberAcc(new Member(memberAcc));
			session.setAttribute("mbsession", mb);
			return "redirect:/layout/index";
		} else {
			String errorMessage = "帳號密碼錯誤，請重新登入";
		    m.addAttribute("errorMessage", errorMessage);
		    return "chih/loginSystem";
		}
	}
	
	@GetMapping("/logout.controller")
	public String StringprocessAction(HttpSession session) {
		  session.invalidate();
		  return "redirect:/layout/index";
	}
	
	@GetMapping("/forgotpassword.controller")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("pageTitle","Forgot Password");
		return "chih/forgot_password_form";
	}
	
	@PostMapping("/forgotpassword.controller")
	public String processForgotPasswordForm(HttpServletRequest request , Model model) {
		String email = request.getParameter("email");
		String token=RandomString.make(45);
		try {
			mService.updateResetPwdToken(token, email);
			
			String resetPasswordLink=Utility.getSiteURL(request)+"/reset.controller?token="+token;
			
			sendEmail(email,resetPasswordLink);
			
			model.addAttribute("message","已發送至您的信箱");
			
		} catch (MemberNoutFoundException e) {
			model.addAttribute("error",e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			model.addAttribute("error","Error while sending email.");
		} 
		
		model.addAttribute("pageTitle","Forgot Password");
		
		return "chih/forgot_password_form";
	}

	private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("travelmasterEEIT65@gmail.com","Travel Master");
		helper.setTo(email);
		
		String subject = "這是TravelMaster更改密碼的連結";
		String content = "<p>你好,</p>"
				+"<p>請改你的密碼，不要再忘記了</p>"
				+"<p><b><a href=\""+resetPasswordLink+"\">Change my password</a></b></p>"
				+"<p>如果您還記得帳號密碼請忽略此封信件</p>";
		
		helper.setSubject(subject);
		helper.setText(content,true);
		
		mailSender.send(message);
	}
	

	@GetMapping("/reset.controller")
	public String showResetPasswordForm(@Param(value="token") String token,Model model) {
		Member mb = mService.get(token);
		if(mb == null) {
			model.addAttribute("title","重設您的密碼");
			model.addAttribute("message","連結錯誤");
			return "chih/resetpwd";	
		}
		model.addAttribute("token",token);
		model.addAttribute("pageTitle","重設您的密碼");
		return "chih/reset_password_form";
	}
	
	@PostMapping("/resetpassword.controller")
	public String processResetPassword(HttpServletRequest request,Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		Member mb = mService.get(token);
		if(mb == null) {
			model.addAttribute("title","重設您的密碼");
			model.addAttribute("message","連結錯誤");
		}else {
			mService.updateMemberPwd(mb, password);
			model.addAttribute("title","重設密碼成功!!!");
			model.addAttribute("message","請重新登入!!!");
		}	
		return "chih/resetpwd";
	}
}
