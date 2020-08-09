package cn.cy.springbatch.config.p20;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstNameUpperProcessor implements ItemProcessor<PersonBean,PersonBean> {
    @Override
    public PersonBean process(PersonBean item) throws Exception {
        String name = item.getName();
        String s = name.substring(0,1).toUpperCase()+name.substring(1);
        item.setName(s);
        return item;
    }
}
