package cn.cy.springbatch.config.p18;

import cn.cy.springbatch.domain.PersonBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;

@Configuration
public class MultiFileItemWriterConfig2 {

    @Bean
    public FlatFileItemWriter<PersonBean> multiFileFlatFileWriter11(){
        FlatFileItemWriter<PersonBean> writer = new FlatFileItemWriter<>();

        String path = "D:/persons11.txt";
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
    public FlatFileItemWriter<PersonBean> multiFileFlatFileWriter22(){
        FlatFileItemWriter<PersonBean> writer = new FlatFileItemWriter<>();

        String path = "D:/persons22.txt";
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

    /**
     * 分组写出
     * @return
     */
    @Bean
    public ClassifierCompositeItemWriter<PersonBean> classifierCompositeItemWriter(){
        ClassifierCompositeItemWriter<PersonBean> writer = new ClassifierCompositeItemWriter<>();
        /**
         * 根据不同的条件 写出到相应的文件
         */
        writer.setClassifier(new Classifier<PersonBean, ItemWriter<? super PersonBean>>() {
            @Override
            public ItemWriter<? super PersonBean> classify(PersonBean personBean) {
                return personBean.getId()%2==0?multiFileFlatFileWriter11():multiFileFlatFileWriter22();
            }
        });

        return writer;
    }





}













