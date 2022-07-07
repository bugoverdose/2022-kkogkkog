package com.woowacourse.kkogkkog.presentation;

import com.woowacourse.kkogkkog.application.CouponDemoService;
import com.woowacourse.kkogkkog.application.dto.CouponsResponse;
import java.net.URI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/demo/coupons")
@RestController
public class CouponDemoController {

    private final CouponDemoService couponDemoService;

    public CouponDemoController(CouponDemoService couponDemoService) {
        this.couponDemoService = couponDemoService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CouponDemoCreateRequest couponDemoCreateRequest) {
        Long couponId = couponDemoService.save(couponDemoCreateRequest);

        return ResponseEntity.created(URI.create("/api/demo/coupons/" + couponId)).build();
    }

    @GetMapping
    public ResponseEntity<CouponsResponse> showAll() {
        CouponsResponse couponsResponse = couponDemoService.findAll();

        return ResponseEntity.ok(couponsResponse);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class CouponDemoCreateRequest {

        private String senderName;
        private String receiverName;
        private String modifier;
        private String message;
        private String backgroundColor;
        private String couponType;

        public CouponDemoCreateRequest(String senderName, String receiverName, String modifier,
            String message, String backgroundColor, String couponType) {
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.modifier = modifier;
            this.message = message;
            this.backgroundColor = backgroundColor;
            this.couponType = couponType;
        }
    }
}
