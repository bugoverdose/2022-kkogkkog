package com.woowacourse.kkogkkog.domain.repository;

import com.woowacourse.kkogkkog.domain.CouponDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponDemoRepository extends JpaRepository<CouponDemo, Long> {
}
