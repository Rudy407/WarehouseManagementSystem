package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.Storage;
import org.example.wms.entity.User;
import org.example.wms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Rudy
 * @since 2025-06-25
 */
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    //仓库查询
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        List<Storage> list= storageService.lambdaQuery().eq(Storage::getName,name).list();
        return list.size()>0?Result.success(list.get(0)):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)?Result.success():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage)?Result.success():Result.fail();
    }
    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Storage storage){
        return storageService.saveOrUpdate(storage)?Result.success():Result.fail();
    }

    //删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Storage storage){
        return storageService.removeById(storage.getId())?Result.success():Result.fail();
    }

    //分页查询(结果封装）
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam queryPageParam) {
        Page<Storage> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        // 检查param是否为null
        HashMap<String, Object> param = queryPageParam.getParam();
        String name = param.get("name").toString();

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Storage::getName, name);
        }

        IPage<Storage> storageIPage = storageService.page(page, lambdaQueryWrapper);
        //System.out.println("Total===" + storageIPage.getTotal());

        return Result.success(storageIPage.getTotal(), storageIPage.getRecords());
    }
}
