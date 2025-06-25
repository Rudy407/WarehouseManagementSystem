package org.example.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.wms.common.QueryPageParam;
import org.example.wms.common.Result;
import org.example.wms.entity.Menu;
import org.example.wms.entity.User;
import org.example.wms.mapper.UserMapper;
import org.example.wms.service.MenuService;
import org.example.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
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

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    //账号查询
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List<User> list= userService.lambdaQuery().eq(User::getNo,no).list();
        return list.size()>0?Result.success(list.get(0)):Result.fail();
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List<User> list= userService.lambdaQuery().eq(User::getNo,user.getNo()).eq(User::getPassword,user.getPassword()).list();
        if(list.size()>0){
            User user0 = list.get(0);
            List<Menu> menus=menuService.lambdaQuery().like(Menu::getMenuright,user0.getRoleId()).list();
            HashMap res=new HashMap<String,Object>();
            res.put("user",user0);
            res.put("menu",menus);
            return Result.success(res);
        }
        return list.size()>0?Result.success(list.get(0)):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user)?Result.success():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateById(user)?Result.success():Result.fail();
    }
    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody User user){
        return userService.saveOrUpdate(user)?Result.success():Result.fail();
    }

    //删除
    @PostMapping("/delete")
    public Result delete(@RequestBody User user){
        return userService.removeById(user.getId())?Result.success():Result.fail();
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName, user.getName());
        }
        return Result.success(userService.list(lambdaQueryWrapper));
    }

    //分页查询
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam queryPageParam){

        Page<User> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, queryPageParam.getParam().get("name"));

        IPage<User> userIPage = userService.page(page, lambdaQueryWrapper);
        //System.out.println("Toal==="+userIPage.getTotal());
        return userIPage.getRecords();
    }

    //分页查询(结果封装）
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam queryPageParam) {
        Page<User> page = new Page<>(queryPageParam.getPageNum(), queryPageParam.getPageSize());

        // 检查param是否为null
        HashMap<String, Object> param = queryPageParam.getParam();
        String name = param.get("name").toString();
        Integer roleId = (Integer) param.get("roleId");
        Integer sex=(Integer) param.get("sex");

//        if (param != null && param.get("name") != null) {
//            name = param.get("name").toString();
//        }

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (roleId != null) {
            lambdaQueryWrapper.eq(User::getRoleId, roleId);
        }
        if (sex != null) {
            lambdaQueryWrapper.eq(User::getSex, sex);
        }

        IPage<User> userIPage = userService.page(page, lambdaQueryWrapper);
        //System.out.println("Total===" + userIPage.getTotal());

        return Result.success(userIPage.getTotal(), userIPage.getRecords());
    }

}
