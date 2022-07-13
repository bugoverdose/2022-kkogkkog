package com.woowacourse.kkogkkog.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.kkogkkog.application.dto.CouponResponse;
import com.woowacourse.kkogkkog.application.dto.CouponResponse2;
import com.woowacourse.kkogkkog.application.dto.CouponsResponse2;
import com.woowacourse.kkogkkog.domain.Coupon;
import com.woowacourse.kkogkkog.domain.CouponStatus;
import com.woowacourse.kkogkkog.domain.CouponType;
import com.woowacourse.kkogkkog.domain.Member;
import com.woowacourse.kkogkkog.domain.repository.MemberRepository;
import com.woowacourse.kkogkkog.presentation.dto.CouponCreateRequest2;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("쿠폰 관련")
public class CouponAcceptance2Test extends AcceptanceTest {

    private static final String LEO_ACCESS_TOKEN = "레오의_JWT_토큰";
    private static final String JEONG_ACCESS_TOKEN = "정의_JWT_토큰";

    private static final  Member 레오 = new Member(1L, "레오");
    private static final  Member 정 = new Member(2L, "정");
    private static final  Member 루키 = new Member(3L, "루키");
    private static final  Member 아서 = new Member(4L, "아서");

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        memberRepository.save(레오);
        memberRepository.save(정);
        memberRepository.save(루키);
        memberRepository.save(아서);
    }

    @DisplayName("쿠폰을 발급할 수 있다.")
    @Nested
    class CreateCouponTest {

        @Test
        void 로그인된_사용자가_복수의_사용자에게_쿠폰을_발급할_수_있다() {
            CouponCreateRequest2 couponCreateRequest = toCouponCreateRequest(List.of(2L, 3L, 4L));
            쿠폰_발급에_성공한다(couponCreateRequest, LEO_ACCESS_TOKEN);
        }

        @Test
        void 로그인하지_않은_사용자는_쿠폰을_발급할_수_없다() {
            CouponCreateRequest2 couponCreateRequest = toCouponCreateRequest(List.of(2L, 3L, 4L));
            ExtractableResponse<Response> response = 쿠폰_발급을_요청한다(couponCreateRequest, null);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void 자신에게_쿠폰을_발급할_수_없다() {
            CouponCreateRequest2 couponCreateRequest = toCouponCreateRequest(List.of(1L, 2L));

            ExtractableResponse<Response> response = 쿠폰_발급을_요청한다(couponCreateRequest, LEO_ACCESS_TOKEN);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 받는_사람이_존재하지_않는_경우_쿠폰_발급을_실패한다() {
            CouponCreateRequest2 couponCreateRequest = toCouponCreateRequest(List.of(999L));

            ExtractableResponse<Response> response = 쿠폰_발급을_요청한다(couponCreateRequest, LEO_ACCESS_TOKEN);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }

    @DisplayName("받은 쿠폰과 보낸 쿠폰을 전체 조회할 수 있다.")
    @Nested
    class ShowAllTest {

        @Test
        void 로그인된_사용자는_받은_쿠폰과_보낸_쿠폰을_전부_조회할_수_있다() {
            쿠폰_발급에_성공한다(toCouponCreateRequest(List.of(2L, 3L)), LEO_ACCESS_TOKEN);
            쿠폰_발급에_성공한다(toCouponCreateRequest(List.of(1L, 4L)), JEONG_ACCESS_TOKEN);

            CouponsResponse2 actual =  쿠폰_전체_조회에_성공한다(LEO_ACCESS_TOKEN);
            CouponsResponse2 expected =  new CouponsResponse2(
                    List.of(toCouponResponse(4L, 정, 레오)),
                    List.of(toCouponResponse(1L, 레오, 정), toCouponResponse(2L, 레오, 루키)));

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        //  TODO:
    }

    public static CouponsResponse2 쿠폰_발급에_성공한다(CouponCreateRequest2 couponCreateRequest, String accessToken) {
        ExtractableResponse<Response> response = 쿠폰_발급을_요청한다(couponCreateRequest, accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(CouponsResponse2.class);
    }

    public static ExtractableResponse<Response> 쿠폰_발급을_요청한다(CouponCreateRequest2 couponCreateRequest,
                                                            String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponCreateRequest)
                .when()
                .post("/api/coupons")
                .then().log().all()
                .extract();
    }

    public static CouponsResponse2 쿠폰_전체_조회에_성공한다(String accessToken) {
        ExtractableResponse<Response> extract = 쿠폰_전체_조회를_요청한다(accessToken);

        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        return extract.as(CouponsResponse2.class);
    }

    public static ExtractableResponse<Response> 쿠폰_전체_조회를_요청한다(String accessToken) {
       return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/coupons")
                .then().log().all()
                .extract();
    }

    private CouponCreateRequest2 toCouponCreateRequest(List<Long> receiverIds) {
        return new CouponCreateRequest2(receiverIds, "한턱내는", "추가 메세지", "#123456", "COFFEE");
    }

    private CouponResponse2 toCouponResponse(Long couponId, Member sender, Member receiver){
        Coupon coupon = new Coupon(couponId, sender, receiver, "한턱내는", "추가 메세지", "#123456", CouponType.COFFEE, CouponStatus.READY);
        return CouponResponse2.of(coupon);
    }

    public static CouponResponse 쿠폰_조회에_성공한다(Long couponId) {
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when()
                .get("/api/coupons/" + couponId)
                .then().log().all()
                .extract();

        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        return extract.as(CouponResponse.class);
    }
}
