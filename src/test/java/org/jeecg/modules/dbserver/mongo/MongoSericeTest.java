package org.jeecg.modules.dbserver.mongo;

import org.jeecg.modules.dbserver.mongo.common.enums.Gender;
import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.jeecg.modules.dbserver.mongo.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoSericeTest extends BaseTest {

    @Autowired
    UserService service;

    @Test
    public void saveTest(){
        User user = new User();
        user.setId("1");
        user.setName("aaa");
        user.setAge(23);
        user.setGender(Gender.MALE);

        service.save(user);
    }

    @Test
    public void deleteTest(){
        service.delete("1");
    }

    @Test
    public void updateTest(){
        User user = new User();
        user.setId("1");
        user.setName("nnn");
        user.setGender(Gender.FEMALE);

        //默认更新全部字段
        service.update(user);
    }

}
