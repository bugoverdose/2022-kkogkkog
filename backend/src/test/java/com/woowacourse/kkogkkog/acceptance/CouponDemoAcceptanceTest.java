package com.woowacourse.kkogkkog.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.kkogkkog.presentation.CouponDemoController.CouponDemoCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponDemoAcceptanceTest extends AcceptanceTest {

    private static final String BACKGROUND_COLOR = "red";
    private static final String MODIFIER = "한턱내는";
    private static final String MESSAGE = "추가 메세지";
    private static final String COUPON_TYPE = "커피";

    @Test
    void 쿠폰_발급을_할_수_있다() {
        CouponDemoCreateRequest couponCreateRequest = new CouponDemoCreateRequest("루키", "아서",
            BACKGROUND_COLOR, MODIFIER, MESSAGE,
            COUPON_TYPE);

        ExtractableResponse<Response> extract = createCoupon(
            couponCreateRequest);
        Long couponId = Long.valueOf(extract.header("Location").split("/")[4]);

        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(couponId).isNotNull();
    }

    private ExtractableResponse<Response> createCoupon(
        CouponDemoCreateRequest couponCreateRequest) {
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
            .body(couponCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/demo/coupons")
            .then().log().all()
            .extract();
        return extract;
    }

    @Test
    void 전체_쿠폰_조회를_할_수_있다() {
        CouponDemoCreateRequest couponCreateRequest1 = new CouponDemoCreateRequest("루키", "아서",
            BACKGROUND_COLOR, MODIFIER, MESSAGE,
            COUPON_TYPE);
        CouponDemoCreateRequest couponCreateRequest2 = new CouponDemoCreateRequest("루키", "정",
            BACKGROUND_COLOR, MODIFIER, MESSAGE,
            COUPON_TYPE);

        createCoupon(couponCreateRequest1);
        createCoupon(couponCreateRequest2);

        ExtractableResponse<Response> extract = RestAssured.given().log().all()
            .when()
            .get("/api/demo/coupons")
            .then().log().all()
            .extract();

        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
