package cn.cy.springbatch.config.p16;

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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
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
public class FileItemWriterDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Resource
    private FlatFileItemWriter flatFileItemWriter;

    @Bean
    public Job fileItemWriterDemoJob(){
        return jobBuilderFactory.get("fileItemWriterDemoJob")
                .start(fileItemWriterDemoStep())
                .build();
    }

    @Bean
    public Step fileItemWriterDemoStep(){
        return stepBuilderFactory.get("fileItemWriterDemoStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(jbdcreaderFromDb())
                .writer(flatFileItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<PersonBean> jbdcreaderFromDb(){
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
        queryProvider.setFromClause("person");
        queryProvider.setSelectClause("id,name,birthday");
        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(orderMap);
        reader.setQueryProvider(queryProvider);
        return reader;
    }


}
