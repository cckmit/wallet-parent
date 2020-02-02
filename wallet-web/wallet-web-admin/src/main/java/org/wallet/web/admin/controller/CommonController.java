package org.wallet.web.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wallet.common.constants.UploadConstants;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.utils.FileUtil;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.BaseController;

import java.io.IOException;

/**
 * 登录
 * 
 * @author zengfucheng
 */
@RestController
@RequestMapping("common")
public class CommonController extends BaseController {

    @Autowired
    private Cache cache;

    @Reference(group = DubboServiceGroup.CLIENT_AWS_S3, timeout = 30000)
    private IService awsS3Service;

	@PostMapping("upload")
	public SimpleResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ServiceRequest request = createRequest("upload", UserUtil.getUserId());
        request.setServiceId(DubboServiceGroup.CLIENT_AWS_S3);

        String path = UploadConstants.UPLOAD_PATH + FileUtil.createFilePathByDate();
        String fileSuffix = FileUtil.getFileSuffix(file.getOriginalFilename());

        if(StringUtils.isEmpty(fileSuffix)){
            return Results.paramInvalid("上传的文件必须包含文件后缀");
        }

        String fileName = FileUtil.createFileNameByTime(fileSuffix);

        request.setParamValue("path", path);
        request.setParamValue("fileName", fileName);
        request.setParamValue("bytes", file.getBytes());

        ServiceResponse response = awsS3Service.invoke(request);

	    return Results.success(response.getResult());
	}
	
}