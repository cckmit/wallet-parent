package org.wallet.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.wallet.common.entity.TableExtAttrValueEntity;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.common.enums.TableExtAttrTypeEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TableExtAttrValueDTO extends BaseNormalDTO {
    private BusinessDomainEnum domain;
    private TableExtAttrTypeEnum type;
    private String label;
    @NotNull
    private Long attrId;
    private Long dataId;
    private String value;

    public TableExtAttrValueEntity toEntity(){
        TableExtAttrValueEntity entity = new TableExtAttrValueEntity();

        BeanUtils.copyProperties(this, entity);

        return entity;
    }

    public static List<TableExtAttrValueDTO> toList(Map<String, String> valueMap){
        if(CollectionUtils.isEmpty(valueMap)){
            return null;
        }

        List<TableExtAttrValueDTO> valueList = new ArrayList<>();

        valueMap.forEach((name, value) -> {
            TableExtAttrValueDTO dto = new TableExtAttrValueDTO();
            dto.setName(name);
            dto.setValue(value);

            valueList.add(dto);
        });

        return valueList;
    }

    public static Map<String, String> toMap(List<TableExtAttrValueDTO> values) {
        if(CollectionUtils.isEmpty(values)){
            return null;
        }

        Map<String, String> valueMap = new LinkedHashMap<>(values.size());

        values.forEach(value -> valueMap.put(value.getName(), value.getValue()));

        return valueMap;
    }
}
