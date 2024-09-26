package mx.com.wcontact.poderjudicial.opensearch.bl;

import mx.com.wcontact.poderjudicial.entity.HttpQuery;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.DeleteResponse;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.bulk.BulkResponseItem;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpQueryOS {

    private static final Logger log = Logger.getLogger(HttpQueryOS.class.getName());

    WCOpenSearchClient<HttpQuery> wcOpenSearch;

    public HttpQueryOS() {
        wcOpenSearch = new WCOpenSearchClient<HttpQuery>(HttpQuery.class, "admin", "admin");
    }

    public void close(){
        wcOpenSearch.close();
    }

    //String index = "pj-httpquery";
    public BulkResponse createDocuments(String index, List<HttpQuery> list) throws Exception {
        //Index some data
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (HttpQuery entity : list) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(index)
                            .id( entity.getHttpQueryId().toString() )
                            .document(entity)
                    )
            );
        }

        BulkResponse result =  wcOpenSearch.cleateClient().bulk(br.build());
        // Log errors, if any
        if (result.errors()) {
            log.log(Level.SEVERE, "WCOpenSearchClient|createDocuments| Bulk had errors ");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.log(Level.SEVERE, "WCOpenSearchClient|createDocuments| Reason: " + item.error().reason());
                }
            }
        }
        return  result;
    }

    public IndexResponse createDocument(String index, HttpQuery entity) throws Exception {
        return wcOpenSearch.createDocument(index,entity,entity.getHttpQueryId().toString());
    }

    public List<HttpQuery> searchIndex( String index ) throws Exception {
        return wcOpenSearch.searchIndex(index);
    }

    public DeleteResponse deleteDocument(String index, Long id) throws Exception {
        return wcOpenSearch.deleteDocument("index", id.toString());
    }


}
