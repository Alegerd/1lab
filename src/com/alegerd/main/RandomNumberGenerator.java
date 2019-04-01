package com.alegerd.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomNumberGenerator {

    private Double prevU;

    /**
     * Генерируем набор случайных чисел
     * @param M
     * @param p
     * @param R0
     * @param howManyToGenerate
     * @return
     */
    public List<Double> next(Integer M, Integer p, Double R0, Long howManyToGenerate){
        List<Double> result = new ArrayList<>();
        Double prevU =  R0 * p;
        for(int i = 0; i < howManyToGenerate; i++){
            result.add(Math.round((prevU / p) * 100.0) / 100.0);
            prevU = (prevU * M) % p;
        }
        return result;
    }

    /**
     * Генерирует случайное число
     * @param M
     * @param p
     * @return
     */
    public Double next(Integer M, Integer p){
        prevU = (prevU * M) % p;
        return prevU / p;
    }

    public void start(Integer p, Double R0){
        this.prevU = R0 * p;
    }

    /**
     * Генерируем список возможных параметров датчика
     * @param pMin
     * @param pMax
     * @param delta
     * @return
     */
    public Map<Integer, Integer> generateParameters(Integer pMin, Integer pMax, Integer delta){
        Map<Integer, Integer> result = new HashMap<>();
        for(int p = pMin; p < pMax; p++){
            for(int n = 0; n < findTopN(p); n++) {
                Double M = p - Math.pow(3, n);
                if(isCorrectM(M,p,n,delta)){
                    result.put(p, M.intValue());
                }
            }
        }
        return result;
    }

    /**
     * Проверяем то, что M удовлетворяет двум условиям датчика:
     * M - целое число
     * M ~= p/2
     * @param M
     * @param p
     * @param n
     * @param delta
     * @return
     */
    private boolean isCorrectM(Double M, Integer p, Integer n, Integer delta){
        // проверка на то, что M - целое число
        if(M % 1 != 0) {
            return false;
        } else {
            //проверка на то, что p/2 приблизительно равно M
            Double temp = p / 2.0;
            return (Math.abs(M - temp) < delta)? true : false;
        }
    }

    /**
     * Находим максимальное n, при котором M не отрицательно
     * @param p
     * @return
     */
    private Integer findTopN(Integer p){
        Integer max = 0;
        while (p > Math.pow(3, max)){
            max++;
        }
        return max;
    }
}
