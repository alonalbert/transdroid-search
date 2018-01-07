package org.transdroid.search.adapters.html.privatetrackers;

import android.content.SharedPreferences;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.transdroid.search.SearchResult;
import org.transdroid.search.SortOrder;
import org.transdroid.search.TorrentSite;
import org.transdroid.search.adapters.html.AbstractHtmlAdapter;
import org.transdroid.util.DateUploadedHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class TorrentingAdapter extends AbstractHtmlAdapter {
  private static final String BASE_URL = "https://www.torrenting.com/";
  private static final String QUERY_URL = BASE_URL + "browse.php?search=%1$s";
  private static final String COOKIE_UID = "uid";
  private static final String COOKIE_PASS = "pass";
  public static final String SORT_BY_SEEDERS = "&sort=7&type=desc";

  @Override
  protected String getLoginUrl() {
    return BASE_URL;
  }

  @Override
  protected String getSearchUrl(SharedPreferences prefs, String query, SortOrder order, int maxResults) throws UnsupportedEncodingException {
    final String url = String.format(QUERY_URL, URLEncoder.encode(query, "UTF-8"));
    return order == SortOrder.BySeeders ? url + SORT_BY_SEEDERS : url;
  }

  @Override
  protected Elements selectTorrentElements(Document document) {
    return document.select("tr.torrentsTableTR");
  }

  @Override
  protected boolean isAuthenticationRequiredForTorrentLink() {
    return true;
  }

  @Override
  protected SearchResult buildSearchResult(Element torrentElement) {
    final Element torrentNameElement = torrentElement.selectFirst("a.nameLink");
    final String title = torrentNameElement.text();
    final String torrentUrl= BASE_URL + torrentElement.selectFirst("a[href$=.torrent]").attr("href");
    final String detailsUrl= BASE_URL + torrentNameElement.attr("href");
    final String size = torrentElement.selectFirst("td:nth-last-child(3)").text();
    final Date added = DateUploadedHelper.convertFromWordTimeSpan(torrentElement.selectFirst("div.uploaded").text());
    final int seeds = Integer.valueOf(torrentElement.selectFirst("td:nth-last-child(2)").text());
    final int leechers = Integer.valueOf(torrentElement.selectFirst("td:last-child").text());

    return new SearchResult(title, torrentUrl, detailsUrl, size, added, seeds, leechers);
  }

  @Override
  protected TorrentSite getTorrentSite() {
    return TorrentSite.Torrenting;
  }

  @Override
  public String buildRssFeedUrlFromSearch(String query, SortOrder order) {
    // not implemented
    return null;
  }

  @Override
  public String getSiteName() {
    return "Torrenting";
  }

  public AuthType getAuthType() {
    return AuthType.COOKIES;
  }

  public String[] getRequiredCookies() {
    return new String[]{COOKIE_UID, COOKIE_PASS};
  }


}
