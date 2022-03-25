package vttp2022.ssf.Assessment.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.Assessment.models.Quotation;

@Service
public class QuotationService {

    private static final String URL = "https://quotation.chuklee.com/quotation";

    public Optional<Quotation> getQuotations(List<String> items){

        JsonArrayBuilder ab = Json.createArrayBuilder();
        for(String item : items) {
            ab.add(item);
        }
        JsonArray totalItems = ab.build();

        RequestEntity<String> req = RequestEntity
            .post(URL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(totalItems.toString(), String.class);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        if(resp.getStatusCodeValue() >= 400)
            return Optional.empty();

        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject body = reader.readObject();
        

        Quotation quotation = new Quotation();
                quotation.setQuoteId(body.getString("quoteId"));
                
                
                JsonArray quotationsArray = body.getJsonArray("quotations");
                System.out.println(">>>>>" + body.getJsonArray("quotations"));

                for(int i = 0; i < quotationsArray.size(); i++) {
                JsonObject item = quotationsArray.getJsonObject(i);
                String itemName = item.getString("item");
                Double unitPrice = item.getJsonNumber("unitPrice").doubleValue();
                quotation.addQuotation(itemName, unitPrice.floatValue());
                
        }

        return Optional.of(quotation);

    }
}
