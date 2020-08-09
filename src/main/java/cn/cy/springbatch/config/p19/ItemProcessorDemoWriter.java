package cn.cy.springbatch.config.p19;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemProcessorDemoWriter implements ItemWriter<PersonBean> {

    @Override
    public void write(List<? extends PersonBean> items) throws Exception {

        for (PersonBean personBean : items) {
            System.out.println(personBean);
        }

    }
}
