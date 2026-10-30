package org.mazhuang.guanggoo.data.task;

import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.SearchResult;
import org.mazhuang.guanggoo.data.entity.SearchResultPage;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取帖子搜索结果。
 *
 * @author mazhuang
 */
public class GetSearchResultTask extends BaseTask<SearchResultPage> {

    private static final String NEXT_PAGE_TEXT = "下一页";

    private final String mUrl;

    public GetSearchResultTask(String url, OnResponseListener<SearchResultPage> listener) {
        super(listener);
        mUrl = url;
    }

    @Override
    public void run() {
        SearchResultPage page = new SearchResultPage();
        try {
            Document doc = get(mUrl);
            Elements articles = doc.select("article.result");
            for (Element article : articles) {
                SearchResult result = parseArticle(article);
                if (result != null) {
                    page.getData().add(result);
                }
            }
            page.setNextPageUrl(parseNextPageUrl(doc));
            successOnUI(page);
        } catch (Exception e) {
            e.printStackTrace();
            failedOnUI("搜索失败，请稍后重试");
        }
    }

    private String parseNextPageUrl(Document doc) {
        for (Element link : doc.select("nav.search-pages a")) {
            if (NEXT_PAGE_TEXT.equals(link.text().trim())) {
                String href = link.absUrl("href");
                return TextUtils.isEmpty(href) ? null : href;
            }
        }
        return null;
    }

    private SearchResult parseArticle(Element article) {
        Element titleLink = article.select("h2 a").first();
        if (titleLink == null || TextUtils.isEmpty(titleLink.absUrl("href"))) {
            return null;
        }

        SearchResult result = new SearchResult();
        result.setTitle(titleLink.text());
        result.setUrl(titleLink.absUrl("href"));

        Element meta = article.select("div.meta").first();
        if (meta != null) {
            Elements links = meta.select("a");
            if (links.size() > 0) {
                result.setNodeTitle(links.get(0).text());
            }
            if (links.size() > 1) {
                result.setAuthor(links.get(1).text());
            }

            List<String> ownTexts = new ArrayList<>();
            for (String part : meta.ownText().split("·")) {
                String text = part.trim();
                if (!TextUtils.isEmpty(text)) {
                    ownTexts.add(text);
                }
            }
            if (ownTexts.size() > 0) {
                result.setDate(ownTexts.get(0));
            }
            if (ownTexts.size() > 1) {
                result.setHitType(ownTexts.get(1));
            }
        }

        Element excerpt = article.select("p.excerpt").first();
        if (excerpt != null) {
            result.setExcerptHtml(excerpt.html());
        }

        return result;
    }
}
