package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2023-03-31
 */
public interface RecordService extends IService<Record> {
        IPage pageCC(IPage<Record> page, Wrapper wrapper);
}
