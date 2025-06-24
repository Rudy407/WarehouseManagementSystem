package org.example.wms.controller;


import org.example.wms.common.Result;
import org.example.wms.entity.Menu;
import org.example.wms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Rudy
 * @since 2025-06-24
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public Result list(@RequestParam String roleId) {
        List<Menu> menus = menuService.lambdaQuery().like(Menu::getMenuright,roleId).list();
        return Result.success(menus);
    }
}
