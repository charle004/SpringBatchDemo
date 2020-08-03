package cn.cy.springbatch.config.p15;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

import javax.annotation.Resource;

@Configuration
public class ItemWriterDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private JdbcBatchItemWriter jdbcBatchItemWriter;

    @Bean
    public Job ItemWriterDbDemoJob(){
        return jobBuilderFactory.get("ItemWriterDbDemoJob")
                .start(itemWriterDbDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDbDemoStep(){
        return stepBuilderFactory.get("ItemWriterDbDemoStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(flatFileItemReader2())
                .writer(jdbcBatchItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader flatFileItemReader2(){
        FlatFileItemReader<PersonBean> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("persons3.txt"));

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames("id","name","birthday");

        DefaultLineMapper<PersonBean> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<PersonBean>() {
            @Override
            public PersonBean mapFieldSet(FieldSet fieldSet) throws BindException {
                PersonBean personBean = new PersonBean();
                personBean.setId(fieldSet.readInt("id"));
                personBean.setName(fieldSet.readString("name"));
                personBean.setBirthday(fieldSet.readString("birthday"));
                return personBean;
            }
        });
        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
        return reader;
    }



}
