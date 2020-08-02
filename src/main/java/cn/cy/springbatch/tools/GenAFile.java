package cn.cy.springbatch.tools;


import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成一个文件 以作数据源
 */
public class GenAFile {

    public static void main(String[] args) {

        PrintWriter printWriter = null;
        try {
            String path = "C:\\Users\\93700\\Desktop\\persons.txt";
            File file = new File(path);
            boolean exist = false;
            if(!file.exists()){
                exist = file.createNewFile();
            }
            if (exist) {
                printWriter = new PrintWriter(path);
                printWriter.println("id , name , birthay");
                for (int i=0;i<500;i++) {
                    printWriter.println(getAperson(i+1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter!=null) {
                printWriter.flush();
                printWriter.close();
            }
        }

    }

    /**
     * 生成一个Person对象字符串
     * @param id id
     * @return Person对象字符串
     */
    public static String getAperson(int id) {
        PersonBean personBean = new PersonBean();
        String dateStr = getDateStr("2000-01-01", "2005-12-31");
        personBean.setBirthday(dateStr);
        String randomStr = getRandomStr();
        personBean.setName(randomStr);
        personBean.setId(id);
        return personBean.toString();
    }

    /**
     * 生成指定时间段内的随机时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 随机时间
     */
    public static String getDateStr(String startTime, String endTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d");
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            long start = startDate.getTime();
            long end = endDate.getTime();
            long time = (long) (Math.random() * (start - end + 1) + end);
            Date theTime = new Date(time);
            return dateFormat.format(theTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成五个随机字母组成的随机字符串
     *
     * @return 随机字符串
     */
    public static String getRandomStr() {

        String s = "abcdefghijklmnopqrstuvwxyz";

        char[] chars = s.toCharArray();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int nextInt = random.nextInt(s.length());
            builder.append(chars[nextInt]);
        }
        return builder.toString();
    }


}
