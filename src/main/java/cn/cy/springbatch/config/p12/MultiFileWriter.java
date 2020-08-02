package cn.cy.springbatch.config.p12;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiFileWriter implements ItemWriter<PersonBean> {
    @Override
    public void write(List<? extends PersonBean> items) throws Exception {
        items.forEach(System.out::println);
    }
}
