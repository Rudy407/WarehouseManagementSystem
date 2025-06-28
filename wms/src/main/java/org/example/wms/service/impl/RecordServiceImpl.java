package org.example.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.entity.Record;
import org.example.wms.entity.RecordRes;
import org.example.wms.mapper.RecordMapper;
import org.example.wms.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Rudy
 * @since 2025-06-26
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public IPage<RecordRes> listPage(Page<Record> page, String goodsname,Integer goodstype,Integer storage) {
        return recordMapper.listPage(page,goodsname,goodstype,storage);
    }

    @Override
    public IPage<RecordRes> listPageById(Page<Record> page, String goodsname,Integer goodstype,Integer storage,Integer userid) {
        return recordMapper.listPageById(page,goodsname,goodstype,storage,userid);
    }
}