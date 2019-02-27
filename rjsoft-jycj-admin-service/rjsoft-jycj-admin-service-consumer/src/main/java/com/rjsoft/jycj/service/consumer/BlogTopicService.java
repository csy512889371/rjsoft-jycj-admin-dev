package com.rjsoft.jycj.service.consumer;

import com.rjsoft.uums.service.provider.spi.BlogTopicSpi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "rjsoft-uums-main", url = "http://127.0.0.1:8080")
public interface BlogTopicService extends BlogTopicSpi {
}
