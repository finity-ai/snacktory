package de.jetwick.snacktory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex P, (ifesdjeen from jreadability)
 * @author Peter Karich
 */
public class ArticleTextExtractorTest {

    private static final Logger logger = LoggerFactory.getLogger(ArticleTextExtractorTest.class);

    ArticleTextExtractor extractor;
    Converter c;

    @Before
    public void setup() throws Exception {
        c = new Converter();
        extractor = new ArticleTextExtractor();
    }

    @Test
    public void testData1() throws Exception {
        // ? http://www.npr.org/blogs/money/2010/10/04/130329523/how-fake-money-saved-brazil
        JResult res = extractor.extractContent(readFileAsString("test_data/1.html"));
        assertEquals("How Fake Money Saved Brazil", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("This is a story about how an economist and his buddies tricked the people of Brazil into saving the country from rampant inflation. They had a crazy, unlikely plan, and it worked. Twenty years ago, Brazil's"));
        assertTrue(res.getText(), res.getText().endsWith("\"How Four Drinking Buddies Saved Brazil.\""));
        assertEquals("http://media.npr.org/assets/img/2010/10/04/real_wide.jpg?t=1286218782&s=3", res.getImageUrl());
        assertTrue(res.getKeywords().isEmpty());
        assertEquals("Chana Joffe-Walt", res.getAuthorName());
    }

