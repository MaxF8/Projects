package edu.yu.introtoalgs;

import java.util.Collections;
import java.util.HashMap;
import java.util.*;


public class EstimateSecretAlgorithmsClient2
{
    public static void main(String[] args)
    {
        final int NumberOfLoops = 4; //number of times each algorithm will run. This is so we can average each data point
        BigOMeasurable algOne = new SecretAlgorithm1();
        BigOMeasurable algTwo = new SecretAlgorithm2();
        BigOMeasurable algThree = new SecretAlgorithm3();
        BigOMeasurable algFour = new SecretAlgorithm4();

        for (int i = 1; i <= 4; i++) {
            
            System.out.println("*********");
            System.out.println(i);
            System.out.println("*********");

            HashMap<Integer,Double> dataMap = new HashMap<>(); //this will hold n and f(n)
            List<Double> doublingRatioList = new ArrayList<>();
            boolean isTimeTooLong = false;
            boolean nanoSeconds = false;

            System.out.println("n, f(n), lg(n), lg(f(n))");
            for (int j = 0; j < NumberOfLoops; j++) {
                for (int n = 256; n < 500000000; n*=2) //n will be doubled
                {
                    BigOMeasurable algorithm = algOne;
                    if (i == 2)
                    {algorithm = algTwo;}
                    else if (i == 3)
                    {algorithm = algThree;}
                    else if (i == 4)
                    {algorithm = algFour;
                    nanoSeconds = true;}
                    if (isTimeTooLong == false) //if runtime is shorter than 8 minutes
                    {
                        long startTime = 0;
                        long endTime = 0;
                        if (nanoSeconds == false) //if algorithm 1,2,3 is running
                        {
                            startTime = System.nanoTime()/1000; //(microseconds)
                            algorithm.setup(n);
                            algorithm.execute();
                            endTime = System.nanoTime()/1000;
                        }
                        else if (nanoSeconds == true) //if algorithm 4 is running
                        {
                            startTime = System.nanoTime();
                            algorithm.setup(n);
                            algorithm.execute();
                            endTime = System.nanoTime();
                        }
                        long timeElapsed = endTime - startTime; //time it took the algorithm to run n
                        
                        double alreadyElapsedTime = 0;
                        double timeElapsedDecimal = (double)timeElapsed;
                        double timeElapsedInSeconds = timeElapsed/1000000; 
                        if (timeElapsedInSeconds <= 480) //8 minutes
                        {
                            if (dataMap.get(n) != null) //this will not be null if the algorithm has looped at least once
                            {
                                alreadyElapsedTime = dataMap.get(n);
                            }
                            dataMap.put(n, timeElapsedDecimal + alreadyElapsedTime); //add new timeElapsed to alreadyElapsedTime so we can take the avg eventually
                        }
                        else { //runtime is > 8 minutes
                            isTimeTooLong = true;
                            dataMap.put(n, timeElapsedDecimal*NumberOfLoops); 
                        }
                    }
                }
                //algorithm loops again
                isTimeTooLong = false;
                nanoSeconds = false;
            }
            List<Integer> list = new ArrayList<>(dataMap.keySet()); //list of each doubled n
            Collections.sort(list); //sorted from smallest to largest
        
            for (Integer n : list) { //now avg each data point of n, f(n), lg(n), lg(f(n))
                int logN = (int)(Math.log(n) / Math.log(2)); //log base 2
                double avgRuntime = (dataMap.get(n)/NumberOfLoops); 
                doublingRatioList.add(avgRuntime);
                double logFn = ((Math.log(avgRuntime) / Math.log(2)));
                String logFnRounded = String.format("%.1f", logFn);

                //print n, f(n), lg(n), lg(f(n))
                System.out.print(n +","+ (+avgRuntime));  
                System.out.print(","+logN +","+ logFnRounded);   
                System.out.println();   
            }
         
            double finalDoublingRatio = doublingRatio(doublingRatioList); //get doubling ratio

            System.out.println("Doubling Ratio: "+finalDoublingRatio);
        }
    }


    private static double doublingRatio(List<Double> doublingRatioList)
    {
        double doublingRatioTotal = 0;
        double oldGrowthRatio = 1;
        double newGrowthRatio = 0;
        int i = 1;

        for (double x : doublingRatioList) { //list of f(n)
            newGrowthRatio = doublingRatioList.get(i); 
            oldGrowthRatio = doublingRatioList.get(i-1);
            doublingRatioTotal += (newGrowthRatio/oldGrowthRatio); //doubling ratio formula
            oldGrowthRatio = doublingRatioList.get(i);
            i++;
            if (i == doublingRatioList.size()) //reached the end of the list 
            break;
        }
        double finalDoublingRatio = doublingRatioTotal / (i-1); //get the avg

        return finalDoublingRatio;
    } 
}
