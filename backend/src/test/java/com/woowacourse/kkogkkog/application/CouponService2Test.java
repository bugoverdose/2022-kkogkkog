package com.woowacourse.kkogkkog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.kkogkkog.application.dto.CouponMemberResponse;
import com.woowacourse.kkogkkog.application.dto.CouponSaveRequest;
import com.woowacourse.kkogkkog.application.dto.CouponResponse2;
import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
import com.woowacourse.kkogkkog.exception.coupon.CouponNotFoundException;
import com.woowacourse.kkogkkog.exception.member.MemberNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
public class CouponService2Test extends DatabaseTest {

    @Autowired
    private CouponService2 couponService;

    @Autowired
    private MemberRepository memberRepository;

    private Member 루키 = new Member(null, "루키");
    private Member 아서 = new Member(null, "아서");
    private Member 정 = new Member(null, "정");
    private Member 레오 = new Member(null, "레오");

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        memberRepository.save(레오);
        memberRepository.save(아서);
        memberRepository.save(루키);
        memberRepository.save(정);
    }

    @DisplayName("사용자가 보낸 쿠폰들을 조회할 수 있다.")
    @Nested
    class FindBySenderTest {

        @DisplayName("조회되는 쿠폰 개수 확인")
        @Test
        void couponCount() {
            couponService.save(toCouponSaveRequest(루키, List.of(아서, 정, 레오)));
            couponService.save(toCouponSaveRequest(정, List.of(아서, 레오)));
            List<CouponResponse2> actual = couponService.findAllBySender(루키.getId());

            assertThat(actual.size()).isEqualTo(3);
        }

        @DisplayName("조회되는 쿠폰의 보낸 사람 정보 확인")
        @Test
        void senderId() {
            couponService.save(toCouponSaveRequest(루키, List.of(아서, 정, 레오)));
            couponService.save(toCouponSaveRequest(정, List.of(아서, 레오)));
            Long senderId = 루키.getId();

            List<Long> actual = couponService.findAllBySender(senderId)
                    .stream().map(CouponResponse2::getSender)
                    .map(CouponMemberResponse::getId)
                    .collect(Collectors.toList());
            List<Long> expected = List.of(senderId, senderId, senderId);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @DisplayName("사용자가 받은 쿠폰들을 조회할 수 있다.")
    @Nested
    class FindByReceiverTest {

        @Test
        @DisplayName("조회되는 쿠폰 개수 확인")
        void couponCount() {
            couponService.save(toCouponSaveRequest(아서, List.of(정, 레오)));
            couponService.save(toCouponSaveRequest(레오, List.of(정, 아서)));
            List<CouponResponse2> actual = couponService.findAllByReceiver(정.getId());

            assertThat(actual.size()).isEqualTo(2);
        }

        @DisplayName("조회되는 쿠폰의 받은 사람 정보 확인")
        @Test
        void receiverId() {
            couponService.save(toCouponSaveRequest(아서, List.of(정, 레오)));
            couponService.save(toCouponSaveRequest(레오, List.of(정, 아서)));
            Long receiverId = 정.getId();

            List<Long> actual = couponService.findAllByReceiver(receiverId)
                    .stream().map(CouponResponse2::getReceiver)
                    .map(CouponMemberResponse::getId)
                    .collect(Collectors.toList());
            List<Long> expected = List.of(receiverId, receiverId);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @DisplayName("단일 쿠폰을 조회할 수 있다.")
    @Nested
    class FindByIdTest {

        @DisplayName("존재하는 쿠폰을 조회하는 경우 성공한다.")
        @Test
        void findById() {
            List<CouponResponse2> savedCoupons = couponService.save(toCouponSaveRequest(레오, List.of(정, 루키)));

            CouponResponse2 expected = savedCoupons.get(0);
            CouponResponse2 actual = couponService.findById(expected.getId());

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("존재하지 않는 쿠폰을 조회할 경우 예외가 발생한다.")
        void findById_notFound() {
            assertThatThrownBy(() -> couponService.findById(1L))
                    .isInstanceOf(CouponNotFoundException.class);
        }
    }

    @DisplayName("복수의 쿠폰을 저장할 수 있다")
    @Nested
    class SaveTest {

        @Test
        @DisplayName("받는 사람으로 지정한 사용자들에게 동일한 내용의 쿠폰이 발급된다.")
        void save() {
            CouponSaveRequest couponSaveRequest = toCouponSaveRequest(루키, List.of(아서, 정, 레오));
            List<CouponResponse2> createdCoupons = couponService.save(couponSaveRequest);

            assertThat(createdCoupons.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 쿠폰을 보내려는 경우 예외가 발생한다.")
        void senderNotFound() {
            Member 존재하지_않는_사용자 = new Member(10000L, "DB에 없는 사용자");
            CouponSaveRequest couponSaveRequest = toCouponSaveRequest(존재하지_않는_사용자, List.of(아서, 레오));

            assertThatThrownBy(() -> couponService.save(couponSaveRequest))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 사용자에게 쿠폰을 보내려는 경우 예외가 발생한다.")
        void receiverNotFound() {
            Member 존재하지_않는_사용자 = new Member(10000L, "DB에 없는 사용자");
            CouponSaveRequest couponSaveRequest = toCouponSaveRequest(정, List.of(아서, 존재하지_않는_사용자));

            assertThatThrownBy(() -> couponService.save(couponSaveRequest))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    private CouponSaveRequest toCouponSaveRequest(Member sender, List<Member> receivers) {
        List<Long> receiverIds = receivers.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return new CouponSaveRequest(sender.getId(), receiverIds, "red", "한턱내는", "추가 메세지", "커피");
    }
}