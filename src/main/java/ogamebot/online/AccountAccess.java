package ogamebot.online;

import ogamebot.comp.Player;
import ogamebot.comp.Universe;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tools.Condition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class AccountAccess {
    private final int serverId;
    private final Universe universe;
    private CloseableHttpClient client;
    private boolean loggedIn;

    AccountAccess(Universe universe) {
        this.universe = universe;
        this.serverId = universe.getNameId();
    }

    Player getPlayer(String mail, String passWord) throws IOException {
        Condition.check().nonNull(mail, passWord).notEmpty(mail, passWord);
        if (!Online.isOnline()) {
            return null;
        }
        client = HttpClients.createDefault();
        login(mail, passWord);

        final String uri = getBasicLinkForQuery(serverId) + pageQuery(PageParameter.OVERVIEW.getPageParameter());
        HttpGet get = new HttpGet(uri);

        final Player player = new OnlinePlayer(getDocument(uri, get)).create(this);
        universe.addPlayer(player);
        return player;
    }

    void update(Player player, String mail, String passWord) throws IOException {
        Condition.check().nonNull(player, mail, passWord).notEmpty(mail, passWord);
        if (!Online.isOnline()) {
            return;
        }
        client = HttpClients.createDefault();

        login(mail, passWord);

        final String uri = getBasicLinkForQuery(serverId) + pageQuery(PageParameter.OVERVIEW.getPageParameter());
        final Document document = getDocument(uri);
        new OnlinePlayer(document).update(this, player);
    }

    void logOut() {
        try {
            HttpGet get = new HttpGet(getPageQuery(PageParameter.LOGOUT));
            this.client.execute(get);
            client.close();
            loggedIn = false;
        } catch (IOException ignored) {
        }
    }

    void update(Player player) throws IOException, AccountException {
        Condition.check().nonNull(player);

        if (!Online.isOnline()) {
            return;
        }

        if (loggedIn) {
            final String uri = getBasicLinkForQuery(serverId) + pageQuery(PageParameter.OVERVIEW.getPageParameter());
            final Document document = getDocument(uri);
            new OnlinePlayer(document).update(this, player);
        } else {
            throw new AccountException();
        }
    }

    Document getDocument(String query, HttpUriRequest request) throws IOException {
        Document document;
        try (CloseableHttpResponse response = client.execute(request)) {
            checkResponse(response);
            final HttpEntity entity = response.getEntity();

            document = Jsoup.parse(entity.getContent(), "utf-8", query);
            EntityUtils.consume(entity);
        }
        return document;
    }

    Document getDocument(String query) throws IOException {
        HttpGet get = new HttpGet(query);

        Document document;
        try (CloseableHttpResponse response = client.execute(get)) {
            checkResponse(response);
            final HttpEntity entity = response.getEntity();

            document = Jsoup.parse(entity.getContent(), "utf-8", query);
            EntityUtils.consume(entity);
        }
        return document;
    }

    Document getDocument(PageParameter pageParameter) throws IOException {
        final String query = getPageQuery(pageParameter);
        HttpGet get = new HttpGet(query);

        Document document;
        try (CloseableHttpResponse response = client.execute(get)) {
            checkResponse(response);
            final HttpEntity entity = response.getEntity();

            document = Jsoup.parse(entity.getContent(), "utf-8", query);
            EntityUtils.consume(entity);
        }
        return document;
    }

    String getPageQuery(PageParameter pageParameter) {
        return getBasicLinkForQuery(serverId) + pageQuery(pageParameter.getPageParameter());
    }

    Universe getUniverse() {
        return universe;
    }

    private void login(String mail, String password) throws IOException {
        HttpPost post = new HttpPost("https://de.ogame.gameforge.com/main/login");

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("login", mail));
        nvps.add(new BasicNameValuePair("pass", password));
        nvps.add(new BasicNameValuePair("uni", "s" + this.serverId + "-de.ogame.gameforge.com"));
        nvps.add(new BasicNameValuePair("kid", ""));

        post.setEntity(new UrlEncodedFormEntity(nvps));

        try (CloseableHttpResponse response = client.execute(post)) {
            checkResponse(response);
            loggedIn = true;
        }
    }

    private void checkResponse(CloseableHttpResponse response) throws IOException {
        final int statusCode = response.getStatusLine().getStatusCode();

        if (200 > statusCode || statusCode > 399) {
            throw new IOException("illegal statusCode");
        }
    }

    private String pageQuery(String parameter) {
        return "page=" + parameter;
    }

    private String getBasicLinkForQuery(int serverNumber) {
        return "https://s" + serverNumber + "-de.ogame.gameforge.com/game/index.php?";
    }
}
