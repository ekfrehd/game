package com.no3.game.controller;

import com.no3.game.dto.MemberJoinDto;
import com.no3.game.dto.OrderHistDto;
import com.no3.game.entity.Member;
import com.no3.game.repository.MemberRepository;
import com.no3.game.repository.OrderRepository;
import com.no3.game.service.MemberService;
import com.no3.game.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
@Log4j2
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final OrderService orderService;

    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberJoinDto", new MemberJoinDto());
        return "member/memberForm";
    } // 회원 가입을 위한 페이지

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberJoinDto memberJoinDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberJoinDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }


    // 마이페이지
    @GetMapping(value = {"/loginInfo", "/loginInfo/{page}"})
    public String memberInfo(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        String loginId = principal.getName();
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 2); // 한 페이지 당 2개의 구매 목록 출력
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(loginId, pageable);

        Optional<Member> optionalMember = memberRepository.findByEmail(loginId);

        Member member = optionalMember.orElse(new Member());

        model.addAttribute("member", member);
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 10);

        return "member/myinfo";
    }

    // 회원 탈퇴
    @PostMapping("/delete")
    public String deleteMember(@RequestParam String password, Model model, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean result = memberService.deleteMember(userDetails.getUsername(), password);

        if (result) {
            return "redirect:/members/logout";
        } else {
            model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
            return "/members/delete";
        }
    }
}
