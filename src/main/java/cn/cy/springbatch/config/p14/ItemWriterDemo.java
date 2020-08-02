package cn.cy.springbatch.config.p14;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemWriterDemo implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        //数据的输出处理

    }
}
