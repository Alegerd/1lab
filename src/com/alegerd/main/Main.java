package com.alegerd.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static Double first;
    public static Double number;
    public static Integer count;
    public static Integer maxL;
    public static Integer bestM;
    public static Integer bestP;
    public static Double bestR0;
    public static List<Double> firstLNumbers;

    public static void main(String[] arg) {
        final Integer pMin = 2027;
        final Integer pMax = 5087;
        final Integer delta = 5;
        Double R0 = 0.01;
        maxL = 0;
        Double n;
        final Long howManyToGenerate = 1000l;

        RandomNumberGenerator generator = new RandomNumberGenerator();
        List<List<Double>> results = new ArrayList<>();

        //получаем список возможных параметров
        Map<Integer, Integer> parameters = generator.generateParameters(pMin, pMax, delta);

        //получаем списки сгенерированных случайных величин
//        parameters.forEach( (p, M) -> {
//            List<Double> randomNumbers = generator.next(M, p, R0, howManyToGenerate);
//            results.add(randomNumbers);
//            Integer L = findL(randomNumbers);
//            System.out.println(String.format(" (M = %d, p = %d, L = %d) -> ", M, p, L));
//        });

        parameters.forEach((p, M) -> {
            firstLNumbers = new ArrayList<>();
            generator.start(p, 0.01);
            count = 0;
            for (Long i = 1l; i < howManyToGenerate; i++) {
                number = generator.next(M, p);
                number = Math.round(number * 100.0) / 100.0;
                firstLNumbers.add(number);
                if (i == 11) {
                    first = number;
                    continue;
                } else {
                    // System.out.println(Main.number);
                    count++;
                    if (number.equals(first)) {
                        if (maxL < count) {
                            maxL = count;
                            bestM = M;
                            bestP = p;
                        }
                        break;
                    }
                }
            }

            System.out.println(String.format(" (M = %d, p = %d, L = %d, D = %f)", M, p, Main.count, calculateDispersion()));
        });
        System.out.println(String.format("Max L = %d, Best M = %d, Best p = %d", maxL, bestM, bestP));

        System.out.println("Меняем R0");

        Double largerD = 0.0;
        for (R0 = 0.01; R0 < 1; R0 += 0.01) {
            firstLNumbers = new ArrayList<>();
            generator.start(bestP, R0);
            for (Long i = 1l; i < howManyToGenerate; i++) {
                number = generator.next(bestM, bestP);
                number = Math.round(number * 100.0) / 100.0;
                firstLNumbers.add(number);
            }
            Double D = calculateDispersion();
            System.out.println(String.format("R0 = %f, D = %f", R0, D));
            if (largerD < D) {
                largerD = D;
                bestR0 = R0;
            }
        }
        System.out.println(String.format("\r Best parameters: M = %d, p = %d, R0 = %f, D = %f", bestM, bestP, bestR0, largerD));
    }

    private static Double calculateDispersion() {
        Double middle = 0.0;
        for (Double number : firstLNumbers) {
            middle += number;
        }
        middle = middle / firstLNumbers.size();
        Double difference = 0.0;
        for (Double number : firstLNumbers) {
            difference += Math.abs(number - middle);
        }
        return difference / firstLNumbers.size();
    }

    private static Integer findL(List<Double> numbers) {
        Double first = numbers.get(2);
        Integer count = 1;
        for (int i = 3; i < numbers.size(); i++) {
            if (!numbers.get(i).equals(first)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
