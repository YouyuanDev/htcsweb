package com.htcsweb.util;

public class PipeActWeightUtil {

    public static double factor = 0.02466;

    //公式:W=0.02466×壁厚x(外径-壁厚)*管长
    public static float getActWeight(float pipe_length,float od, float wt){

        float   actWeight  =   (float) factor*wt*(od-wt)*pipe_length/1000;
        float  w   =  (float)(Math.round(actWeight*1000))/1000;

        return w;

    }

}
