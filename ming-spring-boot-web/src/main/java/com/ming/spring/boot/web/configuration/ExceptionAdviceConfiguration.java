package com.ming.spring.boot.web.configuration;

import com.ming.common.model.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdviceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdviceConfiguration.class);

    @ExceptionHandler(Throwable.class)
    public Result exception(Throwable t) {
        LOGGER.error("web process exception", t);
        return Result.failure(ExceptionUtils.getMessage(t));
    }
}
