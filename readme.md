### Spring Batch 代码案例
包名对应代码的示例：
1. p01 快速入门
2. p02 限定Step的顺序 以及执行条件
3. p03 Flow是Step对象的组合
4. p04 利用split实现Step / Flow 对象的并发执行
5. p05 决策器的创建 什么条件下执行什么step
6. p06 Job的嵌套 一个Job对象可以作为另一个Job的Step（需要先将Job转为Step对象）
7. p07 Job/Step监听器，用于实现Job/Step执行前后的逻辑 以**ListItemReader**读取集合
8. p08 Job的参数，实际是Step的参数，利用监听器传递Step的参数
9. p09 自定义一个实现了**ItemReader接口**的对象，实现数据的输入
10. p10 利用**JdbcPagingItemReader**对象从数据库中获取数据
11. p11 利用**FlatFileItemReader**对象从普通文本文件读取数据
12. p12 利用**MultiResourceItemReader**对象读取多个文本文件
13. p13 创建实现了**ItemStreamReader**接口的ItemReader对象，处理读取时的异常，该接口的方法执行时机见resource目录下的示意图
14. p14 ItemWriter概述,ItemWriter在输出时是一批一批的输出的（每批的大小即chunk指定的大小）代码省略，上述包中已经涉及
15. p15 使用**JdbcBatchItemWriter**对象将读取的数据输出到数据库中
16. p16 使用**FlatFileItemWriter**对象实现将读取的数据写出到文件中
17. p17 使用**CompositeItemWriter**对实现多文件的写出
18. p18 使用**ClassifierCompositeItemWriter**对象实现不同对象写出不同的文件


