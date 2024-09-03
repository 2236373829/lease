package com.atguigu.lease.web.app.service;

import com.atguigu.lease.model.entity.RoomInfo;
import com.atguigu.lease.web.app.vo.room.RoomDetailVo;
import com.atguigu.lease.web.app.vo.room.RoomItemVo;
import com.atguigu.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface RoomInfoService extends IService<RoomInfo> {
    /**
     * 根据条件分页查询房间列表信息
     * @param page
     * @param queryVo
     * @return
     */
    IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo);

    /**
     * 根据id查询房间详情信息
     * @param id
     * @return
     */
    RoomDetailVo getRoomDetailById(Long id);

    /**
     * 根据公寓id分页查询房间列表
     * @param page
     * @param id
     * @return
     */
    IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> page, Long id);
}
