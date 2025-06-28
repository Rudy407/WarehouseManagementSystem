package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.Goods;
import org.example.wms.entity.Record;
import org.example.wms.entity.RecordRes;
import org.example.wms.mapper.GoodsMapper;
import org.example.wms.mapper.RecordMapper;
import org.example.wms.service.GoodsService;
import org.example.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Rudy
 * @since 2025-06-26
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    //出入库记录
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Integer action=record.getAction();
        Goods goods = goodsService.getById(record.getGoods());
        if(action.equals(1)){
            goods.setCount(goods.getCount() + record.getCount());
        }
        else if(action.equals(2)){
            goods.setCount(goods.getCount() - record.getCount());
        }
        goodsService.updateById(goods);
        return recordService.save(record)?Result.success(record):Result.fail();
    }

    //自定义分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam queryPageParam){
        Page<Record> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        // 检查param是否为null
        HashMap<String, Object> param = queryPageParam.getParam();
        String goodsname =param.get("goodsname").toString();
        Integer goodstype=(Integer) param.get("goodstype");
        Integer storage=(Integer) param.get("storage");
        Integer userid=(Integer) param.get("userid");
        Integer roleId=(Integer) param.get("roleId");
        if(roleId!=null&&roleId.equals(2)){
            IPage<RecordRes> recordIPage = recordService.listPageById(page,goodsname,goodstype,storage,userid);
            return Result.success(recordIPage.getTotal(), recordIPage.getRecords());
        }

        IPage<RecordRes> recordIPage = recordService.listPage(page,goodsname,goodstype,storage);
        //System.out.println("Total===" + recordIPage.getTotal());

        return Result.success(recordIPage.getTotal(), recordIPage.getRecords());
    }

}