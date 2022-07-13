package com.woowacourse.kkogkkog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import com.woowacourse.kkogkkog.application.dto.CouponSaveRequest;
import com.woowacourse.kkogkkog.application.dto.CouponSaveResponse;
import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
import com.woowacourse.kkogkkog.exception.member.MemberNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class CouponService2Test {

    private static final Member 루키 = new Member(1L, "루키");
    private static final Member 아서 = new Member(2L, "아서");
    private static final Member 정 = new Member(3L, "정");
    private static final Member 레오 = new Member(4L, "레오");

    @Autowired
    private CouponService2 couponService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(루키);
        memberRepository.save(아서);
        memberRepository.save(정);
        memberRepository.save(레오);
    }

    @Test
    @DisplayName("받는 사람으로 지정한 사용자들에게 동일한 내용의 쿠폰이 발급된다.")
    void save() {
        CouponSaveRequest couponSaveRequest = toCouponSaveRequest(루키, List.of(아서, 정, 레오));
        List<CouponSaveResponse> createdCoupons = couponService.save(couponSaveRequest);

        assertThat(createdCoupons.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 쿠폰을 보내려는 경우 예외가 발생한다.")
    void save_senderNotFound() {
        Member 존재하지_않는_사용자 = new Member(10000L, "DB에 없는 사용자");
        CouponSaveRequest couponSaveRequest = toCouponSaveRequest(존재하지_않는_사용자, List.of(아서, 레오));

        assertThatThrownBy(() -> couponService.save(couponSaveRequest))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 쿠폰을 보내려는 경우 예외가 발생한다.")
    void save_receiverNotFound() {
        Member 존재하지_않는_사용자 = new Member(10000L, "DB에 없는 사용자");
        CouponSaveRequest couponSaveRequest = toCouponSaveRequest(정, List.of(아서, 존재하지_않는_사용자));

        assertThatThrownBy(() -> couponService.save(couponSaveRequest))
                .isInstanceOf(MemberNotFoundException.class);
    }

    private CouponSaveRequest toCouponSaveRequest(Member sender, List<Member> receivers) {
        List<Long> receiverIds = receivers.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return new CouponSaveRequest(sender.getId(), receiverIds, "red", "한턱내는", "추가 메세지", "커피");
    }
}
