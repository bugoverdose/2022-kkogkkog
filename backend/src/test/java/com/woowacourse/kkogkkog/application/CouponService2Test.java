package com.woowacourse.kkogkkog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.kkogkkog.application.dto.CouponMemberResponse;
import com.woowacourse.kkogkkog.application.dto.CouponSaveRequest;
import com.woowacourse.kkogkkog.application.dto.CouponResponse2;
import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
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

    private static final Member 루키 = new Member(1L, "루키");
    private static final Member 아서 = new Member(2L, "아서");
    private static final Member 정 = new Member(3L, "정");
    private static final Member 레오 = new Member(4L, "레오");

    @Autowired
    private CouponService2 couponService;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        memberRepository.save(루키);
        memberRepository.save(아서);
        memberRepository.save(정);
        memberRepository.save(레오);
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

    @DisplayName("사용자가 보낸 쿠폰들이 조회된다.")
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

    @DisplayName("사용자가 받은 쿠폰들이 조회된다.")
    @Nested
    class FindByReceiverTest {

        @DisplayName("조회되는 쿠폰 개수 확인")
        @Test
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

    private CouponSaveRequest toCouponSaveRequest(Member sender, List<Member> receivers) {
        List<Long> receiverIds = receivers.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return new CouponSaveRequest(sender.getId(), receiverIds, "red", "한턱내는", "추가 메세지", "커피");
    }
}
