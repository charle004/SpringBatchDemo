package cn.cy.springbatch.config.p12;

import cn.cy.springbatch.tools.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;

import javax.annotation.Resource;

@Configuration
public class MultiFileReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private MultiFileWriter multiFileWriter;

    @Value("classpath:/persons*.txt")
    private org.springframework.core.io.Resource[] fileResources;

    @Bean
    public Job multiFileReaderJob(){
        return jobBuilderFactory.get("multiFileReaderJob")
                .start(multiFileReaderStep())
                .build();
    }


    @Bean
    public Step multiFileReaderStep(){
        return stepBuilderFactory.get("multiFileReaderStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(multiResourceItemReader())
                .writer(multiFileWriter)
                .build();
    }


    @Bean
    @StepScope
    public MultiResourceItemReader<PersonBean> multiResourceItemReader(){
        MultiResourceItemReader<PersonBean> reader = new MultiResourceItemReader<>();
        //读取一个文件的方式
        reader.setDelegate(flatReader());
        //指定读取哪些文件 按顺序读取
        reader.setResources(fileResources);

        return reader;
    }

    /**
     * 使用 FlatFileItemReader 对象读取文件
     * @return reader
     */
    @Bean
    @StepScope
    public FlatFileItemReader<PersonBean> flatReader(){
        FlatFileItemReader<PersonBean> reader = new FlatFileItemReader<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //每一行数据的分隔符
        tokenizer.setDelimiter(",");
        //每行数据分割后对应的属性
        tokenizer.setNames("id","name","birthday");
        //使用DefaultLineMapper 将读取解析出的一行数据映射为Person对象
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
        //映射失败异常检测
        mapper.afterPropertiesSet();
        //将映射器set给Reader对象
        reader.setLineMapper(mapper);
        return reader;
    }


}
