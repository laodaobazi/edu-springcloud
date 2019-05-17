package cn.edu.pim.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.pim.service.PimService;
import cn.edu.pojo.EduResult;
import cn.edu.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 通知相关服务 Service 实现
 */
@Api(value = "API - PimServiceImpl", description = "通知 服务")
@RestController
@RefreshScope
public class PimServiceImpl implements PimService {

    private static final Logger logger = LoggerFactory.getLogger(PimServiceImpl.class);
    public static final String KEY = "success";
    public static final int ERROR = 1;
    public static final int SUCCESS = 0;

    @Autowired
    private UserService userService;
    
    @Override
    @ApiOperation("测试")
    @ApiImplicitParams({ @ApiImplicitParam(name = "param", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
	public String test(String param) {
		logger.debug("==================");
		String ret = userService.token("11111", "AAAAA");
		return ret;
	}
    
}
