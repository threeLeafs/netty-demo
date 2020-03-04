package com.zhangln.push.wspush.service.impl;

import com.zhangln.push.wspush.service.IAccessTokenService;
import com.zhangln.push.wspush.entity.AccessTokenEntity;
import com.zhangln.push.wspush.mapper.AccessTokenMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * token验证表 服务实现类
 * </p>
 *
 * @author sherry
 * @since 2019-12-07
 */
@Service
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessTokenEntity> implements IAccessTokenService {

}
