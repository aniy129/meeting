package com.meeting.meeting.controller.api.common;

import com.meeting.meeting.model.dto.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@Api(tags = "上传控制器")
@RequestMapping("/common")
public class UploadController {

    @PostMapping("/upload")
    @ApiOperation("上传")
    public BaseResponse<String> upload(MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getSize() < 1) {
            return BaseResponse.failure("文件不存在");
        }
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/upload/");
        if (!upload.exists()) {
            upload.mkdirs();
        }
        String fileName = String.format("%s.%s", UUID.randomUUID().toString(), Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        file.transferTo(new File(upload, fileName));
        return BaseResponse.success("上传成功", "/upload/" + fileName);
    }
}
