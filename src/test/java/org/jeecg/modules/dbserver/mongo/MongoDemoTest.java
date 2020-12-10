package org.jeecg.modules.dbserver.mongo;


import org.jeecg.modules.dbserver.mongo.common.enums.Gender;
import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.jeecg.modules.dbserver.mongo.repository.impl.DemoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MongoDemoTest extends BaseTest {

    @Autowired
    DemoRepository repository;

    @Test
    public void saveTest(){
        User user = new User();
        user.setId("1");
        user.setName("Make2");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.save(user);
    }

    @Test
    public void insertTest(){
        User user = new User();
        user.setId("2");
        user.setName("Make2");
        user.setAge(22);
        user.setGender(Gender.MALE);

        repository.insert(user);
    }

    @Test
    public void insertAllTest(){
        List<User>list = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            User user = new User();
            user.setName("Make33");
            user.setAge(33);
            user.setGender(Gender.MALE);
            list.add(user);
        }
        repository.insertAll(list);
    }


    @Test
    public void removeTest(){
        User user = new User();
        user.setId("1");

        repository.remove(user);
    }

    @Test
    public void removeTest2(){
        repository.remove("1");
    }


    @Test
    public void findAndRemove(){
        User user = repository.findAndRemove(33);
        System.out.println(user);
    }

    @Test
    public void findAllAndRemove(){
        User user = repository.findAndModify();
        System.out.println(user);
    }


}
