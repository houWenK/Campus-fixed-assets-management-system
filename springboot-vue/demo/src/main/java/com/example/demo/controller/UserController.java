package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.QueryPageParam;
import com.example.demo.common.Result;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.service.MenuService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wms
 * @since 2022-12-06
 */
@CrossOrigin//或者common下的CorsConfig
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list = userService.lambdaQuery().eq(User::getNo, user.getNo()).eq(User::getPassword, user.getPassword()).list();
        if(list.size()>0){
            User user1=(User)list.get(0);
            List menulist=menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap res=new HashMap();
            res.put("user",user1);
            res.put("menu",menulist);
           return Result.suc(res);
        }
        return  Result.fall();
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
        List list = userService.lambdaQuery().eq(User::getNo, no).list();
        return list.size() > 0 ? Result.suc(list) : Result.fall();
    }

    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user) ? Result.suc() : Result.fall();
    }

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user) ? Result.suc() : Result.fall();
    }

    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }

    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return userService.removeById(id) ? Result.suc() : Result.fall();
    }

    @PostMapping("/listP")
    public Result ListP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(user.getName())) lambdaQueryWrapper.like(User::getName, user.getName());
        return Result.suc(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
    public Result ListP(@RequestBody QueryPageParam query) {
        String name = (String) query.getParam().get("name");
        String sex = (String) query.getParam().get("sex");
        String roldId = (String) query.getParam().get("roleId");
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex)) {
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roldId)) {
            lambdaQueryWrapper.eq(User::getRoleId, roldId);
        }
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage result = userService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }
}
