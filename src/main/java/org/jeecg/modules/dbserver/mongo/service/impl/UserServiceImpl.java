package org.jeecg.modules.dbserver.mongo.service.impl;

import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.jeecg.modules.dbserver.mongo.repository.UserRepository;
import org.jeecg.modules.dbserver.mongo.service.UserService;
import org.springframework.stereotype.Service;

/**
 *  UserServiceImpl.java
 *
 *  @author ：Mc_GuYi
 *  @since  ：7/8/2019
 */
@Service
public class UserServiceImpl extends BaseMongoServiceImpl<UserRepository, User, String> implements UserService {

}
