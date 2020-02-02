package org.wallet.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Data
public class SortDTO implements Serializable {
    private static final SortDTO UNSORTED = SortDTO.by();
    private List<OrderDTO> orders;

    public SortDTO() {
    }

    private SortDTO(List<OrderDTO> orders) {
        this.orders = Collections.unmodifiableList(orders);
    }

    private SortDTO(OrderDTO... orders) {
        this(Arrays.asList(orders));
    }

    public static SortDTO by(List<OrderDTO> orders) {
        return orders.isEmpty() ? SortDTO.unsorted() : new SortDTO(orders);
    }

    public static SortDTO by(OrderDTO... orders) {
        return new SortDTO(orders);
    }

    public static SortDTO by(Direction direction, String... properties) {
        return SortDTO.by(Arrays.stream(properties)
                .map(it -> new OrderDTO(direction, it))
                .collect(Collectors.toList()));
    }

    public static SortDTO asc(String... properties){
        return SortDTO.by(Direction.ASC, properties);
    }

    public static SortDTO desc(String... properties){
        return SortDTO.by(Direction.DESC, properties);
    }

    public static SortDTO unsorted() {
        return UNSORTED;
    }

    @Data
    public static class OrderDTO implements Serializable {
        static final Direction DEFAULT_DIRECTION = Direction.ASC;

        private Direction direction;
        private String property;

        public OrderDTO() {
        }

        public OrderDTO(String property) {
            this(DEFAULT_DIRECTION, property);
        }

        public OrderDTO(Direction direction, String property) {
            this.direction = direction == null ? DEFAULT_DIRECTION : direction;
            this.property = property;
        }

        public OrderDTO(String direction, String property) {
            this.direction = direction == null ? DEFAULT_DIRECTION : Direction.valueOf(direction);
            this.property = property;
        }

        public static OrderDTO by(String property) {
            return new OrderDTO(DEFAULT_DIRECTION, property);
        }

        public static OrderDTO asc(String property) {
            return new OrderDTO(Direction.DESC, property);
        }

        public static OrderDTO desc(String property) {
            return new OrderDTO(Direction.DESC, property);
        }

    }

    public enum Direction {
        /** 排序方式 - 升序 */
        ASC,
        /** 排序方式 - 降序 */
        DESC
    }
}
