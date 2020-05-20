package numberOfCommits                                                                                                                         ;

import java.io.BufferedReader                                                                                                                   ;
import java.io.BufferedWriter                                                                                                                   ;
import java.io.FileReader                                                                                                                       ;
import java.io.FileWriter                                                                                                                       ;
import java.io.InputStreamReader                                                                                                                ;
import java.net.URL                                                                                                                             ;
import java.text.ParseException                                                                                                                 ;
import java.text.SimpleDateFormat                                                                                                               ;
import java.util.Calendar                                                                                                                       ;

import javax.net.ssl.HttpsURLConnection                                                                                                         ;
import javax.net.ssl.SSLContext                                                                                                                 ;
import javax.net.ssl.TrustManager                                                                                                               ;

import org.json.JSONArray                                                                                                                       ;

/**
 * The core.
 */
public class Crawler                                                                                                                            {

    /**
     * Do an HTTPS request.
     *
     * @param requestUrl    the request HTTPS URL.
     * @param requestMethod the request method, one of "GET", "POST", "HEAD",
     *                      "OPTIONS", "PUT", "DELETE", "TRACE". If you do not know
     *                      what to use, set it "GET".
     * @param outputStr     string to be written to the server. If no string is
     *                      needed to be written, set it {@code null}.
     * @return the string sent by the server.
     */
    public static String httpsRequest(final String requestUrl, final String requestMethod, final String outputStr)                              {
        StringBuffer buffer = null                                                                                                              ;
        try                                                                                                                                     {
            // Create SSLContext
            final var sslContext = SSLContext.getInstance("SSL")                                                                                ;
            final var tm = new TrustManager[] { new MyX509TrustManager() }                                                                      ;
            // Initialize
            sslContext.init(null, tm, new java.security.SecureRandom())                                                                         ;
            // Get SSLSocketFactory object
            final var ssf = sslContext.getSocketFactory()                                                                                       ;
            final var url = new URL(requestUrl)                                                                                                 ;
            final var conn = (HttpsURLConnection) url.openConnection()                                                                          ;
            conn.setDoOutput(true)                                                                                                              ;
            conn.setDoInput(true)                                                                                                               ;
            conn.setUseCaches(false)                                                                                                            ;
            // Cheat the API
            final var key = "User-Agent"                                                                                                        ;
            var value = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit"                                                                  ;
            value += "/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)"                                                          ;
            conn.setRequestProperty(key, value)                                                                                                 ;
            conn.setRequestMethod(requestMethod)                                                                                                ;
            // Set up SSLSoctetFactory
            conn.setSSLSocketFactory(ssf)                                                                                                       ;
            conn.connect()                                                                                                                      ;
            // Write a string to server
            if (null != outputStr)                                                                                                              {
                final var os = conn.getOutputStream()                                                                                           ;
                os.write(outputStr.getBytes("utf-8"))                                                                                           ;
                os.close()                                                                                                                      ;}
            // Read content sent by server
            final var is = conn.getInputStream()                                                                                                ;
            final var isr = new InputStreamReader(is, "utf-8")                                                                                  ;
            final var br = new BufferedReader(isr)                                                                                              ;
            buffer = new StringBuffer()                                                                                                         ;
            String line = null                                                                                                                  ;
            while ((line = br.readLine()) != null)                                                                                              {
                buffer.append(line)                                                                                                             ;}}
        catch (final Exception e)                                                                                                               {
            e.printStackTrace()                                                                                                                 ;}
        return buffer.toString()                                                                                                                ;}

    /**
     * Give how many commits there are on a specified date.
     *
     * @param owner the owner of the repository.
     * @param repo  the name of the repository.
     * @param date  Only commits on this date will be returned. This is a timestamp
     *              in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
     * @return the number of commits.
     */
    public static int numberOfCommits(final String owner, final String repo, final String date)                                                 {
        final var since = date                                                                                                                  ;
        final var until = addDays(date, 1)                                                                                                      ;
        return numberOfCommits(owner, repo, since, until)                                                                                       ;}

    /**
     * Give how many commits there are during a specified period.
     *
     * @param owner the owner of the repository.
     * @param repo  the name of the repository.
     * @param since Only commits after this date will be returned. This is a
     *              timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
     * @param until Only commits before this date will be returned. This is a
     *              timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
     * @return the number of commits.
     */
    public static int numberOfCommits(final String owner, final String repo, final String since, final String until)                            {
        final var link = new StringBuffer()                                                                                                     ;
        link.append("https://gitee.com/api/v5/repos/")                                                                                          ;
        link.append(owner)                                                                                                                      ;
        link.append("/")                                                                                                                        ;
        link.append(repo)                                                                                                                       ;
        link.append("/commits?&since=")                                                                                                         ;
        link.append(since)                                                                                                                      ;
        link.append("&until=")                                                                                                                  ;
        link.append(until)                                                                                                                      ;
        // At most 100 records per page.
        // So if there is a date when num of commits >= 100,
        // recheck the date manually.
        link.append("&page=1&per_page=100")                                                                                                     ;
        final var json = new JSONArray(httpsRequest(link.toString(), "GET", null))                                                              ;
        return json.length()                                                                                                                    ;}

    /**
     * Read a txt file.
     *
     * @param txtFile the path of the txt file.
     * @return the string of the file.
     */
    public static String txtReader(final String txtFile)                                                                                        {
        final var sb = new StringBuffer()                                                                                                       ;
        try                                                                                                                                     {
            final var br = new BufferedReader(new FileReader(txtFile))                                                                          ;
            String s = null                                                                                                                     ;
            while ((s = br.readLine()) != null)                                                                                                 {
                sb.append(s)                                                                                                                    ;
                sb.append("\n")                                                                                                                 ;}
            br.close()                                                                                                                          ;}
        catch (final Exception e)                                                                                                               {
            e.printStackTrace()                                                                                                                 ;}
        return sb.toString()                                                                                                                    ;}

    /**
     * Write a string to a file.
     *
     * @param txtFile the txt file to be written to.
     * @param str     the string to be written.
     * @param append  if {@code true}, then data will be written to the end of the
     *                file rather than the beginning.
     */
    public static void txtWriter(final String txtFile, final String str, final boolean append)                                                  {
        try                                                                                                                                     {
            final var bw = new BufferedWriter(new FileWriter(txtFile, append))                                                                  ;
            bw.write(str)                                                                                                                       ;
            bw.close()                                                                                                                          ;}
        catch (final Exception e)                                                                                                               {
            e.printStackTrace()                                                                                                                 ;}}

    /**
     * Add or subtract the specified amount of time to the given calendar field,
     * based on the calendar's rules.
     *
     * @param date the date to be added.
     * @param n    the amount of days to be added to the date. Negative integers are
     *             allowed.
     * @return the new date.
     */
    public static String addDays(final String date, final int n)                                                                                {
        final var sdf = new SimpleDateFormat("yyyyMMdd")                                                                                        ;
        String newDate = null                                                                                                                   ;
        try                                                                                                                                     {
            final var originalDate = sdf.parse(date)                                                                                            ;
            final var c = Calendar.getInstance()                                                                                                ;
            c.setTime(originalDate)                                                                                                             ;
            c.add(Calendar.DATE, n)                                                                                                             ;
            newDate = sdf.format(c.getTime())                                                                                                   ;}
        catch (final ParseException e)                                                                                                          {
            e.printStackTrace()                                                                                                                 ;}
        return newDate                                                                                                                          ;}}
