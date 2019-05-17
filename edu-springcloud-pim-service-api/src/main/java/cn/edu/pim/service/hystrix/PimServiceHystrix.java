package cn.edu.pim.service.hystrix;

import org.springframework.stereotype.Component;

import cn.edu.pim.service.PimService;
import cn.edu.pojo.EduResult;

/**
 *  熔断处理
 *
 */
@Component
public class PimServiceHystrix implements PimService {

    /**
     * 请求格式 POST
     * 用户登录
     *
     * @param String param
     * @return {
     * status: 200 //200 成功 400 登录失败 500 系统异常
     * msg: "OK" //错误 用户名或密码错误,请检查后重试.
     * data: "fe5cb546aeb3ce1bf37abcb08a40493e" //登录成功，返回token
     * }
     */
    @Override
    public String test(String param) {
        return "11111111111111";
    }
    
}
