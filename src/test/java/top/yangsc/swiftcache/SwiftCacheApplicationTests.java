package top.yangsc.swiftcache;

import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yangsc.swiftcache.Schedule.CountTask;
import top.yangsc.swiftcache.Schedule.mq.KeysDTO;
import top.yangsc.swiftcache.Schedule.mq.Producer;
import top.yangsc.swiftcache.ai.HuoShanAiService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SwiftCacheApplicationTests {

	@Autowired
	private HuoShanAiService huoShanAiService;

	@Autowired
	private CountTask countTask;

	@Autowired
	private Producer producer;

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

	@Test
	void testCountTask() {
		countTask.count();
	}

	@Test
	void testProducer() {
		KeysDTO keysDTO =new KeysDTO();
		keysDTO.setValue("docker exec -it rabbitmq bash");
		keysDTO.setValueTableId(10000200L);
		producer.sendAiTask(JSONUtil.toJsonStr(keysDTO));
	}
}