    @Test
    public void testData3() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/3.html"));
        assertTrue("data3:" + res.getText(), res.getText().startsWith("October 2010 Silicon Valley proper is mostly suburban sprawl. At first glance it "));
        assertTrue(res.getText().endsWith(" and Jessica Livingston for reading drafts of this."));
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testData5() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/5.html"));
        assertTrue("data5:" + res.getText(), res.getText().startsWith("Hackers unite in Stanford"));
//        assertTrue(res.getText().endsWith("have beats and bevvies a-plenty. RSVP here.    "));
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testData6() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/6.html"));
        assertEquals("Acting Governor of Balkh province, Atta Mohammad Noor, said that differences between leaders of the National Unity Government (NUG) – namely President Ashraf Ghani and CEO Abdullah Abdullah— have paved the ground for mounting insecurity. To watch the whole news bulletin, click here: Hundreds of worried relatives gathered outside Kabul hospitals on Tuesday desperate for news of loved ones following the deadly suicide bombing earlier in the day.", res.getText());
    }

    @Test
    public void testData7() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/7.html"));
        assertTrue("data7:" + res.getText(), res.getText().startsWith("Over 100 school girls have been poisoned in western Farah province of Afghanistan during the school hours."));
    }

    @Test
    public void testCNN() throws Exception {
        // http://edition.cnn.com/2011/WORLD/africa/04/06/libya.war/index.html?on.cnn=1
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnn.html")));
        assertEquals("Gadhafi asks Obama to end NATO bombing", res.getTitle());
        assertEquals("http://i.cdn.turner.com/cnn/2011/WORLD/africa/04/06/libya.war/tzvids.libyarebel.gi.jpg", res.getImageUrl());
        assertTrue("cnn:" + res.getText(), res.getText().startsWith("Tripoli, Libya (CNN) -- As rebel and pro-government forces in Libya maneuvered on the battlefield Wedn"));
        assertEquals("By the CNN Wire Staff", res.getAuthorName());
    }

    @Test
    public void testBBC() throws Exception {
        // http://www.bbc.co.uk/news/world-latin-america-21226565
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("bbc_noscript.html")));
        assertEquals("Brazil mourns nightclub fire dead", res.getTitle());
        assertEquals("http://news.bbcimg.co.uk/media/images/65551000/jpg/_65551014_65549948.jpg", res.getImageUrl());
        assertTrue(res.getText().startsWith("Brazil has declared three days of national mourning for 231 people killed in a nightclub fire in the southern city of Santa Maria."));
        assertEquals("Caio Quero", res.getAuthorName());
    }

    @Test
    public void testReuters() throws Exception {
        // http://www.reuters.com/article/2012/08/03/us-knightcapital-trading-technology-idUSBRE87203X20120803
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("reuters.html")));
        assertEquals("Knight trading loss shows cracks in equity markets", res.getTitle());
        assertEquals("http://s1.reutersmedia.net/resources/r/?m=02&d=20120803&t=2&i=637797752&w=130&fh=&fw=&ll=&pl=&r=CBRE872074Y00", res.getImageUrl());
        assertTrue("reuters:" + res.getText(), res.getText().startsWith("(Reuters) - The software glitch that cost Knight Capital Group $440 million in just 45 minutes reveals the deep fault lines in stock markets that are increasingly dominated by sophisticated high-speed trading systems. But Wall Street firms and regulators have few easy solutions for such problems."));
        assertEquals("Jed Horowitz and Joseph Menn", res.getAuthorName());
    }

    @Test
    public void testBBCNoCSS() throws Exception {
        // http://www.bbc.co.uk/news/magazine-21206964
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("bbc_nocss.html")));
        assertEquals("Digital artists inspired by the gif's resurgence", res.getTitle());
        assertEquals("http://ichef-1.bbci.co.uk/news/1024/media/images/65563000/jpg/_65563610_gifpromo.jpg", res.getImageUrl());
        assertTrue("bbc no css:" + res.getText(), res.getText().startsWith("They were created in the late-1980s, but recent years have seen a resurgence in popularity of gif animated files."));
    }

    @Test
    public void testCaltonCaldwell() throws Exception {
        // http://daltoncaldwell.com/dear-mark-zuckerberg (html5)
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("daltoncaldwell.html")));
        assertEquals("Dear Mark Zuckerberg by Dalton Caldwell", res.getTitle());
        assertTrue("daltoncaldwell:" + res.getText(), res.getText().startsWith("On June 13, 2012, at 4:30 p.m., I attended a meeting at Facebook HQ in Menlo Park, California."));
    }

    @Test
    public void testWordpress() throws Exception {
        // http://karussell.wordpress.com/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wordpress.html")));
        assertEquals("Twitter API and Me « Find Time for the Karussell", res.getTitle());
        assertTrue("wordpress:" + res.getText(), res.getText().startsWith("I have a love hate relationship with Twitter. As a user I see "));
    }

    @Test
    public void testFirefox() throws Exception {
        // http://www.golem.de/1104/82797.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("golem.html")));
        assertThat(res.getText(), is(notNullValue()));
        assertThat(res.getText(), startsWith("Unter dem Namen \"Aurora\" hat Firefox einen neuen Kanal mit Vorabversionen von Firefox eingerichtet."));
        assertEquals("http://www.golem.de/1104/82797-9183-i.png", res.getImageUrl());
        assertThat(res.getTitle(), equalTo("Mozilla: Vorabversionen von Firefox 5 und 6 veröffentlicht - Golem.de"));
    }

    @Test
    public void testYomiuri() throws Exception {
        // http://www.yomiuri.co.jp/e-japan/gifu/news/20110410-OYT8T00124.htm
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("yomiuri.html")));
        assertEquals("色とりどりのチューリップ", res.getTitle());
        assertTrue("yomiuri:" + res.getText(), res.getText().contains("海津市海津町の国営木曽三川公園で、チューリップが見頃を迎えている。２０日までは「チューリップ祭」が開かれており、大勢の人たちが多彩な色や形を鑑賞している＝写真＝"));
        assertEquals(Arrays.asList("読売新聞", "地域"), res.getKeywords());
    }

    @Test
    public void testFAZ() throws Exception {
        // http://www.faz.net/s/Rub469C43057F8C437CACC2DE9ED41B7950/Doc~EBA775DE7201E46E0B0C5AD9619BD56E9~ATpl~Ecommon~Scontent.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("faz.html")));
        assertTrue(res.getText(), res.getText().startsWith("Deutschland hat vor, ganz auf Atomkraft zu verzichten. Ist das eine gute"));
        assertEquals("/m/{5F104CCF-3B5A-4B4C-B83E-4774ECB29889}q225_4.jpg", res.getImageUrl());
        assertEquals("FAZ Electronic Media", res.getAuthorName());
        assertEquals(Arrays.asList("Atomkraft", "Deutschland", "Jahren", "Atommüll", "Fukushima", "Problem", "Brand", "Kohle", "2011", "11",
                "Stewart", "Atomdebatte", "Jahre", "Boden", "Treibhausgase", "April", "Welt", "Müll", "Radioaktivität",
                "Gesamtbild", "Klimawandel", "Reaktoren", "Verzicht", "Scheinheiligkeit", "Leute", "Risiken", "Löcher",
                "Fusion", "Gefahren", "Land"),
                res.getKeywords());
    }

    @Test
    public void testRian() throws Exception {
        // http://en.rian.ru/world/20110410/163458489.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("rian.html")));
        assertTrue(res.getText(), res.getText().startsWith("About 15,000 people took to the streets in Tokyo on Sunday to protest against th"));
        assertEquals("Japanese rally against nuclear power industry", res.getTitle());
        assertEquals("/favicon.ico", res.getFaviconUrl());
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testJetwick() throws Exception {
        // http://jetwick.com
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("jetwick.html")));
        assertEquals(Arrays.asList("news", "twitter", "search", "jetwick"), res.getKeywords());
    }

    @Test
    public void testVimeo() throws Exception {
        // http://vimeo.com/20910443
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("vimeo.html")));
        assertTrue(res.getText(), res.getText().startsWith("1 month ago 1 month ago: Fri, Mar 11, 2011 2:24am EST (Eastern Standard Time) See all Show me 1. finn. & Dirk von Lowtzow"));
        assertTrue(res.getTitle(), res.getTitle().startsWith("finn. & Dirk von Lowtzow \"CRYING IN THE RAIN\""));
        assertEquals("", res.getVideoUrl());
        assertEquals(Arrays.asList("finn", "finn.", "Dirk von Lowtzow", "crying in the rain", "I wish I was someone else", "Tocotronic",
                "Sunday Service", "Indigo", "Patrick Zimmer", "Patrick Zimmer aka finn.", "video", "video sharing",
                "digital cameras", "videoblog", "vidblog", "video blogging", "home video", "home movie"),
                res.getKeywords());
        assertEquals("finn.", res.getAuthorName());
    }

    /* Test broken after change to check ratio of text in getFormattedText. TODO: Find a way to support this.
    @Test
    public void testYoutube() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("youtube.html")));
        assertTrue(res.getText(), res.getText().startsWith("Master of the Puppets by Metallica. Converted to 8 bit with GSXCC. Original verson can be found us"));
        assertEquals("YouTube - Metallica - Master of the Puppets 8-bit", res.getTitle());
        assertEquals("http://i4.ytimg.com/vi/wlupmjrfaB4/default.jpg", res.getImageUrl());
        assertEquals("http://www.youtube.com/v/wlupmjrfaB4?version=3", res.getVideoUrl());
    }*/

    @Test
    public void testSpiegel() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("spiegel.html")));
        assertTrue(res.getText(), res.getText().startsWith("Da ist er wieder, der C64: Eigentlich längst ein Relikt der Technikgeschichte, soll der "));
    }

    @Test
    public void testGithub() throws Exception {
        // https://github.com/ifesdjeen/jReadability
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("github.html")));
        assertTrue(res.getDescription(), res.getDescription().startsWith("Article text extractor from given HTML text"));
        assertTrue(res.getText(), res.getText().startsWith("= jReadability This is a small helper utility (only 130 lines of code) for pepole"));
    }

    @Test
    public void testITunes() throws Exception {
        // http://itunes.apple.com/us/album/21/id420075073
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("itunes.html")));
        assertTrue(res.getText(), res.getText().startsWith("What else can be said of this album other than that it is simply amazing? Adele's voice is powerful, vulnerable, assured, and heartbreaking all in one fell swoop."));
        assertTrue("itunes:" + res.getDescription(), res.getDescription().startsWith("Preview songs from 21 by ADELE"));
    }

    @Test
    public void testTwitpic() throws Exception {
        // http://twitpic.com/4k1ku3
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitpic.html")));
        assertEquals("It’s hard to be a dinosaur. on Twitpic", res.getTitle());
//        assertEquals("", res.getText());
//        assertTrue(res.getText(), res.getText().isEmpty());
    }

    @Test
    public void testTwitpic2() throws Exception {
        // http://twitpic.com/4kuem8
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitpic2.html")));
        assertEquals("*Not* what you want to see on the fetal monitor when your wif... on Twitpic", res.getTitle());
//        assertEquals("", res.getText());
    }

    @Test
    public void testHeise() throws Exception {
        // http://www.heise.de/newsticker/meldung/Internet-Explorer-9-jetzt-mit-schnellster-JavaScript-Engine-1138062.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("heise.html")));
        assertEquals("/icons/ho/heise_min.gif", res.getImageUrl());
        assertEquals("heise online - Internet Explorer 9 jetzt mit schnellster JavaScript-Engine", res.getTitle());
        assertTrue(res.getText().startsWith("Microsoft hat heute eine siebte Platform Preview des Internet Explorer veröffentlicht. In den nur dr"));
    }

    @Test
    public void testTechcrunch() throws Exception {
        // http://techcrunch.com/2011/04/04/twitter-advanced-search/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("techcrunch.html")));
        assertEquals("http://i1.wp.com/tctechcrunch2011.files.wordpress.com/2011/04/screen-shot-2011-04-04-at-12-11-36-pm.png?resize=680%2C680", res.getImageUrl());
        assertEquals("Twitter Finally Brings Advanced Search Out Of Purgatory; Updates Discovery Algorithms", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("A couple weeks ago, we wrote a post wishing Twitter a happy fifth birthday, but also noting "));
        assertEquals("MG Siegler", res.getAuthorName());
    }
    
    @Test
    public void testEngadget() throws Exception {
        // http://www.engadget.com/2011/04/09/editorial-androids-problem-isnt-fragmentation-its-contamina/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("engadget.html")));
        assertTrue(res.getText(), res.getText().startsWith("Editorial: Android's problem isn't fragmentation, it's contamination This thought was first given voice by Myriam Joire on last night's Mobile Podcast, and the"));
        assertEquals("http://www.blogcdn.com/www.engadget.com/media/2011/04/11x0409mnbvhg_thumbnail.jpg", res.getImageUrl());
        assertEquals("Editorial: Android's problem isn't fragmentation, it's contamination -- Engadget", res.getTitle());
        // TODO: Fix author extraction.
        //assertEquals("Vlad Savov", res.getAuthorName());
    }

    @Test
    public void testTwitterblog() throws Exception {
        // http://engineering.twitter.com/2011/04/twitter-search-is-now-3x-faster_1656.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitter.html")));
        assertEquals("Twitter Engineering: Twitter Search is Now 3x Faster", res.getTitle());
        assertEquals("http://4.bp.blogspot.com/-CmXJmr9UAbA/TZy6AsT72fI/AAAAAAAAAAs/aaF5AEzC-e4/s72-c/Blender_Tsunami.jpg", res.getImageUrl());
        assertTrue("twitter:" + res.getText(), res.getText().startsWith("In the spring of 2010, the search team at Twitter started to rewrite our search engine in order to serve our ever-growin"));
    }

    @Test
    public void testTazBlog() throws Exception {
        // http://www.taz.de/1/politik/asien/artikel/1/anti-atomkraft-nein-danke/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("taz.html")));
        assertTrue("taz:" + res.getText(), res.getText().startsWith("Absolute Minderheit: Im Shiba-Park in Tokio treffen sich jetzt jeden Sonntag die Atomkraftgegner. Sie blicken neidisch auf die Anti-AKW-Bewegung in Deutschland. "));
        assertEquals("Protestkultur in Japan nach der Katastrophe: Anti-Atomkraft? Nein danke! - taz.de", res.getTitle());
        assertEquals("Georg Blume", res.getAuthorName());
    }

    @Test
    public void testFacebook() throws Exception {
        // http://www.facebook.com/ejdionne/posts/10150154175658687
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("facebook.html")));
        assertTrue(res.getText(), res.getText().startsWith("In my column tomorrow, I urge President Obama to end the spectacle of"));
        assertEquals("http://profile.ak.fbcdn.net/hprofile-ak-snc4/41782_97057113686_1357_q.jpg", res.getImageUrl());
        assertEquals("In my column...", res.getTitle());
    }

    @Test
    public void testFacebook2() throws Exception {
        // http://www.facebook.com/permalink.php?story_fbid=214289195249322&id=101149616624415 
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("facebook2.html")));
        assertTrue(res.getText(), res.getText().startsWith("Sommer is the best time to wear Jetwick T-Shirts!"));
        assertEquals("http://profile.ak.fbcdn.net/hprofile-ak-snc4/41800_101149616624415_7882236_q.jpg", res.getImageUrl());
        assertEquals("Sommer is the best...", res.getTitle());
    }

    @Test
    public void testBlogger() throws Exception {
        // http://blog.talawah.net/2011/04/gavin-king-unviels-red-hats-top-secret.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("blogger.html")));
        assertTrue(res.getText(), res.getText().startsWith("Gavin King of Red Hat/Hibernate/Seam fame recently"));
        assertEquals("//3.bp.blogspot.com/-cyMzveP3IvQ/TaR7f3qkYmI/AAAAAAAAAIk/mrChE-G0b5c/w1200-h630-p-k-no-nu/Java.png", res.getImageUrl());
        assertEquals("Gavin King unveils Red Hat's Java <strike>killer</strike> successor: The Ceylon Project", res.getTitle());
        assertEquals("https://blog.talawah.net/feeds/posts/default?alt=rss", res.getRssUrl());
        // this now fails on new 2020 version of blogger
        assertEquals("View my complete profile", res.getAuthorName());
    }

    @Test
    public void testNyt() throws Exception {
        // http://dealbook.nytimes.com/2011/04/11/for-defense-in-galleon-trial-no-time-to-rest/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("nyt.html")));
        assertEquals("http://graphics8.nytimes.com/images/blogs_v5/../icons/t_logo_291_black.png",
                res.getImageUrl());
        assertTrue(res.getText(), res.getText().startsWith("I wouldn’t want to be Raj Rajaratnam’s lawyer right now."));
        assertEquals("Andrew Ross Sorkin", res.getAuthorName());
    }

    @Test
    public void testHuffingtonpost2020() throws Exception {
        // https://www.huffpost.com/entry/coronavirus-singapore-response_n_5e97cf3cc5b65eae709f1370
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("huffingtonpost2020.html")));
        assertEquals("I Was Exposed To Coronavirus In Singapore. Here's What Their Rapid Response Looks Like.", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("SINGAPORE — At around 4 p.m. on March 26, a Thursday, I got a text message from the yoga"));
        assertEquals("https://img.huffingtonpost.com/asset/5e97d1a1250000b21f6b7d8f.jpeg?cache=1kc6rxpro3&ops=1778_1000", res.getImageUrl());
        assertEquals("dmosbergen", res.getAuthorName());
    }

    @Test
    public void testTechcrunch2() throws Exception {
        // http://techcrunch.com/2010/08/13/gantto-takes-on-microsoft-project-with-web-based-project-management-application/
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("techcrunch2.html")));
        assertEquals("Gantto Takes On Microsoft Project With Web-Based Project Management Application", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Y Combinator-backed Gantto is launching"));
        assertEquals("http://i0.wp.com/tctechcrunch2011.files.wordpress.com/2010/08/gantto.jpg?resize=680%2C680", article.getImageUrl());
        assertEquals("Leena Rao", article.getAuthorName());
    }

    @Test
    public void testCnn2() throws Exception {
        // http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnn2.html")));
        assertEquals("Democrats to use Social Security against GOP this fall", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Washington (CNN) -- Democrats pledged "));
        assertEquals(article.getImageUrl(), "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/tzvids.kaine.gi.jpg");
        assertEquals("Ed Hornick", article.getAuthorName());
    }
    
    @Test
    public void testHealthcareitnews() throws Exception {
        // http://www.healthcareitnews.com/news/kansas-hie-share-data-cdc-system-population-health-monitoring-goal
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("healthcareitnews.html")));
        assertEquals("Kansas HIE to share data with CDC, population health tracking the goal", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Officials at the Kansas Health Information Network (KHIN)"));
        assertEquals(article.getImageUrl(), "");
    }

    @Test
    public void testBusinessweek2() throws Exception {
        // http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("businessweek2.html")));
        assertTrue(article.getText(), article.getText().startsWith("There's discord on Wall Street: Strategists at major American investment "));
        assertEquals("http://images.businessweek.com/mz/covers/current_120x160.jpg", article.getImageUrl());
        assertEquals("Whitney Kisling,Caroline Dye", article.getAuthorName());
    }

    @Test
    public void testFoxnews() throws Exception {
        // http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("foxnews.html")));
        assertTrue("Foxnews:" + article.getText(), article.getText().startsWith("Apr. 8: President Obama signs the New START treaty with Russian President Dmitry Medvedev at the Prague Castle. Russia's announcement "));
        assertEquals("http://a57.foxnews.com/static/managed/img/Politics/60/60/startsign.jpg", article.getImageUrl());
        assertEquals("", article.getAuthorName());
    }

    @Test
    public void testStackoverflow() throws Exception {
        // http://stackoverflow.com/questions/3553693/wicket-vs-vaadin/3660938
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("stackoverflow.html")));
        assertTrue("stackoverflow:" + article.getText(), article.getText().startsWith("I think I've invested some time for both frameworks. I really like bo"));
        assertEquals("java - wicket vs Vaadin - Stack Overflow", article.getTitle());
        assertEquals("", article.getImageUrl());
    }

    @Test
    public void testAolnews() throws Exception {
        // http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("aolnews.html")));
        assertEquals("http://o.aolcdn.com/art/ch_news/aol_favicon.ico", article.getFaviconUrl());
        assertTrue(article.getText(), article.getText().startsWith("WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps"));
        assertEquals("http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG", article.getImageUrl());
        assertEquals(Arrays.asList("news", "update", "breaking", "nation", "U.S.", "elections", "world", "entertainment", "sports", "business",
                "weird news", "health", "science", "latest news articles", "breaking news", "current news", "top news"),
                article.getKeywords());
    }

    @Test
    public void testWallstreetjournal() throws Exception {
        // http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wsj.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Obama administration has paid out less than a third of the nearly $230 billion"));
        assertEquals("http://s.wsj.net/public/resources/images/OB-JO759_0814st_A_20100814143158.jpg", article.getImageUrl());
        assertEquals("LOUISE RADNOFSKY", article.getAuthorName());
    }

    @Test
    public void testUsatoday() throws Exception {
        // http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("usatoday.html")));
        assertTrue(article.getText(), article.getText().startsWith("Brett Favre couldn't get away from the"));
        assertEquals("http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg", article.getImageUrl());
        assertEquals("Sean Leahy", article.getAuthorName());
    }

    @Test
    public void testUsatoday2() throws Exception {
        // http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("usatoday2.html")));
        assertTrue(article.getText(), article.getText().startsWith("General Motors just filed with the Securities and Exchange "));
        assertEquals("http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg", article.getImageUrl());
    }

    @Test
    public void testEspn() throws Exception {
        // http://sports.espn.go.com/espn/commentary/news/story?id=5461430
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("espn.html")));
        assertTrue(article.getText(), article.getText().startsWith("If you believe what college football coaches have said about sports"));
        assertEquals("http://a.espncdn.com/photo/2010/0813/pg2_g_bush3x_300.jpg", article.getImageUrl());
    }

    @Test
    public void testGizmodo() throws Exception {
        // http://www.gizmodo.com.au/2010/08/xbox-kinect-gets-its-fight-club/
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("gizmodo.html")));
        assertTrue(article.getText(), article.getText().startsWith("You love to punch your arms through the air"));
        assertEquals("http://cache.gawkerassets.com/assets/images/9/2010/08/500x_fighters_uncaged__screenshot_4b__rider.jpg", article.getImageUrl());
        // author tested in juicer.
    }

    @Test
    public void testEngadget2() throws Exception {
        // http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("engadget2.html")));
        assertEquals("Verizon FiOS set-top boxes getting a new HD guide, external storage and more in Q4 -- Engadget", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Streaming and downloading TV content to mobiles is nice"));
        assertEquals("http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600_thumbnail.jpg", article.getImageUrl());
    }

    @Test
    public void testWired() throws Exception {
        // http://www.wired.com/playbook/2010/08/stress-hormones-boxing/
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wired.html")));
        assertTrue(article.getText(), article.getText().startsWith("On November 25, 1980, professional boxing"));
        assertEquals("» Stress Hormones Could Predict Boxing Dominance", article.getTitle());
        assertEquals("http://www.wired.com/playbook/wp-content/uploads/2010/08/fight_f-660x441.jpg", article.getImageUrl());
        assertEquals("Brian Mossop", article.getAuthorName());
    }

    @Test
    public void tetGigaohm() throws Exception {
        //String url = "http://gigaom.com/apple/apples-next-macbook-an-800-mac-for-the-masses/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("gigaom.html")));
        assertTrue(article.getText(), article.getText().startsWith("The MacBook Air is a bold move forward "));
        assertEquals("http://gigapple.files.wordpress.com/2010/10/macbook-feature.png?w=604", article.getImageUrl());
    }

    @Test
    public void testMashable() throws Exception {
        //String url = "http://mashable.com/2010/08/18/how-tonot-to-ask-someone-out-online/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("mashable.html")));
        assertTrue(article.getText(), article.getText().startsWith("Imagine, if you will, a crowded dance floor"));
        assertEquals("http://9.mshcdn.com/wp-content/uploads/2010/07/love.jpg", article.getImageUrl());
    }

    @Test
    public void testVenturebeat() throws Exception {
        //String url = "http://social.venturebeat.com/2010/08/18/facebook-reveals-the-details-behind-places/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("venturebeat.html")));
        assertTrue(article.getText(), article.getText().startsWith("Facebook just confirmed the rumors"));
        assertEquals("http://cdn.venturebeat.com/wp-content/uploads/2010/08/mark-zuckerberg-facebook-places.jpg", article.getImageUrl());
    }

    @Test
    public void testPolitico() throws Exception {
        //String url = "http://www.politico.com/news/stories/1010/43352.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("politico.html")));
        assertTrue(article.getText(), article.getText().startsWith("If the newest Census Bureau estimates stay close to form"));
        assertEquals("http://images.politico.com/global/news/100927_obama22_ap_328.jpg", article.getImageUrl());
    }

    @Test
    public void testNinjablog() throws Exception {
        //String url = "http://www.ninjatraderblog.com/im/2010/10/seo-marketing-facts-about-google-instant-and-ranking-your-website/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("ninjatraderblog.html")));
        assertTrue(article.getText(), article.getText().startsWith("Many users around the world Google their queries"));
    }

    @Test
    public void testSportsillustrated() throws Exception {
        //String url = "http://sportsillustrated.cnn.com/2010/football/ncaa/10/15/ohio-state-holmes.ap/index.html?xid=si_ncaaf";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sportsillustrated.html")));
        assertTrue(article.getText(), article.getText().startsWith("COLUMBUS, Ohio (AP) -- Ohio State has closed"));
        assertEquals("http://i.cdn.turner.com/si/.e1d/img/4.0/global/logos/si_100x100.jpg",
              article.getImageUrl());
    }

    @Test public void testDailybeast() throws Exception {
        //String url = "http://www.thedailybeast.com/blogs-and-stories/2010-11-01/ted-sorensen-speechwriter-behind-jfks-best-jokes/?cid=topic:featured1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thedailybeast.html")));
        assertTrue(article.getText(), article.getText().startsWith("Legendary Kennedy speechwriter Ted Sorensen passed"));
        assertEquals("http://www.tdbimg.com/resizeimage/YTo0OntzOjM6ImltZyI7czo2MToiMjAxMC8xMS8wMS9pbWctYnMtYm90dG9tLS0ta2F0ei10ZWQtc29yZW5zZW5fMTYzMjI4NjEwMzUxLmpwZyI7czo1OiJ3aWR0aCI7aTo1MDtzOjY6ImhlaWdodCI7aTo1MDtzOjY6InJhbmRvbSI7czoxOiIxIjt9.jpg",
                article.getImageUrl());
    }

    @Test
    public void testScience() throws Exception {
        //String url = "http://news.sciencemag.org/sciencenow/2011/04/early-birds-smelled-good.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sciencemag.html")));
        assertTrue(article.getText(), article.getText().startsWith("About 65 million years ago, most of the dinosaurs and many other animals and plants were wiped off Earth, probably due to an asteroid hitting our planet. Researchers have long debated how and why some "));
    }

    @Test
    public void testSlamMagazine() throws Exception {
        //String url = "http://www.slamonline.com/online/nba/2010/10/nba-schoolyard-rankings/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("slamonline.html")));
        assertTrue(article.getText(), article.getText().startsWith("When in doubt, rank players and add your findings"));
        assertEquals(article.getImageUrl(), "http://www.slamonline.com/online/wp-content/uploads/2010/10/celtics.jpg");
        assertEquals("SLAM ONLINE | » NBA Schoolyard Rankings", article.getTitle());
    }

    @Test
    public void testEspn3WithFlashVideo() throws Exception {
        //String url = "http://sports.espn.go.com/nfl/news/story?id=5971053";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("espn3.html")));
        assertTrue(article.getText(), article.getText().startsWith("PHILADELPHIA -- Michael Vick missed practice Thursday"));
        assertEquals("http://a.espncdn.com/i/espn/espn_logos/espn_red.png", article.getImageUrl());
        assertEquals("Michael Vick of Philadelphia Eagles misses practice, unlikely to play vs. Dallas Cowboys - ESPN", article.getTitle());
    }

    @Test
    public void testSportingNews() throws Exception {
        //String url = "http://www.sportingnews.com/nfl/feed/2011-01/nfl-coaches/story/raiders-cut-ties-with-cable";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sportingnews.html")));
        assertTrue(article.getText(), article.getText().startsWith("ALAMEDA, Calif. — The Oakland Raiders informed coach Tom Cable on Tuesday that they will not bring him back"));
        assertEquals("http://dy.snimg.com/story-image/0/69/174475/14072-650-366.jpg",
                article.getImageUrl());
        assertEquals("Raiders cut ties with Cable", article.getTitle());
    }

    @Test
    public void testFoxSports() throws Exception {
        //String url = "http://msn.foxsports.com/nfl/story/Tom-Cable-fired-contract-option-Oakland-Raiders-coach-010411";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("foxsports.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Oakland Raiders informed coach Tom Cable"));
        assertEquals("Oakland Raiders won't bring Tom Cable back as coach - NFL News",
                article.getTitle());
    }

    @Test
    public void testEconomist() throws Exception {
        //String url = "http://www.economist.com/node/17956885";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("economist.html")));
        assertTrue(article.getText(), article.getText().startsWith("FOR beleaguered smokers, the world is an increasingly"));
        assertEquals("http://www.economist.com/sites/default/files/images/articles/migrated/20110122_stp004.jpg",
              article.getImageUrl());
        assertFalse(article.getText(), article.getText().contains("Related topics"));
    }

    @Test
    public void testTheVacationGals() throws Exception {
        //String url = "http://thevacationgals.com/vacation-rental-homes-are-a-family-reunion-necessity/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thevacationgals.html")));
        assertTrue(article.getText(), article.getText().startsWith("Editors’ Note: We are huge proponents of vacation rental homes"));
        assertEquals("http://thevacationgals.com/wp-content/uploads/2010/11/Gemmel-Family-Reunion-at-a-Vacation-Rental-Home1-300x225.jpg",
              article.getImageUrl());
    }

    @Test
    public void testShockYa() throws Exception {
        //String url = "http://www.shockya.com/news/2011/01/30/daily-shock-jonathan-knight-of-new-kids-on-the-block-publicly-reveals-hes-gay/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("shockya.html")));
        assertTrue(article.getText(), article.getText().startsWith("New Kids On The Block singer Jonathan Knight has publicly"));
        assertEquals("http://www.shockya.com/news/wp-content/uploads/jonathan_knight_new_kids_gay.jpg",
                article.getImageUrl());
    }

    @Test
    public void testWikipedia() throws Exception {
        // String url = "http://en.wikipedia.org/wiki/Therapsids";
        // Wikipedia has the advantage of also testing protocol relative URL extraction for Favicon and Images.
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia.html")));
        assertTrue(article.getText(), article.getText().startsWith("Therapsida is a group of the most advanced reptile-grade synapsids, and the ancestors of mammals"));
        assertEquals("//upload.wikimedia.org/wikipedia/commons/thumb/4/42/Pristeroognathus_DB.jpg/240px-Pristeroognathus_DB.jpg",
              article.getImageUrl());
        assertEquals("//en.wikipedia.org/apple-touch-icon.png",
                article.getFaviconUrl());
    }

    @Test
    public void testWikipedia2() throws Exception {
        // http://en.wikipedia.org/wiki/President_of_the_United_States
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia_president.html")));
        assertTrue(article.getText(), article.getText().startsWith("The President of the United States of America (acronym: POTUS)[6] is the head of state and head of government"));
    }

    @Test
    public void testWikipedia3() throws Exception {
        // http://en.wikipedia.org/wiki/Muhammad
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia_muhammad.html")));
        assertTrue(article.getText(), article.getText().startsWith("Muhammad (c. 570 – c. 8 June 632);[1] also transliterated as Mohammad, Mohammed, or Muhammed; Arabic: محمد‎, full name: Abū al-Qāsim Muḥammad"));
    }

    @Test
    public void testWikipedia4() throws Exception {
        // http://de.wikipedia.org/wiki/Henne_Strand
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia_Henne_Strand.html")));
        assertTrue(article.getText(), article.getText().startsWith("Der dänische Ort Henne Strand befindet sich in Südwest-Jütland und gehört zur Kommune Varde"));
    }

    @Test
    public void testWikipedia5() throws Exception {
        // http://de.wikipedia.org/wiki/Java
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia_java.html")));
        assertTrue(article.getText(), article.getText().startsWith("Java (Indonesian: Jawa) is an island of Indonesia. With a population of 135 million"));
    }

    @Test
    public void testWikipedia6() throws Exception {
        // http://de.wikipedia.org/wiki/Knight_Rider
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia-knight_rider_de.html")));
        assertTrue(article.getText(), article.getText().startsWith("Knight Rider ist eine US-amerikanische Fernsehserie, "
                + "die von 1982 bis 1986 produziert wurde. Knight Rider ist eine Krimi-Action-Serie mit futuristischen Komponenten "
                + "und hat weltweit Kultstatus erlangt."));
    }

    @Test
    public void testData4() throws Exception {
        // http://blog.traindom.com/places-where-to-submit-your-startup-for-coverage/
        JResult res = extractor.extractContent(readFileAsString("test_data/4.html"));
        assertEquals("36 places where you can submit your startup for some coverage", res.getTitle());
        assertEquals(Arrays.asList("blog coverage", "get coverage", "startup review", "startups", "submit startup"), res.getKeywords());
        assertTrue("data4:" + res.getText(), res.getText().startsWith("So you have a new startup company and want some coverage"));
    }

    @Test
    public void testTimemagazine() throws Exception {
        //String url = "http://www.time.com/time/health/article/0,8599,2011497,00.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("time.html")));
        assertTrue(article.getText(), article.getText().startsWith("This month, the federal government released"));
        assertEquals("http://img.timeinc.net/time/daily/2010/1008/bp_oil_spill_0817.jpg", article.getImageUrl());
    }

    @Test
    public void testCnet() throws Exception {
        //String url = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnet.html")));
        assertTrue(article.getText(), article.getText().startsWith("NEW YORK--Verizon Communications is prepping a new"));
        assertEquals("http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_with_big_TV_60x60.JPG", article.getImageUrl());
    }

    @Test
    public void testBloomberg() throws Exception {
        //String url = "http://www.bloomberg.com/news/2010-11-01/china-becomes-boss-in-peru-on-50-billion-mountain-bought-for-810-million.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("bloomberg.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Chinese entrepreneur and the Peruvian shopkeeper"));
        assertEquals("http://www.bloomberg.com/apps/data?pid=avimage&iid=iimODmqjtcQU", article.getImageUrl());
    }

    @Test
    public void testTheFrisky() throws Exception {
        //String url = "http://www.thefrisky.com/post/246-rachel-dratch-met-her-baby-daddy-in-a-bar/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thefrisky.html")));
        assertTrue(article.getText(), article.getText().startsWith("Rachel Dratch had been keeping the identity of her baby daddy "));

        assertEquals("http://cdn.thefrisky.com/images/uploads/rachel_dratch_102810_t.jpg",
              article.getImageUrl());
        assertEquals("Rachel Dratch Met Her Baby Daddy At A Bar", article.getTitle());
    }

    @Test
    public void testBrOnline() throws Exception {
        // TODO charset for opera was removed:
        // <![endif]-->
        // <link rel="stylesheet" type="text/x-opera-css;charset=utf-8" href="/css/opera.css" />

        //String url = "http://www.br-online.de/br-klassik/programmtipps/highlight-bayreuth-tannhaeuser-festspielzeit-2011-ID1309895438808.xml";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("br-online.html")));
        assertTrue(article.getText(), article.getText().startsWith("Wenn ein Dirigent, der Alte Musik liebt, erstmals eine "
              + "Neuproduktion bei den Bayreuther Richard-Wagner-Festspielen übernimmt,"));
        assertEquals("Eröffnung der 100. Bayreuther Festspiele: Alles neu beim \"Tannhäuser\" | Programmtipps | BR-KLASSIK",
                article.getTitle());
    }

    @Test
    public void cleanTitle() {
        String title = "Hacker News | Ask HN: Apart from Hacker News, what else you read?";
        assertEquals("Ask HN: Apart from Hacker News, what else you read?", extractor.cleanTitle(title));
        assertEquals("mytitle irgendwas", extractor.cleanTitle("mytitle irgendwas | Facebook"));
        assertEquals("mytitle irgendwas", extractor.cleanTitle("mytitle irgendwas | Irgendwas"));

        // this should fail as most sites do store their name after the post
        assertEquals("Irgendwas | mytitle irgendwas", extractor.cleanTitle("Irgendwas | mytitle irgendwas"));
    }

    @Test
    public void testGaltimeWhereUrlContainsSpaces() throws Exception {
        //String url = "http://galtime.com/article/entertainment/37/22938/kris-humphries-avoids-kim-talk-gma";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("galtime.com.html")));
        assertEquals("http://vnetcdn.dtsph.com/files/vnet3/imagecache/opengraph_ogimage/story-images/Kris%20Humphries%20Top%20Bar.JPG", article.getImageUrl());
    }

    @Test
    public void testIssue8() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("no-hidden.html")));
        assertEquals("This is the text which is shorter but visible", res.getText());
    }

    @Test
    public void testIssue8False() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("no-hidden2.html")));
        assertEquals("This is the NONE-HIDDEN text which shouldn't be shown and it is a bit longer so normally prefered", res.getText());
    }

    @Test
    public void testIssue4() throws Exception {
        JResult res = extractor.extractContent("<html><body><div> aaa<a> bbb </a>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());

        res = extractor.extractContent("<html><body><div> aaa <strong>bbb </strong>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());

        res = extractor.extractContent("<html><body><div> aaa <strong> bbb </strong>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());
    }

    @Test
    public void testI4Online() throws Exception {
        //https://i4online.com
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("i4online.html")));
        assertTrue(article.getText(), article.getText().startsWith("Just one week to go and everything is set for the summer Forum 2013"));

        ArticleTextExtractor extractor2 = new ArticleTextExtractor();
        OutputFormatter outputFormater = new OutputFormatter(10);
        outputFormater.setNodesToKeepCssSelector("p,h1,h2,h3,h4,h5,h6");
        extractor2.setOutputFormatter(outputFormater);
        article = extractor2.extractContent(c.streamToString(getClass().getResourceAsStream("i4online.html")));
        assertTrue(article.getText(), article.getText().startsWith("Upcoming events: Forum 79 Just one week to go and everything is set for the summer Forum 2013"));
    }

    @Test
    public void testImagesList() throws Exception {
        // http://www.reuters.com/article/2012/08/03/us-knightcapital-trading-technology-idUSBRE87203X20120803
        // manually tweaked to remove og:image and image_src tags
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("reuters_list.html")));
        assertEquals(1, res.getImagesCount());
        assertEquals(res.getImageUrl(), res.getImages().get(0).src);
        assertEquals("http://s1.reutersmedia.net/resources/r/?m=02&d=20120803&t=2&i=637797752&w=460&fh=&fw=&ll=&pl=&r=CBRE872074Y00",
                res.getImages().get(0).src);

        // http://thevacationgals.com/vacation-rental-homes-are-a-family-reunion-necessity/
        res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thevacationgals.html")));
        assertEquals(3, res.getImagesCount());
        assertEquals("http://thevacationgals.com/wp-content/uploads/2010/11/Gemmel-Family-Reunion-at-a-Vacation-Rental-Home1-300x225.jpg",
              res.getImages().get(0).src);
        assertEquals("../wp-content/uploads/2010/11/The-Gemmel-Family-Does-a-Gilligans-Island-Theme-Family-Reunion-Vacation-Sarah-Gemmel-300x225.jpg",
                res.getImages().get(1).src);
        assertEquals("http://www.linkwithin.com/pixel.png", res.getImages().get(2).src);
    }

    /**
     * Verify that all images are extracted, even if they have all the weights exactly {@code 0} (no dimensions, no title, no alt, e.g. {@code <img src="first-image.jpg"/>}).
     */
    @Test
    public void testImagesZeroWeight() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("artificial/zero-weight-images.html")));
        final List<ImageResult> images = res.getImages();
        assertEquals(2, images.size());
        assertEquals("first-image.jpg", images.get(0).src);
        assertEquals("second-image.jpg", images.get(1).src);
    }

    @Test
    public void testTextList() throws Exception {
        final JResult res = extractor.extractContent(readFileAsString("test_data/1.html"));
        final String text = res.getText();
        final List<String> textList = res.getTextList();
        assertEquals(25, textList.size());
        assertTrue(textList.get(0).startsWith(text.substring(0, 15)));
        assertEquals(textList.get(24), text.substring(text.length() - 87, text.length()));
    }

    @Test
    public void testMurraySenateGov() throws Exception {
        // Test http://www.murray.senate.gov/public/index.cfm/newsreleases?ContentRecord_id=28da79eb-bca4-421e-9358-cec1c064def0
        // Was not fully extracted by version 1.2.3
        final JResult res = extractor.extractContent(readFileAsString("test_data/murray_senate_gov.html"));
        final String text = res.getText();
        // logger.info("text: " + text);
        final List<String> textList = res.getTextList();
        // logger.info("textList:\n-" + StringUtils.join(textList, "\n-"));
        assertEquals(4, textList.size());
        assertTrue(textList.get(0).startsWith(text.substring(0, 15)));
        assertEquals(textList.get(3), text.substring(text.length() - 472, text.length()));
    }

    @Test
    public void testLeFigaroSport() throws Exception {
        // Test http://sport24.lefigaro.fr/football/coupe-du-monde/2014-bresil/fil-info/ronaldo-ecourte-l-entrainement-700221
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("lefigaro.html")));
        final String text = res.getText();
        assertThat(text, startsWith("Cristiano Ronaldo a quitté l’entraînement de la sélection portugaise plus tôt que ses coéquipiers ce mercredi. L’attaquant du Real Madrid a rejoint les vestiaires avec une poche de glace sur un genou, comme il l’a déjà fait à plusieurs reprises depuis son arrivée au Brésil."));
        final List<String> textList = res.getTextList();
        assertEquals(2, textList.size());
        assertTrue(textList.get(0).startsWith(text.substring(0, 15)));
        assertTrue(textList.get(1).endsWith(text.substring(text.length() - 15, text.length())));
    }

    @Test
    public void testSearchEngineJournal() throws Exception {
        // http://www.searchenginejournal.com/planning-progress-18-tips-successful-social-media-strategy/112567/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("searchenginejournal.html")));
        assertEquals("18 Tips for a Successful Social Media Strategy | SEJ", res.getTitle());
        assertTrue(res.getText(), res.getText().contains("Sharam"));
    }

    @Test
    public void testAdweek() throws Exception {
        // http://www.adweek.com/prnewser/5-digital-data-metricstools-that-pr-pros-need-to-know/97735?red=pr
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("adweek.html")));
        assertEquals("5 Digital Metrics/Tools That PR Pros Need to Know", res.getTitle());
        assertTrue(res.getText(), res.getText().contains("Cision provides a proprietary"));
        assertTrue(res.getText(), res.getText().contains("Moz’s Domain Authority."));
        assertTrue(res.getText(), res.getText().contains("Google Authorship and Google Analytics."));
    }

    @Test
    public void testSpinsucks() throws Exception {
        // http://spinsucks.com/communication/2015-communications-trends/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("spinsucks.html")));
        assertEquals("The Five Communications Trends for 2015", res.getTitle());
        assertTrue(res.getText(), res.getText().contains("Conversion of media. Julie Hong, the community manager"));
        assertTrue(res.getText(), res.getText().contains("Paid media affects traditional PR"));
        assertTrue(res.getText(), res.getText().contains("Old ideas become new again. We’ve stopped doing things such as deskside briefings, large events, direct mail"));
    }

    @Test
    public void testPRDaily() throws Exception {
        // http://www.prdaily.com/Main/Articles/7_PR_blogs_worth_reading_17870.aspx
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("prdaily.html")));
        assertEquals("7 PR blogs worth reading", res.getTitle());
        assertTrue(res.getText(), res.getText().contains("I really love the way Rebekah"));
        assertTrue(res.getText(), res.getText().contains("my team became fascinated with Bad Pitch Blog"));
        assertTrue(res.getText(), res.getText().contains("I’m fairly certain there is no one nicer than Deirdre Breakenridge"));
    }

    @Test
    public void testMarthaStewartWeddings() throws Exception {
        // http://www.marthastewartweddings.com/363473/bridal-beauty-diaries-lauren-%25E2%2580%2593-toning-and-cutting-down
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("marthastewartweddings.html")));
        assertEquals("Bridal Beauty Diaries: Lauren – Toning Up and Cutting Down", res.getTitle());
        assertTrue(res.getText(), res.getText().contains("Its “go” time. Approximately seven months until the big day"));
    }

    @Test
    public void testNotebookCheck() throws Exception {
        // http://www.notebookcheck.com/UEbernahme-Microsoft-schluckt-Devices-und-Services-Sparte-von-Nokia.115522.0.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("notebookcheck.html")));
        assertEquals("Übernahme: Microsoft schluckt Devices und Services Sparte von Nokia", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Tschüss Nokia. Willkommen Microsoft. Die Übernahmen ist unter Dach und Fach"));
    }

    @Test
    public void testPeople() throws Exception {
        // http://www.people.com/article/ryan-seacrest-marriage-turning-40
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("people.html")));
        assertEquals("Ryan Seacrest on Marriage: 'I Want What My Mom and Dad Have'", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("There are those who are in denial about turning 40"));
        assertFalse(res.getText(), res.getText().contains("Poppy Montgomery Drama Unforgettable Is Being Brought Back"));
    }

    @Test
    public void testPeople2() throws Exception {
        // http://www.people.com/article/truck-driver-rescues-family-burning-car-video
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("people2.html")));
        assertEquals("Truck Driver Rescues Family Caught in Burning Car (VIDEO)", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("David Fredericksen was driving his semi truck along"));
        assertFalse(res.getText(), res.getText().contains("How Water Helps with Weight Loss"));
    }

    @Test
    public void testPeople3() throws Exception {
        // http://www.people.com/article/pierce-brosnan-jimmy-fallon-goldeneye-007-n64
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("people3.html")));
        assertEquals("Pierce Brosnan Loses to Jimmy Fallon in 'GoldenEye 007' (VIDEO)", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Just because you star in a video game, doesn't mean you'll be any good at it."));
        assertFalse(res.getText(), res.getText().contains("How Water Helps with Weight Loss"));
        assertEquals("Alex Heigl", res.getAuthorName());
    }

    @Test
    public void testEntrepreneur() throws Exception {
        // http://www.entrepreneur.com/article/237402
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("entrepreneur.html")));
        assertEquals("7 Big Changes in the PR Landscape Every Business Should Know About", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("At least three times a week, I get emails from entrepreneurs or small-business owners asking for advice on public relations."));
        assertEquals("Rebekah Iliff", res.getAuthorName());
        assertEquals("Chief Strategy Officer for AirPR", res.getAuthorDescription());
    }

    @Test
    public void testHuffingtonpostAuthor() throws Exception {
        // http://www.huffingtonpost.com/rebekah-iliff/millions-of-consumers-aba_b_5269051.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("huffingtonpost2.html")));
        assertEquals("Millions of Consumers Abandon Hashtag for Backslash", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("In a special Silicon Valley \"Tech Report,\" sources confirmed Monday that millions of "));
        assertEquals("Rebekah Iliff", res.getAuthorName());
        assertEquals("Chief Strategy Officer, AirPR", res.getAuthorDescription());
    }

    @Test
    public void testAllVoices() throws Exception {
        // http://www.allvoices.com/article/17660716
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("allvoices.html")));
        assertEquals("Marchex exec: Lead generation moving away from 'faceless transactions'", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Driven by the surge in mobile, lead generation"));
    }

    @Test
    public void testRocketFuel() throws Exception {
        // http://rocketfuel.com/blog/you-wont-be-seeing-coca-cola-ads-for-awhile-the-reason-why-is-amazing
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("rocketfuel.html")));
        assertEquals("You Won't be Seeing Coca Cola Ads for Awhile. The Reason why Is Amazing.", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Coca Cola announced that it will not be spending"));
    }

    @Test
    public void testHuffingtonpostAuthorDesc() throws Exception {
        // http://www.huffingtonpost.com/2015/03/10/bruce-miller-san-francisco-49ers-domestic-violence_n_6836416.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("huffingtonpost3.html")));
        assertEquals("San Francisco 49er Arrested On Domestic Violence Charges", res.getTitle());
        assertEquals("", res.getAuthorDescription());
    }

    @Test
    public void testPRnewswireAuthorDesc() throws Exception {
        // www.prnewswire.com/news-releases/tableau-to-present-at-upcoming-investor-conferences-300039248.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("prnewswire.html")));
        assertEquals("Tableau to Present at Upcoming Investor Conferences", res.getTitle());
        assertEquals("", res.getAuthorDescription());
    }

    @Test
    public void testTrendkraftAuthorDesc() throws Exception {
        // http://www.trendkraft.de/it-software/freigegeben-und-ab-sofort-verfuegbar-die-sechste-generation-des-ecm-systems-windream/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("trendkraft_de.html")));
        assertEquals("Freigegeben und ab sofort verfügbar: die sechste Generation des ECM-Systems windream", res.getTitle());
        assertTrue(res.getAuthorDescription(), res.getAuthorDescription().length() == 987);
    }

    @Test
    public void testLimitSize() throws Exception {
        // https://medium.com/@nathanbruinooge/a-travelogue-of-india-7b1f3aa62a19
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("medium.html")), 1000);
        assertEquals("A Travelogue of India", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Schipol Airport in 2012 looks nothing like Schipol Airport in the Eighties"));
        assertTrue("Should be less than 1000", res.getText().length() <= 1000);
    }

    @Test
    public void testQualcomm() throws Exception {
        // https://www.qualcomm.com/news/releases/2014/10/16/qualcomm-declares-quarterly-cash-dividend
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("qualcomm.html")));
        assertEquals("Qualcomm Declares Quarterly Cash Dividend | Qualcomm", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Qualcomm Incorporated (NASDAQ: QCOM) today announced"));
    }

    @Test
    public void testApplePR() throws Exception {
        // http://www.apple.com/pr/library/2015/04/27Apple-Expands-Capital-Return-Program-to-200-Billion.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("applepr.html")));
        assertEquals("Apple - Press Info - Apple Expands Capital Return Program to $200 Billion", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Apple Expands Capital Return Program to $200 Billion CUPERTINO, California—April 27, 2015—Apple"));
    }

    @Test
    public void testApplePR2() throws Exception {
        // www.apple.com/pr/library/2015/03/09Apple-Watch-Available-in-Nine-Countries-on-April-24.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("applepr2.html")));
        assertEquals("Apple - Press Info - Apple Watch Available in Nine Countries on April 24", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Apple Watch Available in Nine Countries on April 24"));
    }

    @Test
    public void testForbes() throws Exception {
        // http://fortune.com/2015/05/11/rackspaces-support-other-cloud/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("forbes.html")));
        assertEquals("Does Rackspace’s future lie in supporting someone else’s cloud?", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("Rackspace, a true cloud computing pioneer, is starting to sound like a company that will"));
    }

    @Test
    public void testLongImageName() throws Exception {
        // http://www.adverts.ie/lego-building-toys/lego-general-zod-minifigure-brand-new/5980084
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("adverts.ie.html")));
        assertEquals("LEGO general zod minifigure brand new", res.getTitle());
        assertTrue(res.getImageUrl(), res.getImageUrl().length() == 0);
    }

    @Test
    public void testCloudComputingExpo() throws Exception {
        // http://www.cloudcomputingexpo.com/node/3342675
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cloudcomputingexpo.html")));
        assertTrue(res.getText(), res.getText().startsWith("How to Put Public Sector Data Migration Hassles on the Road to Extinction"));
        // test it doesn't extract outside the article content
        assertFalse("Extracted text outside the content", res.getText().contains("Sandy Carter"));
        assertTrue(res.getText(), res.getText().startsWith("How to Put Public Sector Data Migration Hassles on the Road to Extinction"));
    }

    @Test
    public void testCloudComputingExpo2() throws Exception {
        // http://www.cloudcomputingexpo.com/node/3346367
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cloudcomputingexpo2.html")));
        assertTrue(res.getText(), res.getText().startsWith("Merck, a leading company for innovative, top-quality high-tech"));
        assertTrue(res.getText(), res.getText().endsWith("EMD Millipore and EMD Performance Materials."));
        // test it doesn't extract outside the article content
        assertFalse("Extracted text outside the content", res.getText().contains("Sandy Carter"));
    }

    @Test
    public void testCloudComputingExpo3() throws Exception {
        // http://www.cloudcomputingexpo.com/node/3432136
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cloudcomputingexpo3.html")));
        assertEquals("IHS to Hold Conference Call and Webcast on September 29, 2015 with Release of Third Quarter Results for Fiscal Year 2015", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("IHS Inc. (NYSE: IHS), the leading global "));
        assertTrue(res.getText(), res.getText().endsWith("http://www.businesswire.com/news/home/20150828005027/en/"));
    }

    @Test
    public void testCloudComputingExpo4() throws Exception {
        // http://www.cloudcomputingexpo.com/node/3372014
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cloudcomputingexpo4.html")));
        assertEquals("As New Cases Of Ebola Are Confirmed We Highlight The Need For Global Coordination In The Field Of Distance Education", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("LOS ANGELES, July 16, 2015 /PRNewswire-iReach/"));
        assertTrue(res.getText(), res.getText().endsWith("News distributed by PR Newswire iReach: https://ireach.prnewswire.com"));
    }

    @Test
    public void testCloudComputingExpo5() throws Exception {
        // http://www.cloudcomputingexpo.com/node/3345803
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cloudcomputingexpo5.html")));
        assertEquals("U.S. FDA Approves Eisai's Antiepileptic Agent Fycompa as Adjunctive Treatment For Primary Generalized Tonic-Clonic Seizures", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("U.S. FDA Approves Eisai's Antiepileptic Agent Fycompa as Adjunctive Treatment"));
        //assertTrue(res.getText(), res.getText().endsWith("or for any actions taken in reliance thereon"));
    }

    @Test
    public void testCanonical() throws Exception {
        // http://www.cio.com/article/2941417/internet/internet-of-things-is-overhyped-should-be-called-internet-with-things.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cio.com.html")));
        assertEquals("Internet of things is overhyped, should be called internet with things", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("The internet of things is overhyped and should instead be called the internet with things"));
        assertEquals("http://www.techworld.com/news/startups/rackspace-mongodb-execs-take-iot-hype-down-notch-3617731/", res.getCanonicalUrl());
    }

    @Test
    public void testYahooMobile() throws Exception {
        // https://m.yahoo.com/w/legobpengine/finance/news/stevia-first-corp-stvf-looks-123500390.html?.intl=us&.lang=en-us
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("m_yahoo.html")));
        assertEquals("Stevia First Corp. (STVF) Looks to Disrupt Flavor Industry", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("WHITEFISH, MT / ACCESSWIRE / July 13, 2015 / The global market for sugar and sweeteners"));
    }

    @Test
    public void testWeixin() throws Exception {
        // http://mp.weixin.qq.com/s?3rd=MzA3MDU4NTYzMw%3D%3D&__biz=MzA4MTQ0Njc2Nw%3D%3D&idx=4&mid=207614885&scene=6&sn=eda80bb13406fb31cb25f70d12e6e7dc
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("weixin.qq.com.html")));
        assertTrue(res.getTitle(), res.getTitle().startsWith("缺少IT支持成跨境电商发展阻力"));
        assertTrue(res.getText(), res.getText().startsWith("根据联合国贸发会议预计"));
    }

    @Test
    public void testNaturebox() throws Exception {
        // http://blog.naturebox.com/posts/lunch-box-idea-breakfast-for-lunch
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("naturebox.com.html")));
        assertTrue(res.getTitle(), res.getTitle().startsWith("Lunch Box Idea: Breakfast for Lunch"));
        assertTrue(res.getText(), res.getText().startsWith("I don’t know a kid who doesn’t enjoy breakfast for lunch!"));
        assertEquals("Thu Feb 19 00:00:00 UTC 2015", res.getDate().toString());
    }

    @Test
    public void testKhaleejtimesCom() throws Exception {
        // https://www.khaleejtimes.com/business/local/ai-investments-in-uae-to-touch-dh33-billion-in-2017
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("khaleejtimes_com.html")));
        assertEquals("AI investments in UAE to touch Dh33 billion in 2017", res.getTitle());
        assertEquals("https://www.khaleejtimes.com/storyimage/KT/20171001/ARTICLE/171009970/AR/0/AR-171009970.jpg&NCS_modified=&imageversion=1by1&exif=.jpg", res.getImageUrl());
    }

    @Test
    public void testnews8pluscom() throws Exception {
        // https://news8plus.com/covid-19-potential-coronavirus-treatment-granted-rare-disease-status/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("Covid-19News8Plus.html")));
        assertEquals("Covid-19: Potential coronavirus treatment granted rare disease status", res.getTitle());
    }

    @Test
    public void testthemedialine() throws Exception {
        // https://themedialine.org/top-stories/coronavirus-treatment-could-be-ready-in-matter-of-months/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("Coronavirus_The Media Line.html")));
        assertEquals("Coronavirus Treatment Could Be Ready in ‘Matter of Months’", res.getTitle());
    }

    @Test
    public void testnewsgram() throws Exception {
        // https://www.newsgram.com/anxiety-coronavirus-mental-health/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("corona_newsgram.html")));
        assertEquals("Prolonged Anxiety Due to Novel Coronavirus Harmful for Mental Health", res.getTitle());
    }

    @Test
    public void testtheislandnow() throws Exception {
        // https://theislandnow.com/great_neck/northwells-research-branch-rolls-out-three-clinical-trials-to-combat-coronavirus-spread/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("corona_theislandnow.html")));
        // we fail because <title> uses ' quotes but h1 uses ’ quotes
        assertEquals("Northwell's research branch rolls out three clinical trials to combat coronavirus spread - Great Neck News - The Island Now", res.getTitle());
    }

    @Test
    public void test247wallst() throws Exception {
        // https://247wallst.com/healthcare-business/2020/03/24/how-coronavirus-stocks-are-holding-up/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("corona_247wallst.html")));
        assertEquals("How Coronavirus Stocks Are Holding Up", res.getTitle());
    }

    @Test
    public void testvisiongain() throws Exception {
        // https://www.visiongain.com/visiongain-publishes-novel-coronavirus-covid-19-drugs-in-development-market-forecast-2020-2030-report/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("corona_Visiongain.html")));
        assertEquals("Visiongain publishes Novel Coronavirus (COVID-19) Drugs in Development Market Forecast 2020-2030 report - Visiongain", res.getTitle());
    }

    @Test
    public void testusnewsrank() throws Exception {
        // https://www.usnewsrank.com/business-news/coronavirus-live-updates-new-cases-surge-in-spain-olympics-organizers-agree-to-a-delay/
        final JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("Coronavirus_USNewsRank.com.html")));
        assertEquals("Coronavirus live updates: New cases surge in Spain; Olympics organizers agree to a delay", res.getTitle());
    }

    /**
     * @param filePath the name of the file to open. Not sure if it can accept
     * URLs or just filenames. Path handling could be better, and buffer sizes
     * are hardcoded
     */
    public static String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
