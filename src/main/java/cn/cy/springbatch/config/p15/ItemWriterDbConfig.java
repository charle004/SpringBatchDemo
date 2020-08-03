package cn.cy.springbatch.config.p15;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 使用 JdbcBatchItemWriter 对象 进行数据库的写入
 */
@Component
public class ItemWriterDbConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<PersonBean> jdbcBatchItemWriter(){
        JdbcBatchItemWriter<PersonBean> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource);
        //sql 并设好占位符
        writer.setSql("insert into person(id,name,birthday) values " +
                "(:id,:name,:birthday)");
        //参数属性与对象的映射
        BeanPropertyItemSqlParameterSourceProvider<PersonBean> parameterSourceProvider = new BeanPropertyItemSqlParameterSourceProvider<>();
        writer.setItemSqlParameterSourceProvider(parameterSourceProvider);

        return writer;
    }



}
