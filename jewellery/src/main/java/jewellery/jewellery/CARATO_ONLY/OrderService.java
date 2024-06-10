package jewellery.jewellery.CARATO_ONLY;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderService {

    public static String ordersTableCollection = "Orders";

    public Orders getOrderById(String orderId) throws ExecutionException, InterruptedException {

        Firestore firebaseDatabase = FirestoreClient.getFirestore();

        DocumentReference documentReference = firebaseDatabase.collection(ordersTableCollection).document(orderId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        Orders order;

        if (document.exists()) {

            order = document.toObject(Orders.class);
        }
        else {

            throw new IllegalArgumentException("Order not found");
        }

        return order;
    }

    public List<Orders> getAllOrders() throws ExecutionException, InterruptedException {

        Firestore firebaseDatabase = FirestoreClient.getFirestore();

        CollectionReference ordersCollection = firebaseDatabase.collection(ordersTableCollection);
        ApiFuture<QuerySnapshot> future = ordersCollection.get();

        QuerySnapshot querySnapshot = future.get();
        List<Orders> ordersList = new ArrayList<>();

        if (querySnapshot != null) {

            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                if (document.exists()) {

                    Orders order = document.toObject(Orders.class);
                    if (order != null) {
                        ordersList.add(order);
                    }
                }
            }
        }

        return ordersList;
    }

    public List<String> getAllCollections() throws ExecutionException, InterruptedException {
        Firestore firebaseDatabase = FirestoreClient.getFirestore();
        return listCollectionsRecursively(firebaseDatabase.listCollections());
    }

    private List<String> listCollectionsRecursively(Iterable<CollectionReference> collections) throws ExecutionException, InterruptedException {
        List<String> collectionNames = new ArrayList<>();
        for (CollectionReference collection : collections) {
            collectionNames.add(collection.getPath());
            listSubCollections(collection, collectionNames);
        }
        return collectionNames;
    }

    private void listSubCollections(CollectionReference parentCollection, List<String> collectionNames) throws ExecutionException, InterruptedException {
        Iterable<QueryDocumentSnapshot> documents = parentCollection.get().get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            DocumentReference docRef = document.getReference();
            Iterable<CollectionReference> subCollections = docRef.listCollections();
            for (CollectionReference subCollection : subCollections) {
                collectionNames.add(subCollection.getPath());
                listSubCollections(subCollection, collectionNames); // Recursive call
            }
        }
    }
}