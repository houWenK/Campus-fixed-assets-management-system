package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.QueryPageParam;
import com.example.demo.common.Result;
import com.example.demo.entity.Goodstype;
import com.example.demo.entity.Menu;
import com.example.demo.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-03-31
 */
@CrossOrigin
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;

    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody Goodstype goodstype) {
        return goodstypeService.saveOrUpdate(goodstype) ? Result.suc() : Result.fall();
    }

    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return goodstypeService.removeById(id) ? Result.suc() : Result.fall();
    }

    @PostMapping("/listPage")
    public Result ListP(@RequestBody QueryPageParam query) {
        String name = (String) query.getParam().get("name");
        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Goodstype::getName, name);
        }
        Page<Goodstype> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage result = goodstypeService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List list = goodstypeService.list();
        return Result.suc(list);
    }
}
