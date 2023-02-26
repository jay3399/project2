package Jay.BoardP.service;

import Jay.BoardP.controller.form.LoginForm;
import Jay.BoardP.controller.dto.MemberDto;
import Jay.BoardP.controller.dto.User;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member principal = memberRepository.findMemberByUserId(username)
            .orElseThrow(() ->{
                return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + username);
            });

        return new User(principal); //시큐리티의 세션에 유저정보가 저장이됨. (원래는 콘솔창에 뜨는 user, pw가 있었음)
    }


    public MemberDto login(LoginForm loginForm) {

//        Member member = memberRepositorydd.Login(loginForm);
//
//        MemberDto memberDto = MemberDto.crateMemberDto(member);
//
//        return memberDto;


//        Member memberByUserId = springDataMemberRepository.findMemberByUserId(userId);
//
//        if (memberByUserId == null) {
//            return false;
//        }
//
//        if (!memberByUserId.equals(password)) {
//            return false;
//        }
//
//        return true;
        return null;
    }




}
