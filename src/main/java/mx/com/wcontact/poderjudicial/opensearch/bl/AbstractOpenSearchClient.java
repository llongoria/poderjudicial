package mx.com.wcontact.poderjudicial.opensearch.bl;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;

public class AbstractOpenSearchClient {

    public static final String HOSTNAME = "opensearch.wcontact.loc";
    public static final int PORT = 9200;
    public static final String SCHEME = "https";
    protected OpenSearchClient _Client;
    protected String user;
    protected String pass;

    protected AbstractOpenSearchClient(String user, String pass){
        this.user = user;
        this.pass = pass;
    }

    protected OpenSearchClient cleateClient() throws Exception {
        if(_Client == null) {
            final HttpHost host = new HttpHost(HOSTNAME, PORT, SCHEME);
            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            //Only for demo purposes. Don't specify your credentials in code.
            credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(this.user, this.pass));

            //Initialize the client with SSL and TLS enabled
            final RestClient restClient = RestClient.builder(host).
                    setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        }
                    }).build();

            final OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            _Client = new OpenSearchClient(transport);
        }
        return _Client;
    }

}
