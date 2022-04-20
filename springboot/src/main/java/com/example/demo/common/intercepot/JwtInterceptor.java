package com.example.demo.common.intercepot;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.demo.entity.Dormitory;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.exceptionHandler.ServiceException;
import com.example.demo.mapper.DormitoryMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DormitoryMapper dormitoryMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");

        System.out.println(request.getRequestURI());
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //执行认证
        if (StrUtil.isBlank(token)){
            throw  new ServiceException("401", "无token，请重新登录");
        }
        String userId = new String();
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j){
            throw new ServiceException("401","token验证失败，请重新登录");
        }
        //获取token中的user id
        User user = userMapper.selectById(userId);
        Dormitory dormitory = dormitoryMapper.selectById(userId);
        Student student = studentMapper.selectById(userId);
        if (user == null && dormitory == null && student ==null){
            throw new ServiceException("401","用户不存在，请重新登录");
        }
        //用户密码加签验证 token
        try {
            JWTVerifier jwtVerifier;
            if (user != null) {
                jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            }else if (dormitory != null){
                jwtVerifier = JWT.require(Algorithm.HMAC256(dormitory.getPassword())).build();
            }else {
                jwtVerifier = JWT.require(Algorithm.HMAC256(student.getPassword())).build();
            }
            jwtVerifier.verify(token); //验证token
        } catch (JWTDecodeException e){
            throw new ServiceException("401","token验证失败，请重新登录");
        } catch (SignatureVerificationException s) {
            throw new ServiceException("401","密码已失效，请重新登录");
        }
        return true;
    }
}
