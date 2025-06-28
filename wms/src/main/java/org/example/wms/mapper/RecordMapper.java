package org.example.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.wms.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.wms.entity.RecordRes;


import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Rudy
 * @since 2025-06-26
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    public IPage<RecordRes> listPage(Page<Record> page, String goodsname,Integer goodstype,Integer storage);

    public IPage<RecordRes> listPageById(Page<Record> page, String goodsname,Integer goodstype,Integer storage,Integer userid);
}
