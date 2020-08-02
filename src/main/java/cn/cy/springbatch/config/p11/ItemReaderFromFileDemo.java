package cn.cy.springbatch.config.p11;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
public class ItemReaderFromFileDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    private FlatFileWriter flatFileWriter;

    @Bean
    public Job readerFromFileJob(){
        return jobBuilderFactory.get("readerFromFileJob")
                .start(readerFromFileStep())
                .build();
    }

    @Bean
    public Step readerFromFileStep(){
        return stepBuilderFactory.get("readerFromFileStep")
                //指定读取和输出的数据类型
                .<PersonBean,PersonBean>chunk(10)
                .reader(flatReader2())
                .writer(flatFileWriter)
                .build();
    }

    /**
     * 使用 FlatFileItemReader 对象读取文件
     * @return reader
     */
    @Bean
    @StepScope
    public FlatFileItemReader<PersonBean> flatReader2(){
        FlatFileItemReader<PersonBean> reader = new FlatFileItemReader<>();
        //指定Reader的数据源
        reader.setResource(new ClassPathResource("persons.txt"));
        //跳过文件中的第1行
        reader.setLinesToSkip(1);
        //解析数据
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
