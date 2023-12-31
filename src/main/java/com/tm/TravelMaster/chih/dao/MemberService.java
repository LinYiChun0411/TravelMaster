package com.tm.TravelMaster.chih.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.tm.TravelMaster.chih.model.Member;
import com.tm.TravelMaster.chih.model.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository mRepo;

	public void insertMember(Member mb) {
		mRepo.save(mb);
	}

	public boolean checkLogin(String memberAcc, String memberPwd) {
		Member member = mRepo.findByMemberAccAndMemberPwd(memberAcc, memberPwd);
		if (member != null) {
			return true;
		}
		return false;
	}

	public boolean findByMemberAcc(String memberAcc) {
		Member member = mRepo.findByMemberAcc(memberAcc);
		if (member != null) {
			return true;
		}
		return false;
	}

	public Member returnByMemberAcc(Member mb) {
		Member member = mRepo.findByMemberAcc(mb.getMemberAcc());
		return member;
	}

	public Member findByMemberNum(String memberNum) {
		Member member = mRepo.findByMemberNum(memberNum);
		return member;
	}
	
	public List<Member> findAllOtherMember(Member mb){
		List<Member> memberList = mRepo.findAllOtherMember(mb.getMemberNum());
		return memberList;
	}

	public List<Member> findAllMember() {
		List<Member> memberList = mRepo.findAll();
		return memberList;
	}
	
	public List<Member> findByMemberNumOrMemberLevel(String mytext,String text) {
		List<Member> memberList = mRepo.findByMemberNumOrMemberLevel(mytext,text,text);
		return memberList;
	}

	public void updateMember(Member mb) {
		mRepo.updateMemberNum(mb.getMemberNum(), mb.getMemberName(), mb.getMemberMail(), mb.getMemberPhone(),
				mb.getMemberAdd(), mb.getMemberPwd(), mb.getMemberLevel());
	}
	
	
	public void deleteMember(Member mb) {
		mRepo.delete(mb);
	}
	
	public void updateMemberPhoto(String memberNum,byte[] memberPhoto) {
		mRepo.updateMemberPhoto(memberNum,memberPhoto);
	}
	
	public Page<Member> findByPage(Integer pageNumber, Member mb) {
	    Pageable pageable = PageRequest.of(pageNumber - 1, 3);

	    List<Member> members = mRepo.findAllOtherMember(mb.getMemberNum());
	    int start = (int) pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), members.size());
	    List<Member> subList = members.subList(start, end);

	    Page<Member> page = PageableExecutionUtils.getPage(subList, pageable, () -> members.size());
	    return page;
	}
	
	public void updateResetPwdToken(String token,String email) throws MemberNoutFoundException{
		 Member mb = mRepo.findByMemberMail(email);
		 
		 if(mb!=null) {
			 mb.setResetPwdToken(token);
			 mRepo.save(mb);
		 }else {
			 throw new MemberNoutFoundException("Not find any member with email "+email);
		 }
	}
	
	public Member get(String resetPwdToken) {
		return mRepo.findByResetPwdToken(resetPwdToken);
	}
	
	public void updateMemberPwd(Member mb ,String newPassword) {	
		mb.setMemberPwd(newPassword);
		mb.setResetPwdToken(null);		
		mRepo.updateMemberPwd(mb.getMemberNum(), newPassword);
	}
	
}
