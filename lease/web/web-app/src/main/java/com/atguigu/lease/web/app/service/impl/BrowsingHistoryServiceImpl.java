package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.BrowsingHistory;
import com.atguigu.lease.web.app.mapper.BrowsingHistoryMapper;
import com.atguigu.lease.web.app.service.BrowsingHistoryService;
import com.atguigu.lease.web.app.vo.history.HistoryItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liubo
 * @description 针对表【browsing_history(浏览历史)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory>
        implements BrowsingHistoryService {

    @Autowired
    private BrowsingHistoryMapper mapper;

    @Override
    public IPage<HistoryItemVo> pageListByUserId(Page<HistoryItemVo> page, Long userId) {
        return mapper.pageListByUserId(page, userId);
    }

    @Override
    @Async // 异步操作
    public void saveHistory(Long userId, Long id) {
        System.out.println("保存浏览历史" + Thread.currentThread().getName());

        //　查询当前用户是否浏览过当前房间信息
        LambdaQueryWrapper<BrowsingHistory> browsingHistoryQueryWrapper = new LambdaQueryWrapper<>();
        browsingHistoryQueryWrapper.eq(BrowsingHistory::getUserId, userId);
        browsingHistoryQueryWrapper.eq(BrowsingHistory::getRoomId, id);
        BrowsingHistory browsingHistory = mapper.selectOne(browsingHistoryQueryWrapper);
        if (browsingHistory != null) { // 不为null说明浏览过当前房间 只更新浏览时间
            browsingHistory.setBrowseTime(new Date());
            mapper.updateById(browsingHistory);
        } else { // 为null没有浏览过 保存新的浏览信息
            browsingHistory = new BrowsingHistory();
            browsingHistory.setUserId(userId);
            browsingHistory.setRoomId(id);
            browsingHistory.setBrowseTime(new Date());
            mapper.insert(browsingHistory);
        }
    }
}
