package com.woowacourse.kkogkkog.presentation.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 컨트롤러 구현은 인증 구현 후
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponCreateRequest2 {

    private List<Long> receivers;
    private String modifier;
    private String message;
    private String backgroundColor;
    private String couponType;

    public CouponCreateRequest2(List<Long> receivers, String modifier, String message, String backgroundColor,
                                String couponType) {
        this.receivers = receivers;
        this.modifier = modifier;
        this.message = message;
        this.backgroundColor = backgroundColor;
        this.couponType = couponType;
    }
}
