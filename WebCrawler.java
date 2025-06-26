import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler_113502004 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a URL: ");
        String urlString = scanner.nextLine();

        try {
            // 讀取 HTML 原始碼
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            StringBuilder htmlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line).append("\n");
            }
            reader.close();
            String html = htmlBuilder.toString();

            // 抓取 <img> 中的 src 屬性
            System.out.println("\nFound Image URIs:");
            extractAndPrint(html, "<img[^>]+src=[\"']?([^\"'>\\s]+)", urlString);

            // 抓取 <a> 中的 href 屬性
            System.out.println("\nFound All URLs:");
            extractAndPrint(html, "<a[^>]+href=[\"']?([^\"'>\\s]+)", urlString);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void extractAndPrint(String html, String regex, String baseUrl) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String link = matcher.group(1);
            // 處理相對路徑
            if (!link.startsWith("http") && !link.startsWith("https")) {
                try {
                    URL base = new URL(baseUrl);
                    URL absolute = new URL(base, link);
                    link = absolute.toString();
                } catch (MalformedURLException e) {
                    continue;
                }
            }
            System.out.println(link);
        }
    }
}
