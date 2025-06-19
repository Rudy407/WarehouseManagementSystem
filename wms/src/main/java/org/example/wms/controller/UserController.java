package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.User;
import org.example.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Rudy
 * @since 2025-06-17
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    //新增
    @PostMapping("/save")
    public boolean save(@RequestBody User user){
        return userService.save(user);
    }

    //修改
    @PostMapping("/update")
    public boolean update(@RequestBody User user){
        return userService.updateById(user);
    }
    //新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return userService.removeById(id);
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, user.getName());
        return userService.list(lambdaQueryWrapper);
    }

    //分页查询
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam queryPageParam){

        Page<User> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, queryPageParam.getParam().get("name"));

        IPage<User> userIPage = userService.page(page, lambdaQueryWrapper);
        System.out.println("Toal==="+userIPage.getTotal());
        return userIPage.getRecords();
    }

    //返回结果
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam queryPageParam){

        Page<User> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, queryPageParam.getParam().get("name"));

        IPage<User> userIPage = userService.page(page, lambdaQueryWrapper);
        System.out.println("Toal==="+userIPage.getTotal());
        return Result.success(userIPage.getTotal(),userIPage.getRecords());
    }

}
