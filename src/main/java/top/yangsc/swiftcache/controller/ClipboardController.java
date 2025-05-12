package top.yangsc.swiftcache.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.controller.bean.vo.ClipboardPageVO;
import top.yangsc.swiftcache.controller.bean.vo.CreateClipboardVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.ClipboardRespVO;
import top.yangsc.swiftcache.services.ClipboardHistoryService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/clipboard")
@Tag(name = "剪贴板", description = "剪贴板相关接口")
public class ClipboardController {
    @Resource

    private ClipboardHistoryService  clipboardHistoryService;

    @PostMapping("/create")
    @Operation(summary = "创建剪贴板")
    public ResultData<String> createClipboard(@RequestBody CreateClipboardVO createClipboardVO)
    {
        return clipboardHistoryService.createClipboard(createClipboardVO);
    }

    @GetMapping("/getByPage")
    @Operation(summary = "分页获取剪贴板")
    public ResultData<ClipboardRespVO> getClipboardByPage(@RequestBody ClipboardPageVO clipboardPageVO)
    {
        return clipboardHistoryService.getClipboard();
    }


}
