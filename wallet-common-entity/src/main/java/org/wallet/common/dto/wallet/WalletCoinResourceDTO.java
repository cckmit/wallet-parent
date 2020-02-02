package org.wallet.common.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletCoinResourceDTO implements Serializable {
    @NotEmpty
    private String name;
    @NotEmpty
    private String label;
    @NotEmpty
    private String value;
    @NotNull
    private Integer sort;

    public static List<WalletCoinResourceDTO> getDefault(){
        List<WalletCoinResourceDTO> list = new ArrayList<>();

        list.add(new WalletCoinResourceDTO("ram", "RAM", "4.0 KB", 10));
        list.add(new WalletCoinResourceDTO("cpu", "CPU", "0.5 EOS", 20));
        list.add(new WalletCoinResourceDTO("net", "NET", "0.01 EOS", 30));

        return list;
    }
}
