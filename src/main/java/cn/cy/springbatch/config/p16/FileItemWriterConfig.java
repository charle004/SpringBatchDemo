package cn.cy.springbatch.config.p16;

import cn.cy.springbatch.domain.PersonBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * 使用 FlatFileItemWriter对象将数据写入文件
 */
@Configuration
public class FileItemWriterConfig {

    @Bean
    public FlatFileItemWriter<PersonBean> flatFileItemWriter(){

        FlatFileItemWriter<PersonBean> writer = new FlatFileItemWriter<>();
        String path = "C:\\Users\\93700\\Desktop\\personsFromDB.txt";
        //指定写入的文件
        writer.setResource(new FileSystemResource(path));

        //设置写入的行聚合器
        writer.setLineAggregator(new LineAggregator<PersonBean>() {
            /**
             * 将PersonBean对象转换成一行Json字符数据
             * @param item PersonBean对象
             * @return str PersonBean对象转换而来的一行数据
             */
            @Override
            public String aggregate(PersonBean item) {
                String str = "";
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    str = mapper.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return str;
            }
        });

        return writer;
    }

}
