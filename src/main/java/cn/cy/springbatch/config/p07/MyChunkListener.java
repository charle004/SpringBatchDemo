package cn.cy.springbatch.config.p07;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 *  用注解的方式 创建 Chunk的监听器
 *  也可以用接口的方式
 */
public class MyChunkListener{

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) throws Exception {
        System.out.println(chunkContext.getStepContext().getStepName()+"准备执行");
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) throws Exception {
        System.out.println(chunkContext.getStepContext().getStepName()+"执行完毕");
    }

}
