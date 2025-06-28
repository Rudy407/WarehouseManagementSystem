package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.Goods;
import org.example.wms.service.GoodsService;
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
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //物品查询
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        List<Goods> list= goodsService.lambdaQuery().eq(Goods::getName,name).list();
        return list.size()>0?Result.success(list.get(0)):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.success():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.success():Result.fail();
    }
    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Goods goods){
        return goodsService.saveOrUpdate(goods)?Result.success():Result.fail();
    }

    //删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Goods goods){
        return goodsService.removeById(goods.getId())?Result.success():Result.fail();
    }

    //分页查询(结果封装）
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam queryPageParam) {
        Page<Goods> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        // 检查param是否为null
        HashMap<String, Object> param = queryPageParam.getParam();
        String name = param.get("name").toString();
        Integer goodstype=(Integer) param.get("goodstype");
        Integer storage=(Integer) param.get("storage");

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Goods::getName, name);
        }
        if (goodstype != null) {
            lambdaQueryWrapper.like(Goods::getGoodstype, goodstype);
        }
        if (storage != null) {
            lambdaQueryWrapper.like(Goods::getStorage, storage);
        }

        IPage<Goods> goodsIPage = goodsService.page(page, lambdaQueryWrapper);
        //System.out.println("Total===" + goodsIPage.getTotal());

        return Result.success(goodsIPage.getTotal(), goodsIPage.getRecords());
    }

}
