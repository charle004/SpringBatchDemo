package cn.cy.springbatch.config.p13;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemStreamReaderWriter implements ItemWriter<PersonBean> {

    @Override
    public void write(List<? extends PersonBean> list) throws Exception {
        list.forEach(System.out::println);
    }
}
