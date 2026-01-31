package com.cmt.meituanagent.service;

import com.cmt.meituanagent.model.dto.user.UserQueryRequest;
import com.cmt.meituanagent.model.vo.LoginUserVO;
import com.cmt.meituanagent.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.cmt.meituanagent.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author 001
 */
public interface UserService extends IService<User> {
    long userRegister(String username, String userPassword, String checkPassword);

    LoginUserVO getLoginUserVO(User user);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    // 获取当前登录用户
    User getLoginUser(HttpServletRequest request);

    // 用户注销
    boolean userLogout(HttpServletRequest request);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    String getEncryptPassword(String userPassword);
}
