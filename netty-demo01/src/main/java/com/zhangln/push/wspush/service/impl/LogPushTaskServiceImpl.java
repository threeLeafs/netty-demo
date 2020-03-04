package com.zhangln.push.wspush.service.impl;

import com.zhangln.push.wspush.service.ILogPushTaskService;
import com.zhangln.push.wspush.entity.LogPushTaskEntity;
import com.zhangln.push.wspush.mapper.LogPushTaskMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推送日志 服务实现类
 * </p>
 *
 * @author sherry
 * @since 2019-12-07
 */
@Service
public class LogPushTaskServiceImpl extends ServiceImpl<LogPushTaskMapper, LogPushTaskEntity> implements ILogPushTaskService {

}
