package com.creative.atom.action;

/**
 * @author zhiqiang on 2018/4/19
 */
public class SimpleLogger {

    private static ILogger logger;

    public static void setLogger(ILogger logger) {
        SimpleLogger.logger = logger;
    }

    public static ILogger defaultLogger(){
        if(logger==null){
            logger=new DefaultLogger();
        }
        return logger;
    }

   public static class DefaultLogger implements ILogger{

        @Override
        public void i(String tag, String msg) {
            System.out.print(msg);
        }

        @Override
        public void d(String tag, String msg) {
            i(tag,msg);
        }

        @Override
        public void e(String tag, String msg) {
            i(tag,msg);
        }
    }


    public interface ILogger {
        void i(String tag,String msg);
        void d(String tag,String msg);
        void e(String tag,String msg);
    }

}
