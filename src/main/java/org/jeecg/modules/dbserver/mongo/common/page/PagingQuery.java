package org.jeecg.modules.dbserver.mongo.common.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import org.jeecg.modules.dbserver.mongo.common.utils.ToStringUtils;

@Getter
@Setter
public class PagingQuery implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_PAGE = 1;

    public static final int DEFAULT_SIZE = 10;

    /**
     * 当前页，默认第1页
     */
    private int currentPage = DEFAULT_PAGE;

    /**
     * 每页条数，默认10条
     */
    private int pageSize = DEFAULT_SIZE;

    /**
     * 是否需要分页，默认需要
     */
    private boolean pageable = true;

    /**
     * 导出功能使用，确定要导出的样板定义
     */
    private ExportSampler sampler;

    @Override
    public final String toString() {
        return ToStringUtils.toString(this);
    }


    /**
     * 导出样例
     * @author dengqianyong@besttone.com.cn
     *
     */
    public enum ExportSampler  {

        /**
         * 待填写台账
         */
        UNFILLED_LEDGER,

        /**
         * 有效台账
         */
        AVAILABLE_LEDGER,

        /**
         * 无效台账
         */
        UNAVAILABLE_LEDGER,

        /**
         * 通话记录
         */
        CTI_CALLING,
        ;

    }

}
