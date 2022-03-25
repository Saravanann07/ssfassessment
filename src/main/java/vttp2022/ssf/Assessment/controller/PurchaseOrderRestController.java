package vttp2022.ssf.Assessment.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.Assessment.models.Quotation;
import vttp2022.ssf.Assessment.services.QuotationService;

@RestController
@RequestMapping(path="")
public class PurchaseOrderRestController {

    @Autowired
    private QuotationService quoSvc;


    @PostMapping(path = "/api/po", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postOrder(@RequestBody String payload) throws IOException {

        System.out.println(">>>>> payload: " + payload);

        InputStream is = new ByteArrayInputStream(payload.getBytes());
            JsonReader r = Json.createReader(is);
            JsonObject req = r.readObject();

            String name = req.getString("name");
            JsonArray lineItems = req.getJsonArray("lineItems");
            Map<String, Integer> itemMap = new HashMap<>();

            System.out.println(">>>>> " + lineItems);
            List<String> items = new ArrayList<>();

            for(int i =0; i<lineItems.size(); i++){
                items.add(lineItems.getJsonObject(i).getString("item").toString());
            }

            Optional<Quotation> quoOpt = quoSvc.getQuotations(new ArrayList<>(itemMap.keySet()));

            if(quoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("{}");
            }

            Quotation quotation = quoOpt.get();
            Float totalValue = 0.0f;

            for(Entry<String, Integer> entry : itemMap.entrySet()) {
                Float unitPrice = quotation.getQuotation(entry.getKey());
                totalValue += entry.getValue() * unitPrice;
            }
            
            JsonObject jObj = Json.createObjectBuilder()
                .add("invoiceId", quotation.getQuoteId())
                .add("name", name)
                .add("totalValue", totalValue)
                .build();

                System.out.println(">>>>>" + jObj);

            return ResponseEntity.ok().body(jObj.toString());
            
       
    }

    
    
    
        

    



    
}
