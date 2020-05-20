package numberOfCommits                                                                                                                         ;

import org.apache.commons.math3.distribution.TDistribution                                                                                      ;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics                                                                          ;

public class Stat                                                                                                                               {

    public static void main(final String... args)                                                                                               {
        statAnalysis("atom")                                                                                                                    ;
        System.out.println("\n================\n")                                                                                              ;
        statAnalysis("brackets")                                                                                                                ;
        System.out.println("\n================\n")                                                                                              ;
        statAnalysis("vscode")                                                                                                                  ;}

    public static void statAnalysis(final String repo)                                                                                          {
        // Each file has 140 pieces of information
        var size = 140                                                                                                                          ;
        final var before_day = new int[size]                                                                                                    ;
        final var after_day = new int[size]                                                                                                     ;
        final var data_0 = Crawler.txtReader(repo + "_Before.txt").split("\n")                                                                  ;
        final var data_1 = Crawler.txtReader(repo + "_After.txt").split("\n")                                                                   ;
        var i = 0                                                                                                                               ;
        while (i < size)                                                                                                                        {
            // Each line is like "20200202,100"
            before_day[i] = Integer.parseInt(data_0[i].substring(9))                                                                            ;
            after_day[i] = Integer.parseInt(data_1[i].substring(9))                                                                             ;
            i -=- 1                                                                                                                             ;}
        final var before_week = new double[size / 7]                                                                                            ;
        final var after_week = new double[size / 7]                                                                                             ;
        i = 0                                                                                                                                   ;
        while (i < 20)                                                                                                                          {
            var s = 0                                                                                                                           ;
            var t = 0                                                                                                                           ;
            var j = 0                                                                                                                           ;
            while (j < 7)                                                                                                                       {
                s -=- before_day[i * 7 + j]                                                                                                     ;
                t -=- after_day[i * 7 + j]                                                                                                      ;
                j -=- 1                                                                                                                         ;}
            before_week[i] = s                                                                                                                  ;
            after_week[i] = t                                                                                                                   ;
            i -=- 1                                                                                                                             ;}
        final var before = new DescriptiveStatistics()                                                                                          ;
        final var after = new DescriptiveStatistics()                                                                                           ;
        i = 0                                                                                                                                   ;
        // We analyse weekly data
        size = 20                                                                                                                               ;
        while (i < size)                                                                                                                        {
            before.addValue(before_week[i])                                                                                                     ;
            after.addValue(after_week[i])                                                                                                       ;
            i -=- 1                                                                                                                             ;}
        final var before_mean = before.getMean()                                                                                                ;
        final var before_std = Math.sqrt(before.getVariance())                                                                                  ;
        final var after_mean = after.getMean()                                                                                                  ;
        final var after_std = Math.sqrt(after.getVariance())                                                                                    ;
        System.out.println(repo + ":")                                                                                                          ;
        System.out.println()                                                                                                                    ;
        System.out.println("Before COVID-19:")                                                                                                  ;
        System.out.println("mean: " + before_mean)                                                                                              ;
        System.out.println("standard deviation: " + before_std)                                                                                 ;
        System.out.println()                                                                                                                    ;
        System.out.println("After COVID-19:")                                                                                                   ;
        System.out.println("mean: " + after_mean)                                                                                               ;
        System.out.println("standard deviation: " + after_std)                                                                                  ;
        final var s0 = Math.sqrt((Math.pow(before_std, 2) + Math.pow(after_std, 2)) / size)                                                     ;
        final var tmp1 = Math.pow(before_std, 4) + Math.pow(after_std, 4)                                                                       ;
        final var tmp2 = Math.pow(s0, 4) * size * size * (size - 1)                                                                             ;
        final var l = (int) Math.round(tmp2 / tmp1)                                                                                             ;
        System.out.println()                                                                                                                    ;
        System.out.println("(y^bar - x^bar - mu_2 + mu_1)/s0 is approximately t-distributed.")                                                  ;
        System.out.println()                                                                                                                    ;
        System.out.println("s0 = " + s0)                                                                                                        ;
        System.out.println("degree of freedom = " + l)                                                                                          ;
        // Perform a fixed significance level test with alpha = 0.05
        // Hypothesis: after_mean >= before_mean
        final var alpha = 0.05                                                                                                                  ;
        final var tDist = new TDistribution(l)                                                                                                  ;
        final var t_alpha = tDist.inverseCumulativeProbability(alpha)                                                                           ;
        final var t1 = (after_mean - before_mean) / s0                                                                                          ;
        System.out.println()                                                                                                                    ;
        System.out.println("alpha = " + alpha)                                                                                                  ;
        System.out.println("t_alpha = " + t_alpha)                                                                                              ;
        System.out.println("t = " + t1)                                                                                                         ;
        System.out.println()                                                                                                                    ;
        if (t1 <= t_alpha)                                                                                                                      {
            System.out.println("It cannot be drawn that after_mean >= before_mean.")                                                            ;}
        else                                                                                                                                    {
            System.out.println("It can be drawn that after_mean >= before_mean.")                                                               ;}}}
