package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.Goodstype;
import org.example.wms.service.GoodstypeService;
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
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;

    //物品类型查询
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        List<Goodstype> list= goodstypeService.lambdaQuery().eq(Goodstype::getName,name).list();
        return list.size()>0?Result.success(list.get(0)):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype)?Result.success():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype)?Result.success():Result.fail();
    }
    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Goodstype goodstype){
        return goodstypeService.saveOrUpdate(goodstype)?Result.success():Result.fail();
    }

    //删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Goodstype goodstype){
        return goodstypeService.removeById(goodstype.getId())?Result.success():Result.fail();
    }

    //分页查询(结果封装）
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam queryPageParam) {
        Page<Goodstype> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        // 检查param是否为null
        HashMap<String, Object> param = queryPageParam.getParam();
        String name = param.get("name").toString();

        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Goodstype::getName, name);
        }

        IPage<Goodstype> goodstypeIPage = goodstypeService.page(page, lambdaQueryWrapper);
        //System.out.println("Total===" + goodstypeIPage.getTotal());

        return Result.success(goodstypeIPage.getTotal(), goodstypeIPage.getRecords());
    }

}
