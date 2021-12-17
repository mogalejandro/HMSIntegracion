package  com.example.hmsintegracion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class Site extends AppCompatActivity {

    private SearchService searchService;
    private TextView tv2;
    private EditText queryInput;
    private Button b1;
    private final String TAG = "Site";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        String key = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/api_key");
        try {
            searchService = SearchServiceFactory.create(this, URLEncoder.encode(key, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encode apikey error");
        }

        queryInput = findViewById(R.id.busqueda);
        tv2 = findViewById(R.id.textView2);
        b1 = findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(queryInput.getText().toString().matches("")) {

                    Toast.makeText(getApplicationContext(),"debes realizar una busqueda",Toast.LENGTH_SHORT).show();
                }else{
                    search(this);
                }
            }
        });
    }

    public void search(View.OnClickListener view) {
        TextSearchRequest textSearchRequest = new TextSearchRequest();
        textSearchRequest.setQuery(queryInput.getText().toString());
        textSearchRequest.setRadius(10000);
        textSearchRequest.setCountryCode("ES");
        textSearchRequest.setLanguage("es");
        textSearchRequest.setPageIndex(1);
        textSearchRequest.setPageSize(10);
        textSearchRequest.setChildren(false);
        textSearchRequest.setCountries(Arrays.asList("en", "fr", "cn", "es", "ko"));

        searchService.textSearch(textSearchRequest, new SearchResultListener<TextSearchResponse>() {

            // Return search results upon a successful search.
            @Override
            public void onSearchResult(TextSearchResponse results) {

                StringBuilder response = new StringBuilder("\n");
                response.append("\n");
                int count = 1;

                if (results == null || results.getTotalCount() <= 0) {
                    return;
                }
                List<com.huawei.hms.site.api.model.Site> sites = results.getSites();
                if (sites == null || sites.size() == 0) {
                    return;
                }
                for (com.huawei.hms.site.api.model.Site site : sites) {
                    Log.i("TAG", String.format("[%s], SiteId: '%s',Adress: '%s', name: %s\r\n", (count),site.getSiteId(), site.getAddress().getCountry(), site.getName()));
                   response.append(String.format("[%s], SiteId: '%s',Adress: '%s', name: %s\r\n", (count++),site.getSiteId(), site.getAddress().getCountry(), site.getName()));
                }
                tv2.setText(response);
            }
            // Return the result code and description upon a search exception.
            @Override
            public void onSearchError(SearchStatus status) {
                Log.i("TAG", "Error : " + status.getErrorCode() + " " + status.getErrorMessage());
            }
        });
    }
}