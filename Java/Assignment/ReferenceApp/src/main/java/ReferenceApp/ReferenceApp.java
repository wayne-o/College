package ReferenceApp;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.cfg.Configuration;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import java.io.IOException;


public class ReferenceApp {

    public static void main(String args[]) {

         HibernateUtils utils = new HibernateUtils();

         Configuration config = new Configuration().
            setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect").
            setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver").
            setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:RefenceDb").
            setProperty("hibernate.connection.username", "sa").
            setProperty("hibernate.connection.password", "").
            setProperty("hibernate.connection.pool_size", "1").
            setProperty("hibernate.connection.autocommit", "true").
            setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider").
            setProperty("hibernate.hbm2ddl.auto", "create-drop").
            setProperty("hibernate.show_sql", "true").
            addClass(Boy.class).


       HibernateUtil.setSessionFactory(config.buildSessionFactory());

        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.addComponent(Boy.class);
        pico.addComponent(Girl.class);

       Girl girl = (Girl) pico.getComponent(Girl.class);
        girl.kissSomeone();

        // 0. Specify the analyzer for token-izing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

        // 1. create the index
        Directory index = new RAMDirectory();


        // the boolean arg in the IndexWriter ctor means to
        // create a new index, overwriting any existing index
        IndexWriter w = null;
        try {
            w = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            addDoc(w, "Lucene in Action");
            addDoc(w, "Lucene for Dummies");
            addDoc(w, "Managing Gigabytes");
            addDoc(w, "The Art of Computer Science");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            w.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // 2. query
        String querystr = args.length > 0 ? args[0] : "kushene~0.2";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = null;
        try {
            q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // 3. search
        int hitsPerPage = 10;
        IndexSearcher searcher = null;
        try {
            searcher = new IndexSearcher(index, true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        TopScoreDocCollector collector =
                TopScoreDocCollector.create(hitsPerPage, true);
        try {
            searcher.search(q, collector);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = null;
            try {
                d = searcher.doc(docId);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println((i + 1) + ". " + d.get("title"));
        }

        // searcher can only be closed when there
        // is no need to access the documents any more.
        try {
            searcher.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private static void addDoc(IndexWriter w, String value) throws IOException {
        Document doc = new Document();
        doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }

}
