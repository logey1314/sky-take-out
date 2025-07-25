package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用controller
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file)  {
        log.info("文件上传{}", file);
        //本地存储
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        String name = UUID.randomUUID().toString() + extension;
        String path="E:\\java\\sky\\houduan\\sky-take-out\\sky-server\\" +
                "src\\main\\resources\\image\\"+name;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            log.info("文件上传失败");
        }


        return Result.success(path);
    }
}
