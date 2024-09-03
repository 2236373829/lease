package com.atguigu.lease.web.admin.service;

import com.atguigu.lease.model.entity.RoomInfo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface RoomInfoService extends IService<RoomInfo> {

    /**
     * 保存或修改房间信息
     * @param roomSubmitVo
     * @return
     */
    boolean saveOrUpdateRoomInfo(RoomSubmitVo roomSubmitVo);

    /**
     * 根据条件分页查询房间信息
     * @param roomItemVoPage
     * @param queryVo
     * @return
     */
    IPage<RoomItemVo> roomByPage(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);

    /**
     * 根据id获取房间详情信息
     * @param id
     * @return
     */
    RoomDetailVo selectRoomDetailsById(Long id);

    /**
     * 根据id删除房间信息
     * @param id
     */
    void removeRoomById(Long id);
}
