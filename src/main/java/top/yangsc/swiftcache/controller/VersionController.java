package top.yangsc.swiftcache.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yangsc.swiftcache.services.impl.GitVersionService;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/common")
@Tag(name ="公共接口")
public class VersionController {
    
    @Resource
    private GitVersionService gitVersionService;

    @GetMapping("/version")
    @Operation(summary = "获取版本号")
    public ResponseEntity<Map<String,String>> getVersion() {
        return ResponseEntity.ok(gitVersionService.getVersion());
    }

    @GetMapping("/thread-check")
    public String threadCheck() {
        Thread thread = Thread.currentThread();
        return String.format("Thread: %s, isVirtual: %b", thread, thread.isVirtual());
    }
}
