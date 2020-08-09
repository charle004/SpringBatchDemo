package cn.cy.springbatch.config.p19;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 创建实现了ItemProcessor接口的 处理类
 * 完成姓名的大写转化
 */
@Component
public class NameUpperCaseProcessor implements ItemProcessor<PersonBean,PersonBean> {

    @Override
    public PersonBean process(PersonBean item) throws Exception {
        PersonBean personBean = new PersonBean();
        personBean.setId(item.getId());
        personBean.setName(item.getName().toUpperCase());
        personBean.setBirthday(item.getBirthday());
        return personBean;
    }
}
