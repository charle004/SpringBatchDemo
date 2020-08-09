package cn.cy.springbatch.config.p20;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class IdFilterProcessor implements ItemProcessor<PersonBean,PersonBean> {

    @Override
    public PersonBean process(PersonBean item) throws Exception {
        if(item.getId()%2==0){
            return item;
        }else
            return null;
    }
}
