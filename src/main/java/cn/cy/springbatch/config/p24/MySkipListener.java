package cn.cy.springbatch.config.p24;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

/**
 * Skip监听器
 */
@Component
public class MySkipListener implements SkipListener<String,String> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        System.out.println("读取时发生了skip");
    }

    @Override
    public void onSkipInProcess(String s, Throwable throwable) {
        System.out.println("处理时skip的Item:"+s);
    }

    @Override
    public void onSkipInWrite(String s, Throwable throwable) {
        System.out.println("写出时skip的Item:"+s);
    }

}
