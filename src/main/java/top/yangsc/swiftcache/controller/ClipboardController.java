package top.yangsc.swiftcache.controller;



import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clipboard")
@Tag(name = "剪贴板", description = "剪贴板相关接口")
public class ClipboardController {
}
