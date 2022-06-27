package com.jeremy.seckill;

import com.jeremy.seckill.services.ActivityHtmlPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThymeleafServiceTest {

    @Autowired
    private ActivityHtmlPageService activityHtmlPageService;

    @Test
    public void createHtmlTest() {
        activityHtmlPageService.createActivityHtml(19);
    }
}
