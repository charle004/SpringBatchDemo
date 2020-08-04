package cn.cy.springbatch.config.p17;

import cn.cy.springbatch.domain.PersonBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;

@Configuration
public class MultiFileItemWriterConfig {

    @Bean
    public FlatFileItemWriter<PersonBean> multiFileFlatFileWriter(){
        FlatFileItemWriter<PersonBean> writer = new FlatFileItemWriter<>();

        String path = "D:/persons1.txt";
        writer.setResource(new FileSystemResource(path));
        writer.setLineAggregator(new LineAggregator<PersonBean>() {
            @Override
            public String aggregate(PersonBean personBean) {
                ObjectMapper mapper = new ObjectMapper();
                String str = null;
                try {
                    str = mapper.writeValueAsString(personBean);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return str;
            }
        });
        return writer;
    }

    @Bean
    public FlatFileItemWriter<PersonBean> multiFileFlatFileWriter2(){
        FlatFileItemWriter<PersonBean> writer = new FlatFileItemWriter<>();

        String path = "D:/persons2.txt";
        writer.setResource(new FileSystemResource(path));
        writer.setLineAggregator(new LineAggregator<PersonBean>() {
            @Override
            public String aggregate(PersonBean personBean) {
                ObjectMapper mapper = new ObjectMapper();
                String str = null;
                try {
                    str = mapper.writeValueAsString(personBean);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return str;
            }
        });
        return writer;
    }

    @Bean
    public CompositeItemWriter<PersonBean> compositeItemWriter(){
        CompositeItemWriter<PersonBean> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(multiFileFlatFileWriter(),multiFileFlatFileWriter2()));
        return writer;
    }


}













