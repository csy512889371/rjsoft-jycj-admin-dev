package com.rjsoft.jycj.admin.web.exception.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjsoft.magina.spi.common.dto.BaseResponse;
import com.rjsoft.magina.spi.common.dto.ResponseStatusEnum;
import com.rjsoft.magina.spi.common.exception.BusinessException;
import com.rjsoft.magina.web.common.exception.base.ArgumentInvalidResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    ObjectMapper objectMapper;
    private Environment env;


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public Object handleRequestMethodException(HttpServletRequest request,
                                               HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        baseResponse.setMessage(exception.getMessage());
        baseResponse.setData("HttpRequestMethodNotSupported Exception");
        return baseResponse;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object methodArgumentNotValidHandler(MethodArgumentNotValidException exception) throws JsonProcessingException {
        List<ArgumentInvalidResult> argumentInvalidResults = new ArrayList<ArgumentInvalidResult>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult argumentInvalidResult = new ArgumentInvalidResult();
            argumentInvalidResult.setField(error.getField());
            argumentInvalidResult.setRejectedValue(error.getRejectedValue());
            argumentInvalidResult.setDefaultMessage(error.getDefaultMessage());
            argumentInvalidResults.add(argumentInvalidResult);
        }
        log.error("                方法参数无效错误: message:{}, parameter: {}, result:{}",
                exception.getMessage(),
                objectMapper.writeValueAsString(exception.getParameter()),
                objectMapper.writeValueAsString(exception.getBindingResult())
        );
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseStatusEnum.PARAMETER_ERROR.getCode());
        baseResponse.setMessage(env.getProperty(ResponseStatusEnum.PARAMETER_ERROR.getCode(), ResponseStatusEnum.PARAMETER_ERROR.getMessage()));
        baseResponse.setData(argumentInvalidResults);
        return baseResponse;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String businessExceptionHandler(HttpServletRequest request, BusinessException exception) {
        log.error(exception.getMessage(), exception);

        request.setAttribute("error", exception.getDefaultMessage());
        return "comm/error_500";
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleSQLException(HttpServletRequest request,
                                     SQLException exception) {
        log.error(exception.getMessage(), exception);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        baseResponse.setMessage(exception.getMessage());
        baseResponse.setData("SQLQuery Error");
        return baseResponse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleConstraintViolationException(HttpServletRequest request,
                                                     ConstraintViolationException exception) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuffer errorMsg = new StringBuffer();
        for (ConstraintViolation<?> item : violations) {
            errorMsg.append(item.getMessage());
        }
        baseResponse.setMessage(errorMsg.toString());

        log.error(errorMsg.toString());
        baseResponse.setData("参数校验异常 Error");
        return baseResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleAllException(HttpServletRequest request,
                                     RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        baseResponse.setMessage(exception.getMessage());
        baseResponse.setData("Runtime Error");
        return baseResponse;

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllException(Exception exception, Model model) {

        log.error(exception.getMessage(), exception);

        model.addAttribute("error", exception.getMessage());
        return "comm/error_500";
    }


}
