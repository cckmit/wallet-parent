package org.wallet.web.admin.controller;

import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.common.bind.Results;
import org.wallet.web.common.mvc.controller.BaseController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zengfucheng
 **/
@RestController
@RequestMapping("cache")
public class CacheController extends BaseController {

    @GetMapping
    public SimpleResult getCache(@RequestParam("name") String name, String key) {
        if(StringUtils.isEmpty(key)){
            return Results.success(cache.get(name));
        }else{
            return Results.success(cache.get(name, key));
        }
    }

    @PostMapping
    public SimpleResult addCache(@Valid @RequestBody CacheDTO cacheDTO) {
        String name = cacheDTO.getName();
        String key = cacheDTO.getKey();
        Long expire = cacheDTO.getExpire();
        Object data = cacheDTO.getData();
        cache.put(name, key, data, expire);
        return Results.success();
    }

    @DeleteMapping
    public SimpleResult deleteCache(@RequestParam("name") String name, String key) {
        if(StringUtils.isEmpty(key)){
            cache.evict(name);
            return Results.success();
        }else{
            cache.evict(name, key);
            return Results.success();
        }
    }

    @Data
    public static class CacheDTO{
        @NotEmpty
        private String name;
        @NotEmpty
        private String key;
        @NotNull
        private Object data;
        @NotNull
        private Long expire;
    }
}
