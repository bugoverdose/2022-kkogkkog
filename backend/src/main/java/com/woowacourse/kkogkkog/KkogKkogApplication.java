package com.woowacourse.kkogkkog;

import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.CouponTemplateRepository;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class KkogKkogApplication {

    private final MemberRepository memberRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    public KkogKkogApplication(
        MemberRepository memberRepository,
        CouponTemplateRepository couponTemplateRepository) {
        this.memberRepository = memberRepository;
        this.couponTemplateRepository = couponTemplateRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(KkogKkogApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Member member1 = memberRepository.save(new Member(null, "루키"));
        Member member2 = memberRepository.save(new Member(null, "아서"));
        Member member3 = memberRepository.save(new Member(null, "정"));
        Member member4 = memberRepository.save(new Member(null, "레오"));
    }
}
