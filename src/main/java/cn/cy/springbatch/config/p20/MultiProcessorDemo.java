package cn.cy.springbatch.config.p20;

import cn.cy.springbatch.config.p19.ItemProcessorDemoWriter;
import cn.cy.springbatch.config.p19.NameUpperCaseProcessor;
import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiProcessorDemo  {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Resource
    private ItemProcessorDemoWriter itemProcessorDemoWriter;
    @Resource
    private FirstNameUpperProcessor firstNameUpperProcessor;
    @Resource
    private IdFilterProcessor idFilterProcessor;

    @Bean
    public Job multiProcessorJob(){
        return jobBuilderFactory.get("multiProcessorJob3")
                .start(multiProsserStep())
                .build();
    }

    @Bean
    public Step multiProsserStep(){
        return stepBuilderFactory.get("multiProsserStep")
                .<PersonBean,PersonBean>chunk(10)
                .reader(itemProcessorDemoReader5())
                .processor(processor())
                .writer(itemProcessorDemoWriter)
                .build();
    }


    @Bean
    @StepScope
    public JdbcPagingItemReader<PersonBean> itemProcessorDemoReader5(){
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

    /**
     * 多个Processor的组合处理
     * @return
     */
    @Bean
    public CompositeItemProcessor<PersonBean,PersonBean> processor(){
        //创建一个CompositeItemProcessor对象
        CompositeItemProcessor<PersonBean, PersonBean> processor = new CompositeItemProcessor<>();
        //多个Processor的List
        ArrayList<ItemProcessor<PersonBean, PersonBean>> list = new ArrayList<>();
        list.add(firstNameUpperProcessor);
        list.add(idFilterProcessor);
        processor.setDelegates(list);
        return processor;
    }



}












