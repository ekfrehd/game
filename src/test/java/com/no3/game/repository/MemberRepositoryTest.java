package com.no3.game.repository;

import com.no3.game.constant.Role;
import com.no3.game.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,50).forEach(i -> {
            Member member = Member.builder()
                    .email("aa"+i+5 +"@naver.com")
                    .password(passwordEncoder.encode("12341234"))
                    .name("test"+i)
                    .role(Role.USER)
                    .build();
            memberRepository.save(member);
        }); // USER insert test

    }

    @Commit
    @Transactional
    @Test
    public void testDeleteMember() {

        Long member_id = 23L; // Member의 id

        Member member = Member.builder().id(member_id).build();

        //기존
        //memberRepository.deleteById(mid);
        //reviewRepository.deleteByMember(member);

        //순서 주의
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(member_id);

    }
}