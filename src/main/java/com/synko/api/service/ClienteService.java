package com.synko.api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClienteService {

        private final Firestore firestore = FirestoreClient.getFirestore();

        public void salvarCliente(String id, String nome, String email) throws ExecutionException, InterruptedException {
            Map<String, Object> data = new HashMap<>();
            data.put("nome", nome);
            data.put("email", email);

            ApiFuture<WriteResult> result = firestore.collection("clientes").document(id).set(data);
            System.out.println("Cliente salvo em: " + result.get().getUpdateTime());
        }

        public Map< String, Object> buscarCliente(String id) throws ExecutionException, InterruptedException {
            DocumentSnapshot snapshot = firestore.collection("clientes").document(id).get().get();
            return snapshot.exists() ? snapshot.getData() : null;
        }

}
