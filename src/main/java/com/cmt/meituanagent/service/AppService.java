package com.cmt.meituanagent.service;

import com.cmt.meituanagent.model.dto.app.AppQueryRequest;
import com.cmt.meituanagent.model.entity.User;
import com.cmt.meituanagent.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.cmt.meituanagent.model.entity.App;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.List;

/**
 * 应用 服务层。
 *
 * @author 001
 */
public interface AppService extends IService<App> {

    // 获取应用VO
    AppVO getAppVO(App app);

    // 获取应用查询包装器
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    // 获取应用VO列表
    List<AppVO> getAppVOList(List<App> appList);

    // 通过应用id和用户输入的消息，生成代码
    Flux<String> charToGenCode(Long appId, String message, User loginUser);

    // 应用部署
    String deployApp(Long appId, User loginUser);

    // 删除应用时关联删除对话历史
    boolean removeById(Serializable id);
}
