package mx.com.wcontact.poderjudicial.opensearch.bl;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.DeleteResponse;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class WCOpenSearchClient<T> extends AbstractOpenSearchClient {

    private static final Logger log = Logger.getLogger(WCOpenSearchClient.class.getName());
    protected Class<T> entityClass;

    public WCOpenSearchClient(Class<T> entityClass, String user, String pass){
        super(user,pass);
        this.entityClass = entityClass;
    }

    public void close()  {
        if(_Client != null && _Client._transport() != null){
            try {
                _Client._transport().close();
            } catch (IOException ex) {
                log.log(Level.SEVERE,ex.getMessage(), ex);
            }
        }

    }

    //String index = "pj-httpquery";
    public IndexResponse createDocument( String index, T entity, String id) throws Exception {
        //Create the index
        //CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(index).build();
        //client.indices().create(createIndexRequest);

        //Add some settings to the index
        /*IndexSettings indexSettings = new IndexSettings.Builder().autoExpandReplicas("0-all").build();
        IndexSettings settingsBody = new IndexSettings.Builder().settings(indexSettings).build();
        org.opensearch.client.opensearch.indices.PutIndicesSettingsRequest putSettingsRequest = new org.opensearch.client.opensearch.indices.PutIndicesSettingsRequest.Builder().index(index). value(settingsBody).build();
        client.indices().putSettings(putSettingsRequest);*/
        //Index some data
        IndexRequest<T> indexRequest = new IndexRequest.Builder<T>().index(index).id(id).document(entity).build();
        IndexResponse indexResponse = cleateClient().index(indexRequest);
        log.info("WCOpenSearchClient|createDocument| IndexResponse: " + indexResponse.result().toString());
        return  indexResponse;
    }



    public List<T> searchIndex( String index) throws Exception {
        //Search for the document
        SearchResponse<T> searchResponse = cleateClient().search(s -> s.index(index), entityClass);
        log.info("WCOpenSearchClient|searchIndex| maxScore: " + searchResponse.maxScore());
        List<T> list = new ArrayList<>();
        for (int i = 0; i< searchResponse.hits().hits().size(); i++) {
            //System.out.println(searchResponse.hits().hits().get(i).source());
            list.add( searchResponse.hits().hits().get(i).source() );
        }

        return list;
    }

    public DeleteResponse deleteDocument(String index, String doc) throws Exception {
        //Delete the document
        DeleteResponse response = cleateClient().delete(b -> b.index(index).id(doc));
        log.info("WCOpenSearchClient|deleteDocument| Result: " + response.result().toString());
        return response;
    }

    public void deleteIndex(final OpenSearchClient client, String index) throws IOException {
        // Delete the index
        //DeleteRequest deleteIndex = new DeleteRequest.Builder().index(index).build();
        //DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteIndex);
    }

}
