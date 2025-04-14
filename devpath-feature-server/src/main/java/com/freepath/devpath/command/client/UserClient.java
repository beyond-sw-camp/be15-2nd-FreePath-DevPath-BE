package com.freepath.devpath.command.client;

import com.freepath.devpath.common.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "devpath-community-server", configuration = FeignClientConfig.class)
public interface UserClient {

    // 사용자의 subscribe가 Y인 이메일 List 전달 받음
    @GetMapping("/user/it-news-subscribers")
    List<UserEmail> getSubscribedUsers();
}