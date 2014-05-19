package edu.dhbw.andar.layout;

import java.util.ArrayList;
import java.util.Random;

public class RandomPosition {
	 private ArrayList<Integer> numbers; // Integer array for holding repeat checking data
     private Random gen;
            
     public RandomPosition()
     {
             numbers = new ArrayList<Integer>();
             gen = new Random();
     }
    
     public  int getNextInt(int min, int max) // generate a number between min and max, not including max
     {
             int temp = gen.nextInt(max - min) + min;
            
             if (numbers.isEmpty())
                     numbers.add(temp);
            
             else
             {
                     while (!isAvailable(temp))
                             temp = gen.nextInt(max - min) + min;
             }
            
             numbers.add(temp);
            
             return temp;
     }
    
     private boolean isAvailable(int check) // check to see if 'check' is a repeat number
     {
             for (int x = 0; x < numbers.size(); x++)
                     if (check == numbers.get(x).intValue())
                             return false;
                            
             return true;
     }
    
}
