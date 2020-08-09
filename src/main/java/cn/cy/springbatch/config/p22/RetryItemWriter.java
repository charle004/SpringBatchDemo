package cn.cy.springbatch.config.p22;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RetryItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            System.out.println(item);
        }
    }
}
