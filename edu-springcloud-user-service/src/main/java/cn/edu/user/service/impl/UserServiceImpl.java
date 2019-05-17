package cn.edu.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.mapper.user.TbUserMapper;
import cn.edu.pojo.EduResult;
import cn.edu.pojo.user.TbUser;
import cn.edu.user.service.UserService;
import cn.edu.utils.FastJsonConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 用户登录相关服务 Service 实现
 *
 */

@Api(value = "API - UserServiceImpl", description = "user 服务")
@RestController
@RefreshScope
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    public static final String KEY = "success";
    public static final int ERROR = 1;
    public static final int SUCCESS = 0;

    @Autowired
    private TbUserMapper userMapper;

//    @Autowired
//    private JedisClient jedisClient;

    /**
     * 请求格式 POST
     * 用户登录
     *
     * @param user Tbuser POJO Json
     * @return {
     *          status: 200 //200 成功 400 登录失败 500 系统异常
     *          msg: "OK" //错误 用户名或密码错误,请检查后重试.
     *          data: "fe5cb546aeb3ce1bf37abcb08a40493e" //登录成功，返回token
     *         }
     */
    @Override
    @ApiOperation("用户登录")
    @ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "", required = true, dataType = "TbUser"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public EduResult login(@RequestBody TbUser user) {

        if (user == null) {
            return EduResult.build(400, "error", "数据为空");
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("username", user.getUsername());
        List<TbUser> list = userMapper.selectByCondition(condition);

        if (list == null || list.size() == 0) {
            return EduResult.build(400, "用户名不存在");
        }

        TbUser check = list.get(0);

        if (!check.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            return EduResult.build(401, "用户名或密码错误");
        }

        TbUser result = new TbUser();

        result.setUsername(check.getUsername());
        result.setId(check.getId());

        String token = UUID.randomUUID().toString().replaceAll("-", "");

        return EduResult.ok(token);
    }

    /**
     * 请求格式 GET
     * 根据token值获取用户信息
     *
     * @param token    token值
     * @param callback 可选参数 有参表示jsonp调用
     * @return {
     *          status: 200 //200 成功 400 没有此token 500 系统异常
     *          msg: "OK" //错误 没有此token.
     *          data: {"username":"09909","id":"id"} //返回用户名
     *         }
     */
    @Override
    @ApiOperation("获取token")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "callback", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public EduResult token(String token, String callback) {

        if (StringUtils.isNotBlank(callback)) {
            return EduResult.ok(callback); // 未处理
        }

        return EduResult.build(400, "没有此用户");
    }

    /**
     * 请求格式 GET
     * 根据token值 退出登录
     *
     * @param token    token值
     * @param callback 可选参数 有参表示jsonp调用
     * @return {
     *          status: 200 //200 成功 400 没有此token 500 系统异常
     *          msg: "OK" //错误 没有此token.
     *          data: null
     *         }
     */
    @Override
    @ApiOperation("退出登录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "callback", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public EduResult logout(String token, String callback) {

        if (StringUtils.isNotBlank(callback)) {
            return EduResult.ok(callback); // 未处理
        }


        return EduResult.ok();
    }

    /**
     * 请求格式 POST
     * 注册检查是否可用
     *
     * @param isEngaged 需要检查是否使用的名称
     * @return {
     *          "success": 0 可用 1 不可用
     *          "morePin":["sssss740","sssss5601","sssss76676"] //isEngaged = isPinEngaged时返回推荐
     *         }
     */
    @Override
    @ApiOperation("注册检查是否可用")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "isEngaged", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "regName", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "email", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "phone", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public String validateUser(String isEngaged, String regName, String email, String phone) {

        Random random = new Random();

        HashMap<String, Object> map = new HashMap<>();

        logger.error("传递类型出错！");
        map.put("error", ERROR);

        return FastJsonConvert.convertObjectToJSON(map);
    }

    /**
     * 请求格式 POST
     * 验证验证码
     *
     * @param authCode 输入的验证码
     * @param uuid     Redis验证码uuid
     * @return {
     *          "success": 0 可用 1 不可用
     *         }
     */
    @Override
    @ApiOperation("验证验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authCode", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public String validateAuthCode(String authCode, String uuid) {

        HashMap<String, Integer> map = new HashMap<>();


        map.put(KEY, ERROR);

        return FastJsonConvert.convertObjectToJSON(map);
    }

    /**
     * 请求格式 POST
     * 注册
     *
     * @param regName       注册名
     * @param pwd           第一次密码
     * @param pwdRepeat     第二次密码
     * @param phone         电话
     * @param mobileCode    手机验证码
     * @param email         邮箱
     * @param authCode      输入的验证码
     * @param uuid          Redis验证码uuid
     * @return
     */
    @Override
    @ApiOperation("注册")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "regName", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "pwd", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "pwdRepeat", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "phone", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "mobileCode", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "authCode", value = "", required = true, dataType = "String"),
        @ApiImplicitParam(name = "email", value = "", required = true, dataType = "String"), })
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful — 请求已完成"),
        @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
        @ApiResponse(code = 401, message = "未授权客户机访问数据"),
        @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
        @ApiResponse(code = 500, message = "服务器不能完成请求") })
    public String register(String regName, String pwd, String pwdRepeat, String phone,
                           String mobileCode, String uuid, String authCode, String email) {

        if (!pwd.equals(pwdRepeat)) {

            String info = "两次密码不正确";
            return "({'info':'" + info + "'})";

        }

        if (StringUtils.isNotBlank(authCode)) {

            String code = "";

            if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(authCode)) {

                String info = "验证码不正确或已过期，请重新获取";
                return "({'info':'" + info + "'})";

            }

        } else {

            String info = "验证码不能为空";
            return "({'info':'" + info + "'})";

        }

        if (StringUtils.isNotBlank(phone)) {
            String phone2 = phone.substring(5, phone.length());
            String phonecode = "";

            if (StringUtils.isBlank(phonecode) || !phonecode.equals(mobileCode)) {

                String info = "短信验证码不正确或已过期,请重新获取";
                return "({'info':'" + info + "'})";

            }
        } else {
            String info = "手机号码不能为空";
            return "({'info':'" + info + "'})";
        }

        if (StringUtils.isNotBlank(regName)) {

            TbUser user = new TbUser();
            user.setUsername(regName);
            user.setPassword(DigestUtils.md5DigestAsHex(pwd.getBytes()));
            user.setPhone(phone);

            user.setCreated(new Date());
            user.setUpdated(new Date());

            if (StringUtils.isNotBlank(email)) {
                user.setEmail(email);
            }

            userMapper.insert(user);
            //注册成功 忽略noAuth这个词
            return "({'noAuth':'http://aaaaa?username=" + regName + "'})";
        }
        //注册失败
        return "({'error':1})";

    }
}
