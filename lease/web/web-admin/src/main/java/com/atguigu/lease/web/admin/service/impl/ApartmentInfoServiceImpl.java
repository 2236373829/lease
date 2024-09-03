package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.atguigu.lease.web.admin.vo.fee.FeeValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private ApartmentLabelService apartmentLabelService;
    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;
    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    @Transactional
    public boolean saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);

        // id不为null操作为修改
        if (isUpdate) {
            //1.删除图片列表
            LambdaUpdateWrapper<GraphInfo> graphWrapper = new LambdaUpdateWrapper<>();
            graphWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            boolean graphRemove = graphInfoService.remove(graphWrapper);

            //2.删除配套列表
            LambdaUpdateWrapper<ApartmentFacility> facilityWrapper = new LambdaUpdateWrapper<>();
            facilityWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            boolean facilityRemove = apartmentFacilityService.remove(facilityWrapper);

            //3.删除标签列表
            LambdaUpdateWrapper<ApartmentLabel> labelWrapper = new LambdaUpdateWrapper<>();
            labelWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            boolean labelRemove = apartmentLabelService.remove(labelWrapper);

            //4.删除杂费列表
            LambdaUpdateWrapper<ApartmentFeeValue> feeWrapper = new LambdaUpdateWrapper<>();
            feeWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            boolean feeRemove = apartmentFeeValueService.remove(feeWrapper);

        }

        // id为null 操作添加
        //1.插入图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo vo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(vo.getName());
                graphInfo.setUrl(vo.getUrl());
                graphInfoList.add(graphInfo);
            }
            boolean saveGraphBatch = graphInfoService.saveBatch(graphInfoList);
        }

        //2.插入配套列表
        List<Long> facilityVoList = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityVoList)) {
            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            for (Long facilityId : facilityVoList) {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityId);
                facilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityList);
        }

        //3.插入标签列表
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }
        //4.插入杂费列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }

        return false;
    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        // 1.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);

        // 2.查询图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);

        // 3.查询标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);

        // 4.查询配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);

        // 5.查询杂费列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);

        // 6.
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);

        return apartmentDetailVo;
    }

    @Override
    public boolean removeApartmentById(Long id) {

        LambdaQueryWrapper<RoomInfo> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(RoomInfo::getApartmentId, id);
        Long roomCount = roomInfoMapper.selectCount(roomQueryWrapper);
        if (roomCount > 0) {
            //终止删除,响应提示信息
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }

        boolean removeApartment = this.removeById(id);
        //1.删除图片列表
        LambdaUpdateWrapper<GraphInfo> graphWrapper = new LambdaUpdateWrapper<>();
        graphWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphWrapper.eq(GraphInfo::getItemId, id);
        boolean graphRemove = graphInfoService.remove(graphWrapper);

        //2.删除配套列表
        LambdaUpdateWrapper<ApartmentFacility> facilityWrapper = new LambdaUpdateWrapper<>();
        facilityWrapper.eq(ApartmentFacility::getApartmentId, id);
        boolean facilityRemove = apartmentFacilityService.remove(facilityWrapper);

        //3.删除标签列表
        LambdaUpdateWrapper<ApartmentLabel> labelWrapper = new LambdaUpdateWrapper<>();
        labelWrapper.eq(ApartmentLabel::getApartmentId, id);
        boolean labelRemove = apartmentLabelService.remove(labelWrapper);

        //4.删除杂费列表
        LambdaUpdateWrapper<ApartmentFeeValue> feeWrapper = new LambdaUpdateWrapper<>();
        feeWrapper.eq(ApartmentFeeValue::getApartmentId, id);
        boolean feeRemove = apartmentFeeValueService.remove(feeWrapper);

        return removeApartment;
    }
}




