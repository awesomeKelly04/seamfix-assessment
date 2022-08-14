package com.example.demo;

import java.util.*;

public class Main {

    public static int numberOfNotification(String input){
        int notification = 0;
        if (input == null || input.equals("")){
            return notification;
        }
        String[] lines = input.split("\\R");
        if (lines.length < 2){
            return notification;
        }
        String[] dn = lines[0].split("\\s+");
        String[] expenditure = lines[1].split("\\s+");
        int n = Integer.parseInt(dn[0]);
        int d = Integer.parseInt(dn[1]);
        if(n <= d){
            return notification;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < d; i++){
            queue.add(Integer.parseInt(expenditure[i]));
        }

        for (int j = d; j < n; j++){
            int exp = Integer.parseInt(expenditure[j]);
            int median = getMedian(queue, d);
            if (exp >= 2 * median){
                notification++;
            }
            queue.poll();
            queue.add(exp);
        }
        return notification;
    }

    private static int getMedian(Queue<Integer> queue, int d) {
        List<Integer> tempArr = new ArrayList<>(queue);
        Collections.sort(tempArr);
        if (d % 2 == 0){
            return  (tempArr.get(d / 2) + tempArr.get((d / 2) - 1))/2;
        } else {
            return tempArr.get(d / 2);
        }
    }

    public static void main(String[] args) {
        String str = "9 5\n2 3 4 2 3 6 8 4 5";
        System.out.println(numberOfNotification(str));
    }
}