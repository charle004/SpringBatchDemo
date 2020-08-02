package cn.cy.springbatch.config.p11;

import cn.cy.springbatch.tools.PersonBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件输出操作
 */
@Component
public class FlatFileWriter implements ItemWriter<PersonBean> {

    @Override
    public void write(List<? extends PersonBean> items) throws Exception {
        for (PersonBean personBean : items) {
            System.out.println(personBean);
        }
    }
}
