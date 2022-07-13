package com.woowacourse.kkogkkog.application.dto;

import com.woowacourse.kkogkkog.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponMemberResponse {

    private Long id;
    private String name;
    private String email;

    public CouponMemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // TODO: replace dummy data with actual member field
    public static CouponMemberResponse of(Member member) {
        return new CouponMemberResponse(member.getId(), member.getName(), "이메일");
    }
}
