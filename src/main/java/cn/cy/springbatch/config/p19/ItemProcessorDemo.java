package cn.cy.springbatch.config.p19;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
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

/**
 * 使用ItemProcessor 处理数据
 */
@Configuration
public class ItemProcessorDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Resource
    private ItemProcessorDemoWriter itemProcessorDemoWriter;
    @Resource
    private NameUpperCaseProcessor nameUpperCaseProcessor;

    @Bean
    public Job itemProcessorJob(){
        return jobBuilderFactory.get("ItemProcessorJob")
                .start(itemProcessorStep())
                .build();
    }

    @Bean
    public Step itemProcessorStep(){
        return stepBuilderFactory.get("ItemProcessorStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(itemProcessorDemoReader())
                /*
                 * 在读取之后 写入之前进行数据的相关处理
                 */
                .processor(nameUpperCaseProcessor)
                .writer(itemProcessorDemoWriter)
                .build();
    }


    @Bean
    @StepScope
    public JdbcPagingItemReader<PersonBean> itemProcessorDemoReader(){
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


























