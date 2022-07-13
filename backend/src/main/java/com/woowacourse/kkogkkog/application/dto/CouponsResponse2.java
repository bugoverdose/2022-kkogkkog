package com.woowacourse.kkogkkog.application.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponsResponse2 {

    private CouponsResponses data;

    public CouponsResponse2(List<CouponResponse2> received, List<CouponResponse2> sent) {
        this.data = new CouponsResponses(received, sent);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    private static class CouponsResponses {

        private List<CouponResponse2> received;
        private List<CouponResponse2> sent;

        public CouponsResponses(List<CouponResponse2> received,
                                List<CouponResponse2> sent) {
            this.received = received;
            this.sent = sent;
        }
    }
}
