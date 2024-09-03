package com.atguigu.lease.web.admin.service;

import com.atguigu.lease.model.entity.ApartmentInfo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ApartmentInfoService extends IService<ApartmentInfo> {

    /**
     * 保存或修改公寓信息
     * @param apartmentSubmitVo
     * @return
     */
    boolean saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    /**
     * 根据条件分页查询公寓列表
     * @param page
     * @param queryVo
     * @return
     */
    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo);

    /**
     * 根据id查询公寓详情信息
     * @param id
     * @return
     */
    ApartmentDetailVo getDetailById(Long id);

    /**
     * 根据id删除公寓信息
     * @param id
     * @return
     */
    boolean removeApartmentById(Long id);
}
