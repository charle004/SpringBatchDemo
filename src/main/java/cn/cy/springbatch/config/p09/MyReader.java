package cn.cy.springbatch.config.p09;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import java.util.Iterator;
import java.util.List;

/**
 * 创建一个自定义Reader
 * 自定义如何读取数据
 */
public class MyReader implements ItemReader<String> {

    //集合的迭代器
    private Iterator<String> iterator;

    //构造方法传递集合
    public MyReader(List<String> list) {
        this.iterator = list.iterator();
    }

    //实现读取数据的方法
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //一个数据一个数据的读
        if(iterator.hasNext()){
            return this.iterator.next();
        }
        return null;
    }

}
