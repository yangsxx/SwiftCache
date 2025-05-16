package top.yangsc.swiftcache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yangsc.swiftcache.ai.HuoShanAiService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HuoShanAiServiceTest {

    @Autowired
    private HuoShanAiService huoShanAiService;

    @Test
    void testSimpleGenerateText() {
        // 测试正常请求
        String question = "你好，请介绍一下你自己";
        String response = huoShanAiService.simpleGenerateText(question);
        
        assertNotNull(response);
        assertTrue(response.length() > 0);
        
        System.out.println("AI响应: " + response);
    }

    @Test
    void testEmptyQuestion() {
        // 测试空问题
        String response = huoShanAiService.simpleGenerateText("");
        assertNotNull(response);
    }
}
