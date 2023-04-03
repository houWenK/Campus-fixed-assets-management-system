package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.QueryPageParam;
import com.example.demo.common.Result;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Storage;
import com.example.demo.service.StorageService;
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
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody Storage storage) {
        return storageService.saveOrUpdate(storage) ? Result.suc() : Result.fall();
    }

    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return storageService.removeById(id) ? Result.suc() : Result.fall();
    }

    @PostMapping("/listPage")
    public Result ListP(@RequestBody QueryPageParam query) {
        String name = (String) query.getParam().get("name");
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Storage::getName, name);
        }
        Page<Storage> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage result = storageService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List list = storageService.list();
        return Result.suc(list);
    }
}
