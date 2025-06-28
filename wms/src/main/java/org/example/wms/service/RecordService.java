package org.example.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.wms.entity.RecordRes;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Rudy
 * @since 2025-06-26
 */
public interface RecordService extends IService<Record> {
    public IPage<RecordRes> listPage(Page<Record> page, String goodsname,Integer goodstype,Integer storage);

    public IPage<RecordRes> listPageById(Page<Record> page, String goodsname,Integer goodstype,Integer storage,Integer userid);
}
