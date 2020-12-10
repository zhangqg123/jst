package org.jeecg.modules.dbserver.mongo.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecg.modules.dbserver.mongo.common.enums.Gender;
import org.jeecg.modules.dbserver.mongo.common.model.Alarm;
import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.jeecg.modules.dbserver.mongo.repository.impl.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  UserController.java
 *
 *  @author ：machengxin
 *  @since  ：6/11/2019-3:46 PM
 */
@RestController
@RequestMapping("/work/mongo")
public class UserController {
    @Autowired
    DemoRepository repository;

    @GetMapping(value = "/hello")
    public void hello() {
        User user = new User();
        user.setId("1");
        user.setName("Make1");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.save(user);    	
    }
    
    @GetMapping(value = "/save")
    public void saveTest(){
        User user = new User();
        user.setId("1");
        user.setName("Make2");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.save(user);
    }

    @GetMapping(value = "/insert")
    public void insertTest(){
        User user = new User();
        user.setId("2");
        user.setName("Make2");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.insert(user);
    }
    
    @GetMapping(value = "/insertAlarm")
    public void insertAlarm(){
        Alarm alarm = new Alarm();
 //       alarm.setId("2");
        alarm.setDevNo("dev");
        alarm.setTargetNo("targetno");
        alarm.setAlarmValue("alarmValue");
        alarm.setSendTime(new Date());
        repository.insertAlarm(alarm);
    }
    @GetMapping(value = "/insertAll")
    public void insertAllTest(){
        List<User>list = new ArrayList<>();
        for(int i = 1; i < 1000; i++){
            User user = new User();
            user.setName("Make"+i);
            user.setAge(30+i);
            user.setGender(Gender.MALE);
            user.setUDate(new Date());
            list.add(user);
        }
        repository.insertAll(list);
    }

    @GetMapping(value = "/remove")
    public void removeTest(){
        User user = new User();
        user.setId("1");

        repository.remove(user);
    }

    @GetMapping(value = "/remove2")
    public void removeTest2(){
        repository.remove("2");
    }

    @GetMapping(value = "/findAndRemove")
    public void findAndRemove(){
        User user = repository.findAndRemove(33);
        System.out.println(user);
    }

    @GetMapping(value = "/findAndModify")
    public void findAllAndRemove(){
        User user = repository.findAndModify();
        System.out.println(user);
    }
    @GetMapping(value = "/findByTime")
    public void findByTime(){
        List<User> users = repository.findByTime();
        System.out.println("users size:"+users.size());
    }    
}
