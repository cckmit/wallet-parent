package org.wallet.common.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallet.common.constants.field.EntityField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
public abstract class BaseDTO implements Serializable {
    private Long id;
    private Long creator;
    private Date createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SearchProperty(value = SearchOperator.gte, name = EntityField.CREATE_DATE)
    private Date createDateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @SearchProperty(value = SearchOperator.lte, name = EntityField.CREATE_DATE)
    private Date createDateEnd;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public void removeAdminAttr(){
        setCreator(null);
        setCreateDate(null);
    }
}
