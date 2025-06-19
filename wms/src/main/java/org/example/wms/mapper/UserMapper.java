package org.example.wms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.wms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Rudy
 * @since 2025-06-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
