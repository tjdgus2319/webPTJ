package com.ssafy.nnd.controller;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.nnd.dto.Member;
import com.ssafy.nnd.repository.MemberRepository;
import com.ssafy.nnd.util.KakaoAPI;


@CrossOrigin
@Controller
public class HomeController {
	
	@Autowired
    MemberRepository memberRepository;
 
	private Member tmpMember;
	
	private String email, company;
	
    @Autowired
    private KakaoAPI kakao;
    
    @RequestMapping(value="/")
    public String index() {
        
        return "index";
    }
    @GetMapping("/info/{id}")
	public Object getuserinfo(@PathVariable long id) {
		Optional<Member> member = memberRepository.findById(id);

		return member.get();
	}
    @RequestMapping(value="/login")
    public String login(@RequestParam("code") String code, HttpSession session) {
        String access_Token = kakao.getAccessToken(code);
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);
        Member mem = new Member();
        mem.setEmail((String)userInfo.get("email"));
        mem.setName((String)userInfo.get("nickname"));
        mem.setProfile((String)userInfo.get("profile"));
        mem.setCompany((String)userInfo.get("company"));
        
        email = mem.getEmail();
        company = mem.getCompany();
        
        // db에 넣기
        Optional<Member> member = memberRepository.findByEmailAndCompany(email, company);
        if (member.equals(Optional.empty())) { // 신규 유저일때
        	memberRepository.save(mem);	// db에 등록
		} else {
			mem.setIdx(member.get().getIdx());
		}
       
        tmpMember = new Member(mem);
        
        //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        if (userInfo.get("email") != null) {
//            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("member", mem);
            session.setAttribute("access_Token", access_Token);
        }
//        System.out.println("로그인중"+mem.toString());
        return "redirect:http://localhost:8081/";
    }

//   
//    @GetMapping(value="/userinfo")
//    public @ResponseBody Member returnUserinfo(HttpSession session) throws Exception{
//    	return tmpMember;
//    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpSession session) {
    	System.out.println("로그아웃에는 세션이 있는지 확인 "+session.getAttribute("member"));
        kakao.kakaoLogout((String)session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        return "index";
    }

}

