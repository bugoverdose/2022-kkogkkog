package com.woowacourse.kkogkkog.application;

import static java.util.stream.Collectors.*;

import com.woowacourse.kkogkkog.application.dto.CouponResponse;
import com.woowacourse.kkogkkog.application.dto.CouponsResponse;
import com.woowacourse.kkogkkog.domain.CouponDemo;
import com.woowacourse.kkogkkog.domain.repository.CouponDemoRepository;
import com.woowacourse.kkogkkog.presentation.CouponDemoController.CouponDemoCreateRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CouponDemoService {

    private final CouponDemoRepository couponDemoRepository;

    public CouponDemoService(
        CouponDemoRepository couponDemoRepository) {
        this.couponDemoRepository = couponDemoRepository;
    }

    public Long save(CouponDemoCreateRequest couponDemoCreateRequest) {
        CouponDemo couponDemo = new CouponDemo(null, couponDemoCreateRequest.getSenderName(),
            couponDemoCreateRequest.getReceiverName(), couponDemoCreateRequest.getModifier(),
            couponDemoCreateRequest.getMessage(), couponDemoCreateRequest.getBackgroundColor(),
            couponDemoCreateRequest.getCouponType(), "READY");

        CouponDemo saveCouponDemo = couponDemoRepository.save(couponDemo);
        return saveCouponDemo.getId();
    }

    @Transactional(readOnly = true)
    public CouponsResponse findAll() {
        List<CouponResponse> couponResponses = couponDemoRepository.findAll().stream()
            .map(couponDemo -> CouponResponse.of(couponDemo))
            .collect(toList());

        return new CouponsResponse(couponResponses);
    }
}
