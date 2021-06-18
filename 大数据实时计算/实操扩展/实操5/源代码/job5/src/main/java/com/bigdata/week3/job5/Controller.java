package com.bigdata.week3.job5;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job5")
public class Controller {
    @RequestMapping("/run")
    public void run(){
        System.out.println("run");
        job5.run();
    }
    @RequestMapping("/stop")
    public void stop() throws InterruptedException {
        System.out.println("stop");
    }
    @RequestMapping("/change")
    public void change(@RequestParam("user")String user) throws InterruptedException {
        System.out.println("change:"+user);
        job5.changeUser(user);
    }
}
