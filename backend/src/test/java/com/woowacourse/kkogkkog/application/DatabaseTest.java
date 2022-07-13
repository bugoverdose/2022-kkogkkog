package com.woowacourse.kkogkkog.application;

import com.woowacourse.kkogkkog.acceptance.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: ServiceTest로 수정, DatabaseCleaner 의존성 해결
@SpringBootTest
public class DatabaseTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }
}
