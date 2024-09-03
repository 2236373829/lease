package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.LeaseAgreement;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.app.mapper.GraphInfoMapper;
import com.atguigu.lease.web.app.mapper.LeaseAgreementMapper;
import com.atguigu.lease.web.app.service.LeaseAgreementService;
import com.atguigu.lease.web.app.vo.agreement.AgreementDetailVo;
import com.atguigu.lease.web.app.vo.agreement.AgreementItemVo;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper mapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Override
    public List<AgreementItemVo> listItemByPhone(String phone) {
        return mapper.listItemByPhone(phone);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {
        AgreementDetailVo agreementDetailVo = mapper.getDetailById(id);
        // 查询公寓图片
        List<GraphVo> apartmentGraphList = graphInfoMapper.selectByItemTypeAndItemId(ItemType.APARTMENT, agreementDetailVo.getApartmentId());
        agreementDetailVo.setApartmentGraphVoList(apartmentGraphList);

        // 查询房间图片
        List<GraphVo> roomGraphList = graphInfoMapper.selectByItemTypeAndItemId(ItemType.ROOM, agreementDetailVo.getRoomId());
        agreementDetailVo.setRoomGraphVoList(roomGraphList);

        return agreementDetailVo;
    }
}




