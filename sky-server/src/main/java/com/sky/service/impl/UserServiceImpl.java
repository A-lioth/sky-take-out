package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.constant.WXLoginConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO 微信登录信息
     * @return 登录结果
     */
    public User login(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        // * 判断openid是否为空
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // * 根据openid查询数据库
        User user = userMapper.getByOpenid(openid);
        // * 如果用户不存在，则注册用户
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    /**
     * 获取微信登录的openid
     *
     * @param code 微信登录信息code
     * @return openid
     */
    private String getOpenid(String code) {
        // * 调用微信接口，获取openid
        Map<String, String> map = new HashMap<>();
        map.put(WXLoginConstant.APP_ID, weChatProperties.getAppid());
        map.put(WXLoginConstant.SECRET, weChatProperties.getSecret());
        map.put(WXLoginConstant.JS_CODE, code);
        map.put(WXLoginConstant.GRANT_TYPE, WXLoginConstant.GRANT_TYPE);
        String json = HttpClientUtil.doGet(WXLoginConstant.WX_LOGIN_URL, map);
        // * 解析获取到的json数据
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString(WXLoginConstant.OPEN_ID);
    }
}
