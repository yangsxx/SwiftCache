package top.yangsc.swiftcache.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.yangsc.swiftcache.base.Exception.ParameterValidationException;
import top.yangsc.swiftcache.base.ResultData;
import top.yangsc.swiftcache.config.PageResult;
import top.yangsc.swiftcache.controller.bean.vo.KeyTablePageVO;
import top.yangsc.swiftcache.controller.bean.vo.PageBaseVO;
import top.yangsc.swiftcache.controller.bean.vo.UpdateKeyValueVO;
import top.yangsc.swiftcache.controller.bean.vo.resp.KeyTableRespVO;
import top.yangsc.swiftcache.services.KeyTableService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：top.yangsc.swiftcache.controller
 *
 * @author yang
 * @date 2025/5/13 10:09
 */

@RestController
@RequestMapping("/keyTable")
@Tag(name = "词条管理")
public class KeyTableController {

    @Resource
    private KeyTableService keyTableService;

    @PostMapping("/create")
    @Operation(summary = "创建词条")
    public ResultData<String> createKeyTable(@RequestBody UpdateKeyValueVO createKeyTableVO) {
        boolean keyTable = keyTableService.createKeyTable(createKeyTableVO);
        return  keyTable?  ResultData.ok("创建成功") : ResultData.error("创建失败");

    }

    @PostMapping("/update")
    public ResultData<String> updateValue(@RequestBody UpdateKeyValueVO createKeyTableVO) {
        boolean keyTable = keyTableService.updateKeyTable(createKeyTableVO);
        return  keyTable ?  ResultData.ok("更新成功") : ResultData.error("更新失败");
    }

    @GetMapping("/get")
    public ResultData<KeyTableRespVO> getValue(@RequestParam Long id) {
        return  ResultData.ok(keyTableService.getValue(id));
    }

    @GetMapping("/delete")
    public ResultData<String> delete(@RequestParam Long id) {
        boolean ok = keyTableService.delete(id);
        return  ok?  ResultData.ok("删除成功") : ResultData.error("删除失败");
    }

    @GetMapping("/updatePermission")
    public ResultData<String> updatePermission(@RequestParam Long id, @RequestParam int permission) {
        if (permission !=0 && permission!=1 &&  permission!=-1){
           throw new ParameterValidationException("权限参数错误");
        }
        boolean ok = keyTableService.updatePermission(id, permission);
        return  ok?  ResultData.ok("更新成功") : ResultData.error("更新失败");
    }

    @PostMapping("/getByPage")
    public ResultData<PageResult<KeyTableRespVO>> getValueByPage(@RequestBody KeyTablePageVO pageBaseVO) {
        return  ResultData.ok(keyTableService.getKeyTableByPage(pageBaseVO));
    }


}
