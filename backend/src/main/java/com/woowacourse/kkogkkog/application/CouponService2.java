package com.woowacourse.kkogkkog.application;

import com.woowacourse.kkogkkog.application.dto.CouponSaveRequest;
import com.woowacourse.kkogkkog.application.dto.CouponSaveResponse;
import com.woowacourse.kkogkkog.domain.Coupon;
import com.woowacourse.kkogkkog.domain.CouponStatus;
import com.woowacourse.kkogkkog.domain.CouponType;
import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.CouponRepository;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
import com.woowacourse.kkogkkog.exception.member.MemberNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService2 {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public CouponService2(CouponRepository couponRepository, MemberRepository memberRepository) {
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public List<CouponSaveResponse> save(CouponSaveRequest couponSaveRequest) {
        List<Coupon> coupons = toCoupons(couponSaveRequest);
        List<Coupon> savedCoupons = couponRepository.saveAll(coupons);
        return savedCoupons.stream()
                .map(CouponSaveResponse::of)
                .collect(Collectors.toList());
    }

    private List<Coupon> toCoupons(CouponSaveRequest couponSaveRequest) {
        Member sender = memberRepository.findById(couponSaveRequest.getSenderId())
                .orElseThrow(MemberNotFoundException::new);
        List<Member> receivers = findMembers(couponSaveRequest.getReceivers());
        String modifier = couponSaveRequest.getModifier();
        String message = couponSaveRequest.getMessage();
        String backgroundColor = couponSaveRequest.getBackgroundColor();
        CouponType couponType = CouponType.of(couponSaveRequest.getCouponType());
        CouponStatus couponStatus = CouponStatus.READY;
        return receivers.stream()
                .map(it -> new Coupon(sender, it, modifier, message, backgroundColor, couponType, couponStatus))
                .collect(Collectors.toList());
    }

    private List<Member> findMembers(List<Long> memberIds) {
        List<Member> receivers = memberRepository.findAllById(memberIds);
        if (memberIds.size() != receivers.size()) {
            throw new MemberNotFoundException();
        }
        return receivers;
    }
}
