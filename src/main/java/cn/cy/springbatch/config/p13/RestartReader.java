package cn.cy.springbatch.config.p13;

import cn.cy.springbatch.domain.PersonBean;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * 创建实现 ItemStreamReader 接口的 ItemReader对象
 * 实现该接口的 4个方法 以 处理读取时的异常
 */
@Component
public class RestartReader implements ItemStreamReader<PersonBean> {

    //读取对象
    private final FlatFileItemReader<PersonBean> flatFileItemReader = new FlatFileItemReader<>();
    //读取文件的行号
    private Long curentLine = 0L;
    //是否重启读取任务
    private Boolean restart = false;
    //执行的上下文环境
    private ExecutionContext executionContext;

    //构造方法指定读取数据的方式
    public RestartReader() {
        //读取文件源
        flatFileItemReader.setResource(new ClassPathResource("persons3.txt"));

        //解析文本文件
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames("id","name","birthday");
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

        mapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(mapper);
    }


    /**
     * 执行读取
     * @return personBean对象
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public PersonBean read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        PersonBean personBean = null;
        //每读一行 行号+1
        this.curentLine++;
        if(restart){
            flatFileItemReader.setLinesToSkip(this.curentLine.intValue()-1);
            restart = false;
            System.out.println("Start Reading from line number: "+this.curentLine);
        }

        //读取数据到PersonBean对象
        flatFileItemReader.open(executionContext);
        PersonBean person = flatFileItemReader.read();

        //制造一个异常
        if(person!=null && person.getName().equals("WrongName")){
            throw new RuntimeException("读取对象发生异常, PersonBean对象： "+person.toString());
        }

        return person;
    }

    /**
     * 该方法在Step执行之前执行
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //获取Step执行的上下文环境
        this.executionContext = executionContext;
        if(executionContext.containsKey("curentLine")){
            this.curentLine = executionContext.getLong("curentLine");
            this.restart = true;
        }else {
            this.curentLine = 0L;
            executionContext.put("curentLine",this.curentLine);
            System.out.println("Step执行之前，open()方法执行， Starting Reading From Line: "+this.curentLine+1);
        }
    }

    /**
     * 该方法在处理完 Chunk的指定大小的数据后执行
     * 本例中，Chunk设置为10 即 每读取10行数据 执行一次update()方法
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("currentLine",this.curentLine);
        System.out.println("update()执行， 当前行："+this.curentLine);
    }

    /**
     * 整个Step执行结束后执行
     * @throws ItemStreamException
     */
    @Override
    public void close() throws ItemStreamException {
        System.out.println("整个Step执行完毕，close方法执行");
    }

}
