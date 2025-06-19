package org.example.wms.service.impl;

import org.example.wms.entity.User;
import org.example.wms.mapper.UserMapper;
import org.example.wms.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Rudy
 * @since 2025-06-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
