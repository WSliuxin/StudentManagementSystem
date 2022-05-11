package com.example.demo.runner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Leave;
import com.example.demo.mapper.LeaveMapper;
import com.example.demo.service.ILeaveService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Resource
    ILeaveService leaveService;

    @Resource
    LeaveMapper leaveMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();

    }

    class Daemon implements Runnable{

        @Override
        public void run() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Date date = new Date();
                    List<Leave> list = leaveService.list();
                    for (Leave leave : list) {
                        Date endTime = leave.getEndTime();
                        if (endTime.getTime()<date.getTime()&&leave.getState()==1){
                            leave.setState(2);
                            leaveService.updateById(leave);
                        }
                    }
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task,1000,1000*60*5);
        }
    }
}
