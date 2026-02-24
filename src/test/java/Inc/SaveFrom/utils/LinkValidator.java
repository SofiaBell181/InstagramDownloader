package Inc.SaveFrom.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

public class LinkValidator {

	public List<String> findBrokenLinks(List<WebElement> links) {
		HttpClient client = HttpClient.newHttpClient();
		List<String> brokenlinks = new ArrayList<String>();
		
		List<String> hrefs = links.stream().map(l -> l.getAttribute("href")).filter(l -> !l.isEmpty())
				.filter(l -> !l.startsWith("mailto:")).filter(l -> !l.endsWith("#")).toList();
		System.out.println(hrefs);

		for (String href : hrefs) {

			try {
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(href)).timeout(Duration.ofSeconds(10))
						.GET().build();

				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				if (response.statusCode() >= 400) {
					brokenlinks.add(href + "- >" + response.statusCode());
				}
			} catch (Exception e) {
				brokenlinks.add(href + " -> REQUEST FAILED");
			}

		}

		return brokenlinks;
	}
}
