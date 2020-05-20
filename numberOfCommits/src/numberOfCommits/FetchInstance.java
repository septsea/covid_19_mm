package numberOfCommits                                                                                                                         ;

public class FetchInstance                                                                                                                      {

    public static void main(final String... args)                                                                                               {
        fetch("atom")                                                                                                                           ;
        fetch("brackets")                                                                                                                       ;
        fetch("vscode")                                                                                                                         ;}

    public static void fetch(final String repo)                                                                                                 {
        // It takes long time to load GitHub,
        // so we manually clone the repositories.
        // Hence the owner is not the original owner.
        final var owner = "septsea"                                                                                                             ;
        // It is 140 days from the start date = 20191229
        // to the end date = 20200516, end date included
        var date = "20200516"                                                                                                                   ;
        var i = 0                                                                                                                               ;
        while (i < 140)                                                                                                                         {
            final var data = date + "," + Crawler.numberOfCommits(owner, repo, date)                                                            ;
            Crawler.txtWriter(repo + "_After.txt", data + "\n", true)                                                                           ;
            System.out.println(data)                                                                                                            ;
            date = Crawler.addDays(date, -1)                                                                                                    ;
            i -=- 1                                                                                                                             ;}
        // It is 140 days from the start date = 20190811
        // to the end date = 20191228, end date included
        date = "20191228"                                                                                                                       ;
        i = 0                                                                                                                                   ;
        while (i < 140)                                                                                                                         {
            final var data = date + "," + Crawler.numberOfCommits(owner, repo, date)                                                            ;
            Crawler.txtWriter(repo + "_Before.txt", data + "\n", true)                                                                          ;
            System.out.println(data)                                                                                                            ;
            date = Crawler.addDays(date, -1)                                                                                                    ;
            i -=- 1                                                                                                                             ;}}}
