package cn.cy.springbatch.config.p10;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 创建一个ItemWriter对象
 * 自定义如何处理读取到的数据
 */
@Component
public class DbJdbcWriter implements ItemWriter<User> {

    @Override
    public void write(List<? extends User> items) throws Exception {
        //将读取到的数据直接输出
        items.forEach(System.out::println);
    }
}
