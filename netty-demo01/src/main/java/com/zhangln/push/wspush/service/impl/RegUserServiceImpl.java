package com.zhangln.push.wspush.service.impl;

import com.zhangln.push.wspush.service.IRegUserService;
import com.zhangln.push.wspush.entity.RegUserEntity;
import com.zhangln.push.wspush.mapper.RegUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 注册用户，即允许使用推送服务的账号 服务实现类
 * </p>
 *
 * @author sherry
 * @since 2019-12-06
 */
@Service
public class RegUserServiceImpl extends ServiceImpl<RegUserMapper, RegUserEntity> implements IRegUserService {

}
