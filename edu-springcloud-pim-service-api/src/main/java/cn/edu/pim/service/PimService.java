package cn.edu.pim.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.pim.service.hystrix.PimServiceHystrix;
import cn.edu.pojo.EduResult;

/**
 *
 */
@FeignClient(value = "EDU-PIM-SERVICE",fallback = PimServiceHystrix.class)
public interface PimService {

    /**
     * 请求格式 POST
     * 测试
     *
     * @param String param
     * @return {
     *           status: 200 //200 成功 400 登录失败 500 系统异常
     *           msg: "OK" //错误 用户名或密码错误,请检查后重试.
     *           data: "fe5cb546aeb3ce1bf37abcb08a40493e" //登录成功，返回token
     *         }
     */
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String test(String param);
}
