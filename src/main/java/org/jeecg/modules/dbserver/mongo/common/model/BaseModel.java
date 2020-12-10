package org.jeecg.modules.dbserver.mongo.common.model;


import java.io.Serializable;

import org.jeecg.modules.dbserver.mongo.common.utils.ToStringUtils;

public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public final String toString() {
        return ToStringUtils.toString(this);
    }

}
