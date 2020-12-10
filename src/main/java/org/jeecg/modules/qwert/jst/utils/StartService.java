package org.jeecg.modules.qwert.jst.utils;

import org.jeecg.modules.qwert.jst.service.IJstZcDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 继承Application接口后项目启动时会按照执行顺序执行run方法
 * 通过设置Order的value来指定执行的顺序
 */
@Component
@Order(value = 1)
public class StartService implements ApplicationRunner {
	@Autowired
	private IJstZcDevService jstZcDevService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("auto start handle read at :"+new Date());
 //       JstConstant.runflag=true;
 //       String hr = jstZcDevService.handleRead("all");
    }


}