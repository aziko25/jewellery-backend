package jewellery.jewellery.CARATO_ONLY;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import jewellery.jewellery.DTO.TransactionCreateRequest;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static jewellery.jewellery.CARATO_ONLY.OrderService.ordersTableCollection;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService service;

    @PostMapping("/createTransaction")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionCreateRequest request) {

        String url = "https://my.click.uz/services/pay?service_id=30196&merchant_id=22689" +
                "&return_url=" + request.getReturnUrl() +
                "&amount=" + request.getOrderTotalSum() +
                "&transaction_param=" + request.getOrderId();

        return ResponseEntity.ok(url);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/allCollections")
    public ResponseEntity<?> getAllCollections() throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(service.getAllCollections());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(service.getAllOrders());
    }

    @PostMapping("/prepare-order")
    public ResponseEntity<?> prepareOrder(@RequestParam Map<String, String> body) {

        String clickTransId = body.get("click_trans_id");
        String merchantTransId = body.get("merchant_trans_id");

        Map<String, String> response = new HashMap<>();

        response.put("click_trans_id", clickTransId);
        response.put("merchant_trans_id", merchantTransId);
        response.put("merchant_prepare_id", merchantTransId);
        response.put("error", "0");
        response.put("error_note", "Success");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete-order")
    public ResponseEntity<?> completeOrder(@RequestParam Map<String, String> body) throws ExecutionException, InterruptedException {

        String clickTransId = body.get("click_trans_id");
        String merchantTransId = body.get("merchant_trans_id");
        String error = body.get("error");

        Map<String, Object> response = new HashMap<>();

        response.put("click_trans_id", clickTransId);
        response.put("merchant_trans_id", merchantTransId);
        Integer merchantConfirmId = error.equals("0") ? 1 : null;
        response.put("merchant_confirm_id", merchantConfirmId);

        String orderId = String.valueOf(body.get("merchant_trans_id"));

        Orders order = service.getOrderById(orderId);

        order.setStatus("Paid");
        order.setIsPaymentDone(true);

        Firestore firebaseDatabase = FirestoreClient.getFirestore();
        DocumentReference documentReference = firebaseDatabase.collection(ordersTableCollection).document(orderId);
        ApiFuture<WriteResult> writeResult = documentReference.set(order);

        writeResult.get();

        response.put("error", "0");
        response.put("error_note", "Success");

        return ResponseEntity.ok(response);
    }
}