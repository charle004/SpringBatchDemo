package cn.cy.springbatch.config.p10;

import cn.cy.springbatch.domain.User;
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
 * ItemReader 以数据库作数据源
 */
@Configuration
public class itemReaderFromDBDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //注入数据源
    @Autowired
    private DataSource dataSource;

    //注入ItemWriter对象
    @Resource
    private DbJdbcWriter jdbcWriter;

    @Bean
    public Job readerFromDBJob(){
        return jobBuilderFactory.get("readerFromDBJob")
                .start(readerFromDBStep())
                .build();
    }

    @Bean
    public Step readerFromDBStep(){
        return stepBuilderFactory.get("readerFromDBStep")
                .<User,User>chunk(2)
                //指定读取的ItemReaer对象
                .reader(dbJdbcReader())
                //输出读取到的数据对象
                .writer(jdbcWriter)
                .build();
    }

    @Bean
    @StepScope //指定该ItemReader的范围是Step
    public JdbcPagingItemReader<User> dbJdbcReader(){
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
        //设置数据源
        reader.setDataSource(dataSource);
        //设置分页
        reader.setFetchSize(2);

        //把读取到的数据转换成User对象
        reader.setRowMapper(new RowMapper<User>() {
            //将每一条记录映射成User对象
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setAge(rs.getInt(4));
                return user;
            }
        });

        //指定sql
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
            //指定查询的字段
        queryProvider.setSelectClause("id,username,password,age");
            //指定查询的表
        queryProvider.setFromClause("user");
            //指定查询的排序方式
        Map<String, Order> orderMap = new HashMap<>(1);
            //按哪一列怎么排序
        orderMap.put("id",Order.ASCENDING);
        queryProvider.setSortKeys(orderMap);

        //查询器set给Readerd对象
        reader.setQueryProvider(queryProvider);

        return reader;
    }


}
