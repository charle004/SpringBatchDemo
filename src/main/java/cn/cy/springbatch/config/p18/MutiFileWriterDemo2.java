package cn.cy.springbatch.config.p18;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MutiFileWriterDemo2 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Resource
    private ClassifierCompositeItemWriter<PersonBean> classifierCompositeItemWriter;
    @Resource
    private FlatFileItemWriter<PersonBean> multiFileFlatFileWriter11;
    @Resource
    private FlatFileItemWriter<PersonBean> multiFileFlatFileWriter22;


    @Bean
    public Job multiFileWriterDemoJob2(){
        return jobBuilderFactory.get("multiFileWriterDemoJob2")
                .start(multiFileWriterDemoStep2())
                .build();
    }

    @Bean
    public Step multiFileWriterDemoStep2(){
        return stepBuilderFactory.get("multiFileWriterDemoStep2")
                .<PersonBean,PersonBean>chunk(20)
                .reader(multiFileItemReader2())
                .writer(classifierCompositeItemWriter)
                /**
                 * 指定两个ItemWriter
                 */
                .stream(multiFileFlatFileWriter11)
                .stream(multiFileFlatFileWriter22)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<PersonBean> multiFileItemReader2(){
        JdbcPagingItemReader<PersonBean> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(20);

        reader.setRowMapper(new RowMapper<PersonBean>() {
            @Override
            public PersonBean mapRow(ResultSet resultSet, int i) throws SQLException {
                PersonBean personBean = new PersonBean();
                personBean.setId(resultSet.getInt(1));
                personBean.setName(resultSet.getString(2));
                personBean.setBirthday(resultSet.getString(3));
                return personBean;
            }
        });
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id,name,birthday");
        queryProvider.setFromClause("person");
        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put("id",Order.ASCENDING);
        queryProvider.setSortKeys(orderMap);
        reader.setQueryProvider(queryProvider);
        return reader;
    }



}











